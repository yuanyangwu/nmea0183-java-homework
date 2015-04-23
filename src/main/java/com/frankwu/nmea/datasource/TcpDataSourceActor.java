package com.frankwu.nmea.datasource;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by wuf2 on 4/19/2015.
 */
public class TcpDataSourceActor extends UntypedActor {
    private static final int TIMEOUT_IN_MS = 500;

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private int port;
    private boolean running = false;
    private Thread acceptThread;

    public TcpDataSourceActor(int port) {
        this.port = port;
    }

    public static Props props(int port) {
        return Props.create(new Creator<TcpDataSourceActor>() {
            @Override
            public TcpDataSourceActor create() throws Exception {
                return new TcpDataSourceActor(port);
            }
        });
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void preStart() throws Exception {
        logger.debug("TcpDataSourceActor.preStart");
        acceptThread = new AcceptThread(this);
        running = true;
        acceptThread.start();
    }

    @Override
    public void postStop() throws Exception {
        logger.debug("TcpDataSourceActor.postStop");
        try {
            running = false;
            acceptThread.join();
        } catch (InterruptedException e) {
            logger.error("shutdown TcpDataSource fail: {}", e);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("receiving " + message.toString());
        unhandled(message);
    }

    private static class AcceptThread extends Thread {
        private final Logger logger = LoggerFactory.getLogger(AcceptThread.class);
        private TcpDataSourceActor tcpDataSourceActor;

        public AcceptThread(TcpDataSourceActor tcpDataSourceActor) {
            super("TcpDataSourceActor.AcceptThread");
            this.tcpDataSourceActor = tcpDataSourceActor;
        }

        @Override
        public void run() {
            try (
                    ServerSocket serverSocket =
                            new ServerSocket(tcpDataSourceActor.getPort());
            ) {
                logger.debug("TcpDataSourceActor.AcceptThread listening");
                serverSocket.setSoTimeout(TIMEOUT_IN_MS);
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        new ReceiveThread(clientSocket, tcpDataSourceActor).start();
                    } catch (SocketTimeoutException e) {
                        if (!tcpDataSourceActor.isRunning()) {
                            serverSocket.close();
                            logger.debug("TcpDataSourceActor.AcceptThread ends");
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Accept incoming TCP data fail: {}", e);
            }
        }
    }

    private static class ReceiveThread extends Thread {
        private final Logger logger = LoggerFactory.getLogger(ReceiveThread.class);
        private Socket socket;
        private TcpDataSourceActor tcpDataSourceActor;

        public ReceiveThread(Socket socket, TcpDataSourceActor tcpDataSourceActor) {
            super("TcpDataSourceActor.ReceiveThread");
            this.socket = socket;
            this.tcpDataSourceActor = tcpDataSourceActor;
        }

        @Override
        public void run() {
            logger.info("incoming stream starts {}", socket.getInetAddress().toString());
            try (
                    InputStream in = socket.getInputStream();
            ) {
                byte[] buf = new byte[128];
                final ActorSelection codecManagerActor = tcpDataSourceActor.getContext().actorSelection("../codecManager");
                while (true) {
                    try {
                        int count = in.read(buf);
                        if (count < 0) {
                            logger.info("incoming stream ends for peer socket is closed {}", socket.getInetAddress().toString());
                            break;
                        }
                        String data = new String(buf, 0, count, Charsets.US_ASCII);
                        logger.debug("{} receive: {}", socket.getInetAddress().toString(), data);
                        codecManagerActor.tell(data, tcpDataSourceActor.getSender());
                    } catch (SocketTimeoutException e) {
                        if (!tcpDataSourceActor.isRunning()) {
                            logger.info("incoming stream ends for server is shut down {}", socket.getInetAddress().toString());
                            break;
                        }
                    }
                }
                socket.close();
            } catch (Exception e) {
                logger.error("Handle incoming TCP data fail {}", e);
            }
        }
    }
}

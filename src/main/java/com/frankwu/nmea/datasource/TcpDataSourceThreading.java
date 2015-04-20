package com.frankwu.nmea.datasource;

import com.frankwu.nmea.CodecManager;
import com.frankwu.nmea.queue.AbstractBoundQueue;
import com.frankwu.nmea.queue.SemaphoreBoundQueue;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by wuf2 on 4/3/2015.
 */
public class TcpDataSourceThreading {
    private static final int TIMEOUT_IN_MS = 500;
    private static final int QUEUE_SIZE = 50;

    private final Logger logger = LoggerFactory.getLogger(TcpDataSourceThreading.class);
    private int port;
    private CodecManager codecManager;
    private AbstractBoundQueue<String> queue = new SemaphoreBoundQueue<String>(QUEUE_SIZE);
    private Thread acceptThread;
    private Thread decodeThread;
    private volatile boolean running = false;

    public TcpDataSourceThreading(int port, CodecManager codecManager) {
        this.port = port;
        this.codecManager = codecManager;
    }

    public void start() {
        acceptThread = new AcceptThread(this);
        decodeThread = new DecodeThread(this);
        running = true;
        decodeThread.start();
        acceptThread.start();

    }

    public void shutdown() {
        try {
            running = false;
            acceptThread.join();
            queue.put(null);
            decodeThread.join();
        } catch (InterruptedException e) {
            logger.error("shutdown TcpDataSourceThreading fail: {}", e);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public CodecManager getCodecManager() {
        return codecManager;
    }

    public int getPort() {
        return port;
    }

    public AbstractBoundQueue<String> getQueue() {
        return queue;
    }

    private static class AcceptThread extends Thread {
        private final Logger logger = LoggerFactory.getLogger(AcceptThread.class);
        private TcpDataSourceThreading tcpDataSourceThreading;

        public AcceptThread(TcpDataSourceThreading tcpDataSourceThreading) {
            super("TcpDataSourceThreading.AcceptThread");
            this.tcpDataSourceThreading = tcpDataSourceThreading;
        }

        @Override
        public void run() {
            try (
                    ServerSocket serverSocket =
                            new ServerSocket(tcpDataSourceThreading.getPort());
            ) {
                serverSocket.setSoTimeout(TIMEOUT_IN_MS);
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        new ReceiveThread(clientSocket, tcpDataSourceThreading).start();
                    } catch (SocketTimeoutException e) {
                        if (!tcpDataSourceThreading.isRunning()) {
                            serverSocket.close();
                            logger.debug("TcpDataSourceThreading.AcceptThread ends");
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
        private TcpDataSourceThreading tcpDataSourceThreading;

        public ReceiveThread(Socket socket, TcpDataSourceThreading tcpDataSourceThreading) {
            super("TcpDataSourceThreading.ReceiveThread");
            this.socket = socket;
            this.tcpDataSourceThreading = tcpDataSourceThreading;
        }

        @Override
        public void run() {
            logger.info("incoming stream starts {}", socket.getInetAddress().toString());
            try (
                    InputStream in = socket.getInputStream();
            ) {
                byte[] buf = new byte[128];
                while (true) {
                    try {
                        int count = in.read(buf);
                        if (count < 0) {
                            logger.info("incoming stream ends for peer socket is closed {}", socket.getInetAddress().toString());
                            break;
                        }
                        String data = new String(buf, 0, count, Charsets.US_ASCII);
                        logger.debug("{} receive: {}", socket.getInetAddress().toString(), data);
                        tcpDataSourceThreading.getQueue().put(data);
                    } catch (SocketTimeoutException e) {
                        if (!tcpDataSourceThreading.isRunning()) {
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

    private static class DecodeThread extends Thread {
        private final Logger logger = LoggerFactory.getLogger(DecodeThread.class);
        private TcpDataSourceThreading tcpDataSourceThreading;

        public DecodeThread(TcpDataSourceThreading tcpDataSourceThreading) {
            super("TcpDataSourceThreading.DecodeThread");
            this.tcpDataSourceThreading = tcpDataSourceThreading;
        }

        @Override
        public void run() {
            logger.info("Decode thread starts");
            try {
                while (true) {
                    String data = tcpDataSourceThreading.getQueue().take();
                    if (data == null) {
                        break;
                    }
                    tcpDataSourceThreading.getCodecManager().decode(data);
                }
            } catch (Exception e) {
                logger.error("Decode TCP data fail {}", e);
            }
            logger.info("Decode thread ends");
        }
    }
}

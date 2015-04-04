package com.frankwu.nmea.datasource;

import com.frankwu.nmea.CodecManager;
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
public class TcpDataSource extends AbstractDataSource {
    private static final int TIMEOUT_IN_MS = 500;
    private final Logger logger = LoggerFactory.getLogger(TcpDataSource.class);
    private int port;
    private CodecManager codecManager;
    private Thread acceptThread;
    private volatile boolean running = false;

    public TcpDataSource(int port, CodecManager codecManager) {
        this.port = port;
        this.codecManager = codecManager;
    }

    public void start() {
        acceptThread = new AcceptThread(this);
        running = true;
        acceptThread.start();
    }

    public void shutdown() {
        try {
            running = false;
            acceptThread.join();
        } catch (InterruptedException e) {
            logger.error("shutdown TcpDataSource fail: {}", e);
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

    private static class AcceptThread extends Thread {
        private final Logger logger = LoggerFactory.getLogger(AcceptThread.class);
        private TcpDataSource tcpDataSource;

        public AcceptThread(TcpDataSource tcpDataSource) {
            super("TcpDataSource.AcceptThread");
            this.tcpDataSource = tcpDataSource;
        }

        @Override
        public void run() {
            try (
                    ServerSocket serverSocket =
                            new ServerSocket(tcpDataSource.getPort());
            ) {
                serverSocket.setSoTimeout(TIMEOUT_IN_MS);
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        new ReceiveThread(clientSocket, tcpDataSource).start();
                    } catch (SocketTimeoutException e) {
                        if (!tcpDataSource.isRunning()) {
                            serverSocket.close();
                            logger.debug("TcpDataSource.AcceptThread ends");
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
        private TcpDataSource tcpDataSource;

        public ReceiveThread(Socket socket, TcpDataSource tcpDataSource) {
            super("TcpDataSource.ReceiveThread");
            this.socket = socket;
            this.tcpDataSource = tcpDataSource;
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
                        tcpDataSource.getCodecManager().decode(data);
                    } catch (SocketTimeoutException e) {
                        if (!tcpDataSource.isRunning()) {
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
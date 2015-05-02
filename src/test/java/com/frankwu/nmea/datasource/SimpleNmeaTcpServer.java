package com.frankwu.nmea.datasource;

import com.google.common.collect.Lists;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

/**
 * Created by wuf2 on 5/2/2015.
 */
public class SimpleNmeaTcpServer {
    private final int port;
    private final Collection<String> contents;

    public SimpleNmeaTcpServer(int port, Collection<String> contents) {
        this.port = port;
        this.contents = contents;
    }

    public void run() {
        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ) {
            System.out.println("Start listening on " + port);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());

            for (String content: contents) {
                out.write(content);
                out.flush();
            }

            out.close();
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Accept incoming TCP data fail: " + e);
        }
    }

    public static void main(String[] args) {
        new SimpleNmeaTcpServer(
                8007,
                Lists.newArrayList("$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n")
                )
                .run();
    }
}

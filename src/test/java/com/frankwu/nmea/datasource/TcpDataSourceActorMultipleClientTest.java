package com.frankwu.nmea.datasource;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.frankwu.nmea.CodecManager;
import com.frankwu.nmea.NmeaApplication;
import com.frankwu.nmea.NmeaObjectMonitorActor;
import com.frankwu.nmea.protobuf.ProtobufCodecManager;
import com.frankwu.nmea.testing.CountingObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 4/20/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NmeaApplication.class)
public class TcpDataSourceActorMultipleClientTest {
    private final static long TIMEOUT = 500;

    @Autowired
    private String monitorAddress;

    @Autowired
    private ProtobufCodecManager protobufCodecManager;

    @Autowired
    private CodecManager tcpCodecManager;

    @Autowired
    private int tcpDataSourcePort;

    private ActorSystem system;
    private CountingObserver countingObserver = new CountingObserver();
    private final static int CLIENT_NUM = 3;
    Socket[] clientSocket = new Socket[CLIENT_NUM];
    PrintWriter[] out = new PrintWriter[CLIENT_NUM];

    @Before
    public void setup() {
        countingObserver.setCount(0);
        tcpCodecManager.addObserver(countingObserver);

        system = ActorSystem.create("TcpDataSourceActorMultipleClientTest");
        system.actorOf(NmeaObjectMonitorActor.props(protobufCodecManager, monitorAddress), "nmeaObjectMonitor");
        final ActorRef tcpDataSourceRef = system.actorOf(TcpDataSourceActor.props(
                tcpDataSourcePort, tcpCodecManager, protobufCodecManager, monitorAddress), "tcpDataSource");

        try {
            Thread.sleep(TIMEOUT);
            for (int i = 0; i < CLIENT_NUM; i++) {
                clientSocket[i] = new Socket("127.0.0.1", tcpDataSourcePort);
                out[i] = new PrintWriter(clientSocket[i].getOutputStream(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void teardown() {
        try {
            for (int i = 0; i < CLIENT_NUM; i++) {
                out[i].close();
                out[i] = null;
                clientSocket[i].close();
                clientSocket[i] = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tcpCodecManager.deleteObservers();
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void singleObject() throws InterruptedException {
        String content;
        content = "$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n";
        out[0].write(content);
        out[0].flush();
        Thread.sleep(TIMEOUT);
        assertEquals(1, countingObserver.getCount());

        content = "$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A\r\n";
        out[1].write(content);
        out[1].flush();
        Thread.sleep(TIMEOUT);
        assertEquals(2, countingObserver.getCount());

        content = "$GPGSV,1,1,08,01,40,083,46,02,17,308,41,12,07,344,39*4A\r\n";
        out[2].write(content);
        out[2].flush();
        Thread.sleep(TIMEOUT);
        assertEquals(3, countingObserver.getCount());
    }

    @Test
    public void partialObject()  throws InterruptedException {
        String content;
        content = "$GPGGA,092750.000,5321.6802,N,";
        out[0].write(content);
        out[0].flush();

        content = "$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A\r\n";
        out[1].write(content);
        out[1].flush();

        content = "$GPGSV,";
        out[2].write(content);
        out[2].flush();

        content = "00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n";
        out[0].write(content);
        out[0].flush();

        content = "1,1,08,01,40,083,46,02,17,308,41,12,07,344,39*4A\r\n";
        out[2].write(content);
        out[2].flush();
        Thread.sleep(TIMEOUT);
        assertEquals(3, countingObserver.getCount());
    }
}

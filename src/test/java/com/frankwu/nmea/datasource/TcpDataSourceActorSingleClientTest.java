package com.frankwu.nmea.datasource;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.frankwu.nmea.CodecManager;
import com.frankwu.nmea.testing.CountingObserver;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 4/19/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context.xml"})
public class TcpDataSourceActorSingleClientTest {
    private final static long TIMEOUT = 500;

    @Autowired
    private CodecManager tcpCodecManager;

    @Autowired
    private int tcpDataSourcePort;

    private ActorSystem system;
    private CountingObserver countingObserver = new CountingObserver();
    Socket clientSocket;
    PrintWriter out;

    @Before
    public void setup() {
        countingObserver.setCount(0);
        tcpCodecManager.addObserver(countingObserver);

        system = ActorSystem.create("TcpDataSourceActorSingleClientTest");
        final ActorRef tcpDataSourceRef = system.actorOf(TcpDataSourceActor.props(tcpDataSourcePort, tcpCodecManager), "tcpDataSource");

        try {
            clientSocket = new Socket("127.0.0.1", tcpDataSourcePort);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void teardown() {
        try {
            out.close();
            clientSocket.close();
            tcpCodecManager.deleteObservers();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void singleObject() throws IOException, InterruptedException {
        out.write("$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n");
        out.flush();
        Thread.sleep(TIMEOUT);
        assertEquals(1, countingObserver.getCount());
    }

    @Test
    public void partialObject() throws IOException, InterruptedException {
        out.write("$GPRMC,092751.000,A,5321.6802,");
        out.flush();
        out.write("N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n");
        out.flush();
        Thread.sleep(TIMEOUT);
        assertEquals(1, countingObserver.getCount());
    }

    @Test
    public void multipleObjects() throws IOException, InterruptedException {
        String content;
        content = "$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n";
        out.write(content);
        out.flush();

        content = "$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A\r\n";
        out.write(content);
        out.flush();

        content = "$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n";
        out.write(content);
        out.flush();

        content = "$GPGSV,1,1,08,01,40,083,46,02,17,308,41,12,07,344,39*4A\r\n";
        out.write(content);
        out.flush();

        content = "!AIVDM,1,1,,B,16:>>s5Oh08dLO8AsMAVqptj0@>p,0*67\r\n";
        out.write(content);
        out.flush();

        content = "!AIVDM,2,1,2,A,569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@,0*25\r\n";
        out.write(content);
        out.flush();

        content = "!AIVDM,2,2,2,A,0000000000>,2*2A\r\n";
        out.write(content);
        out.flush();

        Thread.sleep(TIMEOUT);
        assertEquals(6, countingObserver.getCount());
    }
}

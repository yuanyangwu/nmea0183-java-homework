package com.frankwu.nmea.datasource;

import com.frankwu.nmea.CodecManager;
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
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 4/3/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context.xml"})
public class TcpDataSourceThreadingSingleClientTest {
    private final static long TIMEOUT = 500;

    @Autowired
    private CodecManager codecManager;

    @Autowired
    private int tcpDataSourcePort;

    private TcpDataSourceThreading tcpDataSourceThreading;

    private CountingObserver countingObserver = new CountingObserver();
    Socket clientSocket;
    PrintWriter out;

    @Before
    public void setup() {
        countingObserver.setCount(0);
        codecManager.addObserver(countingObserver);

        tcpDataSourceThreading = new TcpDataSourceThreading(tcpDataSourcePort, codecManager);
        tcpDataSourceThreading.start();

        try {
            clientSocket = new Socket("127.0.0.1", tcpDataSourcePort);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            out.close();
            clientSocket.close();
            codecManager.deleteObservers();

            tcpDataSourceThreading.shutdown();
            tcpDataSourceThreading = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
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

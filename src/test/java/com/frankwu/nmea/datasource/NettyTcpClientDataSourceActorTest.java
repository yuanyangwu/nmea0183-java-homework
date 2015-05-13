package com.frankwu.nmea.datasource;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.frankwu.nmea.CodecManager;
import com.frankwu.nmea.NmeaApplication;
import com.frankwu.nmea.NmeaObjectMonitorActor;
import com.frankwu.nmea.testing.CountingObserver;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 5/2/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NmeaApplication.class)
public class NettyTcpClientDataSourceActorTest {
    private final static long TIMEOUT = 500;

    @Autowired
    private String monitorAddress;

    @Autowired
    private CodecManager nettyTcpClientCodecManager;

    @Autowired
    private String nettyTcpClientDataSourceTargetHost;

    @Autowired
    private int nettyTcpClientDataSourceTargetPort;

    private ActorSystem system;
    private CountingObserver countingObserver = new CountingObserver();

    @Before
    public void setup() {
        countingObserver.setCount(0);
        nettyTcpClientCodecManager.addObserver(countingObserver);

        system = ActorSystem.create("NettyTcpClientDataSourceActorSingleClientTest");
        system.actorOf(NmeaObjectMonitorActor.props(monitorAddress), "nmeaObjectMonitor");
        final ActorRef nettyTcpServerDataSourceRef = system.actorOf(
                NettyTcpClientDataSourceActor.props(
                        "localhost", nettyTcpClientDataSourceTargetPort, nettyTcpClientCodecManager, monitorAddress),
                "NettyTcpClientDataSourceActorTest");
    }

    @After
    public void teardown() {
        nettyTcpClientCodecManager.deleteObservers();
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void singleObject() throws InterruptedException {
        Collection<String> contents = Lists.newArrayList("$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n");
        new SimpleNmeaTcpServer(nettyTcpClientDataSourceTargetPort, contents).run();
        Thread.sleep(TIMEOUT);
        assertEquals(1, countingObserver.getCount());
    }

    @Test
    public void reconnectWithPartialObject() throws InterruptedException {
        Collection<String> contents = Lists.newArrayList("$GPRMC,092751.000,A,5321.6802,");
        new SimpleNmeaTcpServer(nettyTcpClientDataSourceTargetPort, contents).run();

        contents = Lists.newArrayList("N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n");
        new SimpleNmeaTcpServer(nettyTcpClientDataSourceTargetPort, contents).run();
        Thread.sleep(TIMEOUT);
        assertEquals(1, countingObserver.getCount());
    }

    @Test
    public void reconnectWithMultipleObjects() throws InterruptedException {
        Collection<String> contents = Lists.newArrayList(
                "$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n",
                "$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A\r\n",
                "$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n"
        );
        new SimpleNmeaTcpServer(nettyTcpClientDataSourceTargetPort, contents).run();

        contents = Lists.newArrayList(
                "$GPGSV,1,1,08,01,40,083,46,02,17,308,41,12,07,344,39*4A\r\n",
                "!AIVDM,1,1,,B,16:>>s5Oh08dLO8AsMAVqptj0@>p,0*67\r\n",
                "!AIVDM,2,1,2,A,569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@,0*25\r\n",
                "!AIVDM,2,2,2,A,0000000000>,2*2A\r\n"
        );
        new SimpleNmeaTcpServer(nettyTcpClientDataSourceTargetPort, contents).run();
        Thread.sleep(TIMEOUT);
        assertEquals(6, countingObserver.getCount());
    }
}

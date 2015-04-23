package com.frankwu.nmea.datasource;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.frankwu.nmea.CodecManager;
import com.frankwu.nmea.testing.CountingObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 4/20/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context.xml"})
public class FileDataSourceActorTest {
    private final static long TIMEOUT = 500;

    @Autowired
    private CodecManager codecManager;

    private ActorSystem system;
    private ActorRef fileDataSourceActorRef;
    private CountingObserver countingObserver = new CountingObserver();

    @Before
    public void setup() {
        countingObserver.setCount(0);
        codecManager.addObserver(countingObserver);

        system = ActorSystem.create("FileDataSourceActorTest");
        fileDataSourceActorRef = system.actorOf(FileDataSourceActor.props(Paths.get("doc/sample.txt"), codecManager), "fileDataSource");
    }

    @After
    public void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void validFile() throws InterruptedException {
        fileDataSourceActorRef.tell("start", ActorRef.noSender());
        Thread.sleep(TIMEOUT);
        assertEquals(6, countingObserver.getCount());
    }
}

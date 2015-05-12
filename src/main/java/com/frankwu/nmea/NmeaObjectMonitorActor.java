package com.frankwu.nmea;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Created by wuf2 on 5/12/2015.
 */
public class NmeaObjectMonitorActor extends UntypedActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private final String address;
    private Thread subscriberThread;

    public NmeaObjectMonitorActor(String address) {
        this.address = address;
    }

    public static Props props(String address) {
        return Props.create(new Creator<NmeaObjectMonitorActor>() {
            @Override
            public NmeaObjectMonitorActor create() throws Exception {
                return new NmeaObjectMonitorActor(address);
            }
        });
    }

    public void preStart() throws Exception {
        logger.debug("NmeaObjectMonitorActor.preStart");
        subscriberThread = new SubscriberThread(address);
        subscriberThread.start();
    }

    @Override
    public void postStop() throws Exception {
        logger.debug("NmeaObjectMonitorActor.postStop");
        try {
            subscriberThread.interrupt();
            subscriberThread.join();
        } catch (InterruptedException e) {
            logger.error("shutdown NmeaObjectMonitorActor fail: {}", e);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("receiving " + message.toString());
        unhandled(message);
    }

    private static class SubscriberThread extends Thread {
        private final Logger logger = LoggerFactory.getLogger(SubscriberThread.class);
        private final String address;

        public SubscriberThread(String address) {
            super("NmeaObjectMonitorActor.SubscriberThread");
            this.address = address;
        }

        @Override
        public void run() {
            ZMQ.Context zmqContext = ZMQ.context(1);
            ZMQ.Socket subscriber = zmqContext.socket(ZMQ.SUB);
            try {
                subscriber.connect(address);
                // disable filter and accept all sources
                subscriber.subscribe("".getBytes());
                while (!Thread.currentThread().isInterrupted()) {
                    String envelop = subscriber.recvStr();
                    byte[] bytes = subscriber.recv();
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    Object object = objectInputStream.readObject();
                    logger.info("subscriber receive: {} - {}", envelop, object);
                }
            } catch (Exception e) {
                logger.error("subscriber fail", e);
            } finally {
                subscriber.close();
                zmqContext.term();
            }
        }
    }
}

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
    private Thread monitorThread;

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
        monitorThread = new MonitorThread(address);
        monitorThread.start();
    }

    @Override
    public void postStop() throws Exception {
        logger.debug("NmeaObjectMonitorActor.postStop");
        try {
            monitorThread.interrupt();
            monitorThread.join();
        } catch (InterruptedException e) {
            logger.error("shutdown NmeaObjectMonitorActor fail: {}", e);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("receiving " + message.toString());
        unhandled(message);
    }

    private static class MonitorThread extends Thread {
        private final Logger logger = LoggerFactory.getLogger(MonitorThread.class);
        private final String address;

        public MonitorThread(String address) {
            super("NmeaObjectMonitorActor.MonitorThread");
            this.address = address;
        }

        @Override
        public void run() {
            ZMQ.Context zmqContext = ZMQ.context(1);
            ZMQ.Socket monitor = zmqContext.socket(ZMQ.PULL);
            try {
                monitor.bind(address);
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] bytes = monitor.recv(0);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    Object object = objectInputStream.readObject();
                    logger.info("monitor receive: {}", object);
                }
            } catch (Exception e) {
                logger.error("monitor fail", e);
            } finally {
                monitor.close();
                zmqContext.term();
            }
        }
    }
}

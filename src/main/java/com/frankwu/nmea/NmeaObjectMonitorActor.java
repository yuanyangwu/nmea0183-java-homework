package com.frankwu.nmea;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import com.frankwu.nmea.protobuf.NmeaObjects;
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
    private boolean running = false;
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

    public String getAddress() {
        return address;
    }

    public boolean isRunning() {
        return running;
    }

    public void preStart() throws Exception {
        logger.debug("NmeaObjectMonitorActor.preStart");
        running = true;
        monitorThread = new MonitorThread(this);
        monitorThread.start();
    }

    @Override
    public void postStop() throws Exception {
        logger.debug("NmeaObjectMonitorActor.postStop");
        try {
            running = false;
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
        private static final int TIMEOUT_IN_MS = 1000;
        private final Logger logger = LoggerFactory.getLogger(MonitorThread.class);
        private final NmeaObjectMonitorActor actor;

        public MonitorThread(NmeaObjectMonitorActor actor) {
            super("NmeaObjectMonitorActor.MonitorThread");
            this.actor = actor;
        }

        @Override
        public void run() {
            ZMQ.Context zmqContext = ZMQ.context(1);
            ZMQ.Socket socket = zmqContext.socket(ZMQ.SUB);
            socket.subscribe("".getBytes());
            ZMQ.Poller poller = new ZMQ.Poller(1);
            try {
                socket.bind(actor.getAddress());
                poller.register(socket, ZMQ.Poller.POLLIN);
                while (actor.isRunning()) {
                    // use poller to check running flag periodically
                    if (0 == poller.poll(TIMEOUT_IN_MS)) {
                        continue;
                    }
                    byte[] bytes = socket.recv(0);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

                    // Java serialization
                    //ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    //Object object = objectInputStream.readObject();

                    // Protobuf serialization
                    NmeaObjects.NmeaObject object = NmeaObjects.NmeaObject.parseFrom(byteArrayInputStream);

                    logger.info("monitor receive: {}", object);
                }
                logger.debug("monitor gracefully shut down");
            } catch (Exception e) {
                logger.error("monitor fail", e);
            } finally {
                socket.close();
                zmqContext.term();
            }
        }
    }
}

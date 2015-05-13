package com.frankwu.nmea;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import org.zeromq.ZMQ;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 4/15/2015.
 */
public class CodecManagerActor extends UntypedActor implements Observer {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private final CodecManager codecManager;
    private final String monitorAddress;
    private String envelop;
    private ZMQ.Context zmqContext;
    private ZMQ.Socket socket;

    public CodecManagerActor(CodecManager codecManager, String monitorAddress) {
        this.codecManager = codecManager;
        this.monitorAddress = monitorAddress;
    }

    public static Props props(CodecManager codecManager, String monitorAddress) {
        return Props.create(new Creator<CodecManagerActor>() {
            @Override
            public CodecManagerActor create() throws Exception {
                return new CodecManagerActor(codecManager, monitorAddress);
            }
        });
    }

    @Override
    public void preStart() throws Exception {
        envelop = getSelf().path().toString();
        zmqContext = ZMQ.context(1);
        socket = zmqContext.socket(ZMQ.PUSH);
        socket.connect(monitorAddress);
        codecManager.addObserver(this);
    }

    @Override
    public void postStop() throws Exception {
        codecManager.deleteObserver(this);
        // if monitor Actor is down or disconnected, pending packets can block socket.close()
        // discard pending packets with ZMQ_LINGER = 0
        socket.setLinger(0);
        socket.close();
        zmqContext.term();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("receiving " + message.toString());

        if (message instanceof String) {
            codecManager.decode((String) message);
        } else {
            unhandled(message);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        logger.debug("Observer receive {}", arg);
        try {
            if (arg instanceof AbstractNmeaObject) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(arg);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                // send in NOBLOCK in case that receiver is not listening
                socket.send(bytes, ZMQ.NOBLOCK);
            }
        } catch (Exception e) {
            logger.error(e, "Observer update fail");
        }
    }
}

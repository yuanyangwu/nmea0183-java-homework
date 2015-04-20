package com.frankwu.nmea;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wuf2 on 4/15/2015.
 */
public class CodecManagerActor extends UntypedActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private CodecManager codecManager;

    public CodecManagerActor(CodecManager codecManager) {
        this.codecManager = codecManager;
    }

    public static Props props(CodecManager codecManager) {
        return Props.create(new Creator<CodecManagerActor>() {
            @Override
            public CodecManagerActor create() throws Exception {
                return new CodecManagerActor(codecManager);
            }
        });
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("receiving " + message.toString());

        if (message instanceof String) {
            codecManager.decode((String)message);
        } else {
            unhandled(message);
        }
    }
}

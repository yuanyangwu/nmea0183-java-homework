package com.frankwu.nmea;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 2/13/2015.
 */
public class CodecManager extends Observable implements Observer {
    private final Logger logger = LoggerFactory.getLogger(CodecManager.class);
    private CodecFactory codecFactory;
    private Buffer buffer = new Buffer();

    public CodecManager(CodecFactory codecFactory) {
        this.codecFactory = codecFactory;
        this.codecFactory.addObserver(this);
    }

    public synchronized void  decode(String content) throws Exception {
        Preconditions.checkNotNull(content, "content is null");

        List<String> contents = buffer.appendContent(content);
        for (String msg : contents) {
            if (NmeaMessageValidator.isValid(msg)) {
                logger.trace("decode() message: " + msg);
                String type = msg.substring(3, 6);
                try {
                    AbstractNmeaCodec codec = codecFactory.create(type);
                    codec.decode(msg);
                } catch (Exception e) {
                    logger.error("decode() message fail: " + msg);
                    throw e;
                }
            } else {
                logger.error("decode() invalid message: " + msg);
            }
        }
    }

    public List<String> encode(AbstractNmeaObject obj) throws Exception {
        try {
            Preconditions.checkNotNull(obj);
            String objType = obj.getObjType();
            AbstractNmeaCodec codec = codecFactory.create(objType.substring(objType.length() - 3));
            return codec.encode(obj);
        } catch (Exception e) {
            logger.error("encode() message fail: " + obj);
            throw e;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        logger.info("parsed object: " + arg);
        setChanged();
        notifyObservers(arg);
    }
}

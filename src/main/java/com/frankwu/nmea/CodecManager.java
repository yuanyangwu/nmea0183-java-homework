package com.frankwu.nmea;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 2/13/2015.
 */
public class CodecManager extends Observable implements Observer {
    private final Logger logger = LoggerFactory.getLogger(CodecManager.class);
    private HashMap<String, AbstractNmeaCodec> codecs = new HashMap<>();
    private Buffer buffer = new Buffer();

    public AbstractNmeaCodec createCodec(String type) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Preconditions.checkNotNull(type);
        Preconditions.checkArgument(type.length() == 3, "type length is expected to be 3 but " + type.length());

        AbstractNmeaCodec codec = codecs.get(type);
        if (codec == null) {
            String name = "com.frankwu.nmea." + type.substring(0, 1) + type.substring(1).toLowerCase() + "NmeaCodec";
            codec = (AbstractNmeaCodec) Class.forName(name).newInstance();
            codec.addObserver(this);
            codecs.put(type, codec);
        }

        return codec;
    }

    public void decode(String content) throws Exception {
        Preconditions.checkNotNull(content, "content is null");

        List<String> contents = buffer.appendContent(content);
        for (String msg : contents) {
            if (NmeaMessageValidator.isValid(msg)) {
                logger.trace("decode() message: " + msg);
                String type = msg.substring(3, 6);
                try {
                    AbstractNmeaCodec codec = createCodec(type);
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
            AbstractNmeaCodec codec = createCodec(objType.substring(objType.length() - 3));
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

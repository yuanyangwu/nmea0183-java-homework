package com.frankwu.nmea;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by wuf2 on 2/13/2015.
 */
public class CodecManager {
    private final static Logger logger = Logger.getLogger(CodecManager.class);

    private static AbstractNmeaCodec createCodec(String type) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (type.length() != 3) {
            throw new IllegalArgumentException("type must be 3-char long");
        }
        String name = "com.frankwu.nmea." + type.substring(0, 1) + type.substring(1).toLowerCase() + "NmeaCodec";
        return (AbstractNmeaCodec) Class.forName(name).newInstance();
    }

    public void decode(String content) throws Exception {
        for (String msg : content.split(NmeaConst.MSG_END)) {
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
            String objType = obj.getObjType();
            AbstractNmeaCodec codec = createCodec(objType.substring(objType.length() - 3));
            return codec.encode(obj);
        } catch (Exception e) {
            logger.error("encode() message fail: " + obj);
            throw e;
        }
    }
}

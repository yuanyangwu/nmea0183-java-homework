package com.frankwu.nmea;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by wuf2 on 2/13/2015.
 */
public class CodecManager {
    private final static Logger logger = Logger.getLogger(CodecManager.class);

    // TODO: refactor all codec instances to singleton?
    private static AbstractNmeaCodec createCodec(String type) {
        if (type.equals(NmeaConst.MSG_TYPE_GGA)) {
            return new GgaNmeaCodec();
        } else if (type.equals(NmeaConst.MSG_TYPE_GLL)) {
            return new GllNmeaCodec();
        } else if (type.equals(NmeaConst.MSG_TYPE_RMC)) {
            return new RmcNmeaCodec();
        } else if (type.equals(NmeaConst.MSG_TYPE_VDM)) {
            return new VdmNmeaCodec();
        }

        throw new IllegalArgumentException("Unsupported message type: " + type);
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

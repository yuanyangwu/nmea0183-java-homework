package com.frankwu.nmea;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class RmcNmeaCodec extends ParametricSentenceCodec<RmcNmeaObject> {
    public RmcNmeaCodec() {
        super(RmcNmeaObject.class);
    }
}

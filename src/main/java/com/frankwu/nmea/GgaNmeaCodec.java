package com.frankwu.nmea;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class GgaNmeaCodec extends ParametricSentenceCodec<GgaNmeaObject> {
    public GgaNmeaCodec() {
        super(GgaNmeaObject.class);
    }
}

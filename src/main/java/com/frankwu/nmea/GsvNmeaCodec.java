package com.frankwu.nmea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by wuf2 on 3/29/2015.
 */
public class GsvNmeaCodec extends ParametricSentenceCodec<GsvNmeaObject>  {
    public GsvNmeaCodec() {
        super(GsvNmeaObject.class);
    }
}

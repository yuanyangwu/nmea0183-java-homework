package com.frankwu.nmea;

import java.util.List;
import java.util.Observable;

/**
 * Created by wuf2 on 2/13/2015.
 */
public abstract class AbstractNmeaCodec extends Observable {
    public abstract void decode(String content);
    public abstract List<String> encode(AbstractNmeaObject obj);
}

package com.frankwu.nmea;

/**
 * Created by wuf2 on 3/21/2015.
 */
public abstract class PostFilter {
    public abstract boolean decode(AbstractNmeaObject object);
}

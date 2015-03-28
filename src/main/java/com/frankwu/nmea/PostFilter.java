package com.frankwu.nmea;

/**
 * Created by wuf2 on 3/21/2015.
 */
public interface PostFilter {
    public boolean decode(AbstractNmeaObject object);
}

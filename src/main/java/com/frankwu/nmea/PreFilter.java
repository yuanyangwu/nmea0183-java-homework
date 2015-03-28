package com.frankwu.nmea;

/**
 * Created by wuf2 on 3/27/2015.
 */
public interface PreFilter {
    public boolean encode(AbstractNmeaObject object);
}

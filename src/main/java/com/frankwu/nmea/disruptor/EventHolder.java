package com.frankwu.nmea.disruptor;

import com.frankwu.nmea.AbstractNmeaObject;
import com.lmax.disruptor.EventFactory;

/**
 * Created by wuf2 on 6/16/2015.
 */
public class EventHolder {
    public static final EventFactory<EventHolder> factory = new EventFactory<EventHolder>() {
        public EventHolder newInstance() {
            return new EventHolder();
        }
    };

    public AbstractNmeaObject event;
}

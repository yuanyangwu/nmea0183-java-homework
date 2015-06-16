package com.frankwu.nmea.disruptor;

import com.frankwu.nmea.AbstractNmeaObject;
import com.lmax.disruptor.EventHandler;

/**
 * Created by wuf2 on 6/16/2015.
 */
public class EventHolderHandler implements EventHandler<EventHolder> {
    private final EventHandler<AbstractNmeaObject> delegator;

    public EventHolderHandler(EventHandler<AbstractNmeaObject> delegator) {
        this.delegator = delegator;
    }

    @Override
    public void onEvent(EventHolder event, long sequence, boolean endOfBatch) throws Exception {
        delegator.onEvent(event.event, sequence, endOfBatch);
        event.event = null;
    }
}

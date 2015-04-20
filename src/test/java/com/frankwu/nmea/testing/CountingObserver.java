package com.frankwu.nmea.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wuf2 on 4/4/2015.
 */
public class CountingObserver implements Observer {
    private final Logger logger = LoggerFactory.getLogger(CountingObserver.class);
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void update(Observable o, Object arg) {
        count.getAndIncrement();
        logger.debug("Observe: " + arg);
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public int getCount() {
        return count.get();
    }
}

package com.frankwu.nmea.testing;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wuf2 on 4/4/2015.
 */
public class CountingObserver implements Observer {
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void update(Observable o, Object arg) {
        count.getAndIncrement();
    }

    public int getCount() {
        return count.get();
    }
}

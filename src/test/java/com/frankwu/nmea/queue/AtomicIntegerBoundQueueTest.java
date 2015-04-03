package com.frankwu.nmea.queue;

import org.junit.Before;

/**
 * Created by wuf2 on 4/3/2015.
 */
public class AtomicIntegerBoundQueueTest extends BoundQueueTest {
    @Before
    public void setup() {
        setQueue(new AtomicIntegerBoundQueue<String>(100));
        setSize(100);
    }
}

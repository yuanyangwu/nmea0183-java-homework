package com.frankwu.nmea.queue;

import org.junit.Test;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by wuf2 on 4/3/2015.
 */
public abstract class BoundQueueTest {
    protected AbstractBoundQueue<String> queue;
    protected int size;

    public void setQueue(AbstractBoundQueue<String> queue) {
        this.queue = queue;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Test
    public void serialTest() throws InterruptedException {
        for (int i = 0; i < size; i++) {
            queue.put(Integer.toString(i));
            assertEquals(Integer.toString(i), queue.take());
        }

        for (int i = 0; i < size; i++) {
            queue.put(Integer.toString(i));
        }

        for (int i = 0; i < size; i++) {
            assertEquals(Integer.toString(i), queue.take());
        }

    }

    @Test
    public void parallelTestPutFirst() throws InterruptedException {
        AtomicInteger totalPut = new AtomicInteger();
        AtomicInteger totalTake = new AtomicInteger();
        final ExecutorService executor = Executors.newCachedThreadPool();
        ConcurrentHashMap<String, Boolean> map = new ConcurrentHashMap<>();
        CountDownLatch stopSignal = new CountDownLatch(size * 2);

        // put threads
        for (int i = 0; i < size; i++) {
            map.put(Integer.toString(i), false);
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        queue.put(Integer.toString(totalPut.getAndIncrement()));
                    } catch (InterruptedException e) {
                    }
                    stopSignal.countDown();
                }
            });
        }

        // take threads
        for (int i = 0; i < size; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        map.put(queue.take(), true);
                    } catch (InterruptedException e) {
                    }
                    totalTake.getAndIncrement();
                    stopSignal.countDown();
                }
            });
        }

        stopSignal.await();
        assertEquals(size, totalTake.get());
        for (Map.Entry<String, Boolean> e : map.entrySet()) {
            if (!e.getValue()) {
                assertTrue(e.getKey().toString() + " is not found", false);
            }
        }
        executor.shutdownNow();
    }

    @Test
    public void parallelTestTakeFirst() throws InterruptedException {
        AtomicInteger totalPut = new AtomicInteger();
        AtomicInteger totalTake = new AtomicInteger();
        final ExecutorService executor = Executors.newFixedThreadPool(size * 3);
        ConcurrentHashMap<String, Boolean> map = new ConcurrentHashMap<>();
        CountDownLatch stopSignal = new CountDownLatch(size * 2);

        for (int i = 0; i < size; i++) {
            map.put(Integer.toString(i), false);
        }

        // take threads
        for (int i = 0; i < size; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(new Random().nextInt(100));
                        map.put(queue.take(), true);
                    } catch (InterruptedException e) {
                    }
                    totalTake.getAndIncrement();
                    stopSignal.countDown();
                }
            });
        }

        // put threads
        for (int i = 0; i < size; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(new Random().nextInt(100));
                        queue.put(Integer.toString(totalPut.getAndIncrement()));
                    } catch (InterruptedException e) {
                    }
                    stopSignal.countDown();
                }
            });
        }

        stopSignal.await();
        assertEquals(size, totalPut.get());
        assertEquals(size, totalTake.get());
        for (Map.Entry<String, Boolean> e : map.entrySet()) {
            assertTrue(e.getKey().toString() + " is not found", e.getValue());
        }
        executor.shutdownNow();
    }
}

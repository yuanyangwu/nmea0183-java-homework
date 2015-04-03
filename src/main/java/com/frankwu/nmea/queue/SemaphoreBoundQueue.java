package com.frankwu.nmea.queue;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SemaphoreBoundQueue<T> implements AbstractBoundQueue<T> {
    private final T[] items;
    private Semaphore available;
    private Semaphore free;
    private AtomicInteger head = new AtomicInteger(0);
    private AtomicInteger tail = new AtomicInteger(0);


    public SemaphoreBoundQueue(int capacity) {
        items = (T[])new Object[capacity];
        available = new Semaphore(0);
        free = new Semaphore(capacity);
    }

    @Override
    public void put(T item) throws InterruptedException {
        free.acquire();
        int index = tail.getAndUpdate(i -> { return (i + 1) % items.length;});
        items[index] = item;
        available.release();
    }

    @Override
    public T take() throws InterruptedException {
        available.acquire();
        int index = head.getAndUpdate(i -> { return (i + 1) % items.length;});
        Object object = items[index];
        free.release();
        return (T)object;
    }
}

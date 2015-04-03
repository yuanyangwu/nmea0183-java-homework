package com.frankwu.nmea.queue;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wuf2 on 4/3/2015.
 */
public class AtomicIntegerBoundQueue<T> implements AbstractBoundQueue<T> {
    private final T[] items;
    private AtomicInteger available = new AtomicInteger(0);
    private AtomicInteger free;
    private AtomicInteger head = new AtomicInteger(0);
    private AtomicInteger tail = new AtomicInteger(0);

    public AtomicIntegerBoundQueue(int capacity) {
        items = (T[]) new Object[capacity];
        free = new AtomicInteger(capacity);
    }

    @Override
    public void put(T item) throws InterruptedException {
        while (0 == free.getAndUpdate(i -> {
            return (i == 0) ? 0 : (i - 1);
        })) ;

        int index = head.getAndUpdate(i -> {
            return (i + 1) % items.length;
        });

        items[index] = item;
        available.getAndIncrement();
    }

    @Override
    public T take() throws InterruptedException {
        while (0 == available.getAndUpdate(i -> {
            return (i == 0) ? 0 : (i - 1);
        })) ;

        int index = tail.getAndUpdate(i -> {
            return (i + 1) % items.length;
        });

        T object = (T)items[index];

        free.getAndIncrement();
        return object;
    }
}

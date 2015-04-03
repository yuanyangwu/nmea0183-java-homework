package com.frankwu.nmea.queue;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by wuf2 on 4/3/2015.
 */
public class AtomicReferenceArrayBoundQueue<T> implements AbstractBoundQueue<T> {

    private final int capacity;
    private final AtomicReferenceArray items;

    private volatile int head = 0;
    private volatile int tail = 0;

    public AtomicReferenceArrayBoundQueue(int capacity) {
        this.capacity = capacity + 1;
        items = new AtomicReferenceArray(this.capacity);
    }

    @Override
    public void put(T item) {
        while (true) {
            int oldTailIndex = tail;
            int newTailIndex = (oldTailIndex + 1) % capacity;
            if (newTailIndex == head) {
                // queue is full
                continue;
            }

            if (items.compareAndSet(oldTailIndex, null, item)) {
                tail = newTailIndex;
                return;
            }
        }
    }

    @Override
    public T take() {
        while (true) {
            int oldHeadIndex = head;
            int newHeadIndex = (oldHeadIndex + 1) % capacity;
            if (oldHeadIndex == tail) {
                // empty
                continue;
            }
            Object object = items.get(oldHeadIndex);
            if (object == null) {
                // empty
                continue;
            }
            if (items.compareAndSet(oldHeadIndex, object, null)) {
                head = newHeadIndex;
                return (T)object;
            }
        }
    }
}

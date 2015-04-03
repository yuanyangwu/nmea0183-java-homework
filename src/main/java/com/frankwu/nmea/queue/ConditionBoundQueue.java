package com.frankwu.nmea.queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wuf2 on 4/3/2015.
 */
public class ConditionBoundQueue<T> implements AbstractBoundQueue<T> {
    private final T[] items;
    private final Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private int head = 0;
    private int tail = 0;
    private int count = 0;

    public ConditionBoundQueue(int capacity) {
        items = (T[]) new Object[capacity];
    }

    @Override
    public void put(T t) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();
            }
            items[tail] = t;
            tail = (tail + 1) % items.length;
            count++;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            T ret = items[head];
            items[head] = null;

            head = (head + 1) % items.length;
            count--;
            notFull.signalAll();
            return ret;
        } finally {
            lock.unlock();
        }
    }
}

package com.frankwu.nmea.queue;

public interface AbstractBoundQueue<T> {
    public void put(T item) throws InterruptedException;
    public T take() throws InterruptedException;
}

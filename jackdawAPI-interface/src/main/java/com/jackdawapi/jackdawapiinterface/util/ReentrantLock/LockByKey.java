package com.jackdawapi.jackdawapiinterface.util.ReentrantLock;

public interface LockByKey<T> {

    /**
     * 加锁
     */
    void lock(T key);

    /**
     * 解锁
     */
    void unlock(T key);
}
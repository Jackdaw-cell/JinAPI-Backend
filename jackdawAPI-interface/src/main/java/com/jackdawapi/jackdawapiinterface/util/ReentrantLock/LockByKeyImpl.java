package com.jackdawapi.jackdawapiinterface.util.ReentrantLock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LockByKeyImpl<T> implements LockByKey<T> {

    /**
     * 锁维护一个Map集合，里面按不同的key存放ReentrantLock,实现每一个key【即用户唯一标识】有一个锁，而这个用户只能同时进行一个同样的操作
     * 因为是线程锁，所以锁集合存储在本地jvm
     */
    private final Map<T, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    /**
     * 加锁
     */
    @Override
    public void lock(T key) {
        // 如果key为空，直接返回
        if (key == null) {
            throw new IllegalArgumentException("key 不能为空");
        }
        
        // 获取或创建一个ReentrantLock对象
        ReentrantLock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
        // 获取锁
        lock.lock();
    }


    /**
     * 解锁
     */
    @Override
    public void unlock(T key) {
        // 如果key为空，直接返回
        if (key == null) {
            throw new IllegalArgumentException("key 不能为空");
        }

        // 从Map中获取锁对象
        ReentrantLock lock = lockMap.get(key);
        // 获取不到报错
        if (lock == null) {
            throw new IllegalArgumentException("key " + key + "尚未加锁");
        }
        // 其他线程非法持有不允许释放
        if (!lock.isHeldByCurrentThread()) {
            throw new IllegalStateException("当前线程尚未持有，key:" + key + "的锁，不允许释放");
        }
        lock.unlock();
    }
}
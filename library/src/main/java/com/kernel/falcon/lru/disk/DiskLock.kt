package com.kernel.falcon.lru.disk

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class DiskLock<K> {

    private val editionLocks = ConcurrentHashMap<K, Lock>()
    private val invalidationReadWriteLock = ReentrantReadWriteLock()

    fun lockDiskEntryWrite(key: K) {
        invalidationReadWriteLock.readLock().lock()
        getLockForGivenDiskEntry(key)?.lock()
    }

    fun unLockDiskEntryWrite(key: K) {
        getLockForGivenDiskEntry(key)?.unlock()
        invalidationReadWriteLock.readLock().unlock()
    }

    fun lockFullDiskWrite() {
        invalidationReadWriteLock.writeLock().lock()
    }

    fun unLockFullDiskWrite() {
        invalidationReadWriteLock.writeLock().unlock()
    }

    private fun getLockForGivenDiskEntry(key: K): Lock? {
        if (!editionLocks.containsKey(key)) {
            editionLocks.putIfAbsent(key, ReentrantLock())
        }
        return editionLocks[key]
    }

}
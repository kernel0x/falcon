package com.kernel.falcon.lru

interface LruCache<K, V> {
    fun put(key: K?, value: V?)
    fun size(): Long
    fun remove(key: K?)
    fun clear()
    fun synchronizedMap(): Map<K, V>
}

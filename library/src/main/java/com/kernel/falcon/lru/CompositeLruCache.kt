package com.kernel.falcon.lru

import android.content.Context
import com.kernel.falcon.encrypt.EncryptStrategy
import com.kernel.falcon.lru.disk.DiskLruCache
import com.kernel.falcon.lru.ram.RamLruCache
import com.kernel.falcon.models.Size

class CompositeLruCache<K, V>(context: Context, maxSize: Size, encryptStrategy: EncryptStrategy) : LruCache<K, V> {

    private var diskCache: DiskLruCache<K, V> =
        DiskLruCache(context, maxSize.valueAsByte, encryptStrategy)
    private var ramCache: RamLruCache<K, V> =
        RamLruCache(maxSize.valueAsByte)

    fun putRam(key: K?, value: V?) {
        ramCache.put(key, value)
    }

    fun putDisk(key: K?, value: V?) {
        diskCache.put(key, value)
    }

    override fun put(key: K?, value: V?) {
        putRam(key, value)
        putDisk(key, value)
    }

    override fun size(): Long {
        return ramCache.size() + diskCache.size()
    }

    override fun remove(key: K?) {
        ramCache.remove(key)
        diskCache.remove(key)
    }

    override fun clear() {
        ramCache.clear()
        diskCache.clear()
    }

    override fun synchronizedMap(): MutableMap<K, V> {
        val result = HashMap<K, V>()
        result.putAll(ramCache.synchronizedMap())
        result.putAll(diskCache.synchronizedMap())
        return result
    }

    operator fun get(key: K): V? {
        return ramCache[key] ?: diskCache[key]
    }

}
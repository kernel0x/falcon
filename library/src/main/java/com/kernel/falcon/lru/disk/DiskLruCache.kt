package com.kernel.falcon.lru.disk

import android.content.Context
import com.kernel.falcon.BuildConfig
import com.kernel.falcon.encrypt.EncryptStrategy
import com.kernel.falcon.lru.LruCache
import com.kernel.falcon.utils.MappingUtil
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

class DiskLruCache<K, V>(private val context: Context, private val maxSize: Int, private val encryptStrategy: EncryptStrategy) : LruCache<K, V> {

    private var diskLruCache: Disk? = null
    private val lock = DiskLock<K>()

    init {
        try {
            openDiskLruCache()
        } catch (e: IOException) {
            // ignore
        }
    }

    @Throws(IOException::class)
    private fun openDiskLruCache() {
        this.diskLruCache = Disk.open(
            getDefaultDiskCacheFolder(),
            BuildConfig.VERSION_CODE,
            1,
            maxSize.toLong()
        )
    }

    private fun getDefaultDiskCacheFolder(): File {
        return File(
            context.cacheDir.absolutePath
                    + "/" + BuildConfig.LIBRARY_PACKAGE_NAME
                    + "/" + BuildConfig.VERSION_NAME
        )
    }

    operator fun get(key: K): V? {
        val snapshotObject: Disk.Snapshot?
        try {
            snapshotObject = diskLruCache?.get(key.toString())
            if (snapshotObject != null) {
                return MappingUtil.decode(encryptStrategy.decrypt(snapshotObject.getString(0))) as V?
            }
        } catch (e: IOException) {
            return null
        }
        return null
    }

    override fun put(key: K?, value: V?) {
        if (key == null || value == null) {
            throw NullPointerException("key == null || value == null")
        }

        try {
            lock.lockDiskEntryWrite(key)
            val editor = diskLruCache?.edit(key.toString())
            editor?.apply {
                editor.set(0, encryptStrategy.encrypt(MappingUtil.encode(value)))
                editor.commit()
            }
        } catch (e: IOException) {
            // ignore
        } finally {
            lock.unLockDiskEntryWrite(key)
        }
    }

    override fun size(): Long {
        return diskLruCache?.size() ?: 0
    }

    override fun remove(key: K?) {
        try {
            diskLruCache?.remove(key.toString())
        } catch (e: IOException) {
            // ignore
        }
    }

    override fun clear() {
        try {
            diskLruCache?.delete()
            openDiskLruCache()
        } catch (e: IOException) {
            // ignore
        }
    }

    @Synchronized
    override fun synchronizedMap(): Map<K, V> {
        val map = ConcurrentHashMap<K, V>()
        if (diskLruCache == null) return map
        for (key in diskLruCache!!.synchronizedMap().keys) {
            get(key as K)?.let { map.put(key as K, it) }
        }
        return map
    }

}
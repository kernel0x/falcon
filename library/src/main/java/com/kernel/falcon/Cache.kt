package com.kernel.falcon

import android.content.Context
import com.kernel.falcon.encrypt.EncryptStrategy
import com.kernel.falcon.encrypt.NoEncryptStrategy
import com.kernel.falcon.lru.CompositeLruCache
import com.kernel.falcon.models.CacheObject
import com.kernel.falcon.models.DualCacheMode
import com.kernel.falcon.models.Duration
import com.kernel.falcon.models.Size
import com.kernel.falcon.utils.DateProvider
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class Cache<T>(
    context: Context,
    maxSize: Size,
    encryptStrategy: EncryptStrategy,
    val caseSensitiveKeys: Boolean,
    val timeAutoCleanup: Long,
    val defaultLifetime: Long,
    val dualCacheMode: DualCacheMode) {

    private var lruCache: CompositeLruCache<String, CacheObject<T>> =
        CompositeLruCache(context, maxSize, encryptStrategy)

    private var autoCleanupService: ScheduledExecutorService =
        Executors.newSingleThreadScheduledExecutor()

    var dateProvider: DateProvider = DateProvider.SYSTEM

    init {
        startAutoCleanupServiceIfNeeded()
    }

    fun shutdown() {
        clear()
        autoCleanupService.shutdown()
    }

    private fun startAutoCleanupServiceIfNeeded() {
        if (timeAutoCleanup > 0) {
            autoCleanupService.scheduleAtFixedRate(
                { cleanup() },
                timeAutoCleanup,
                timeAutoCleanup,
                TimeUnit.MILLISECONDS
            )
        }
    }

    operator fun set(key: String, value: T) {
        set(key, value, defaultLifetime, dualCacheMode)
    }

    operator fun set(key: String, value: T, dualCacheMode: DualCacheMode) {
        set(key, value, defaultLifetime, dualCacheMode)
    }

    operator fun set(key: String, value: T, lifetime: Duration) {
        set(key, value, lifetime.valueAsMs, dualCacheMode)
    }

    operator fun set(key: String, value: T, lifetime: Duration, dualCacheMode: DualCacheMode) {
        set(key, value, lifetime.valueAsMs, dualCacheMode)
    }

    operator fun set(key: String, value: T, lifetime: Long) {
        set(key, value, lifetime, dualCacheMode)
    }

    operator fun set(key: String, value: T, lifetime: Long, dualCacheMode: DualCacheMode) {
        if (lifetime >= 0) {
            val cleanKey = getCleanKey(key)
            when (dualCacheMode) {
                DualCacheMode.AUTO -> {
                    lruCache.put(cleanKey, CacheObject(value, now(), lifetime))
                }
                DualCacheMode.ONLY_RAM -> {
                    lruCache.putRam(cleanKey, CacheObject(value, now(), lifetime))
                }
                DualCacheMode.ONLY_DISK -> {
                    lruCache.putDisk(cleanKey, CacheObject(value, now(), lifetime))
                }
            }
        }
    }

    fun get(key: String, defaultValue: T): T = get(key) ?: defaultValue

    operator fun get(key: String) = get(key, cleanupIfDead = false)

    fun getAndCleanupIfDead(key: String) = get(key, cleanupIfDead = true)

    private fun get(key: String, cleanupIfDead: Boolean): T? {
        val cleanKey = getCleanKey(key)
        val retrievedValue = lruCache[cleanKey]
        return when {
            retrievedValue == null -> null
            retrievedValue.isAlive(now()) -> retrievedValue.value
            else -> {
                if (cleanupIfDead) {
                    lruCache.remove(cleanKey)
                }
                null
            }
        }
    }

    fun getTimeCreation(key: String): Long {
        val cleanKey = getCleanKey(key)
        val retrievedValue = lruCache[cleanKey]
        return when {
            retrievedValue == null -> -1
            retrievedValue.isAlive(now()) -> retrievedValue.timeCreation
            else -> {
                -1
            }
        }
    }

    fun remove(key: String) {
        val cleanKey = getCleanKey(key)
        lruCache.remove(cleanKey)
    }

    fun isEmpty() = sizeAliveElements() == 0

    fun clear() = lruCache.clear()

    fun keySet() = keySetAlive()

    fun keySetDeadAndAlive(): List<String> = lruCache.synchronizedMap().keys.toList()

    fun keySetAlive(): List<String> {
        return keySetDeadAndAlive().filter {
            isKeyAlive(it)
        }
    }

    fun keySetDead(): List<String> {
        return keySetDeadAndAlive().filter {
            isKeyDead(it)
        }
    }

    fun keySetStartingWith(start: String?): List<String> {
        if (start == null) return Collections.emptyList()
        val cleanKeyStartingWith = getCleanKey(start)
        return keySetDeadAndAlive().filter {
            it.startsWith(cleanKeyStartingWith)
        }
    }

    fun keySetAliveStartingWith(start: String?): List<String> {
        if (start == null) return Collections.emptyList()

        return keySetStartingWith(start).filter {
            isKeyAlive(it)
        }
    }

    fun isKeyAlive(key: String): Boolean {
        val value = lruCache[key] ?: return false
        return value.isAlive(now())
    }

    fun isKeyDead(key: String) = !isKeyAlive(key)

    fun size() = sizeAliveElements()

    fun sizeAliveElements(): Int {
        return lruCache.synchronizedMap().values.filter { it.isAlive(now()) }.count()
    }

    fun sizeDeadElements() = lruCache.synchronizedMap().size - sizeAliveElements()

    fun sizeDeadAndAliveElements() = lruCache.synchronizedMap().size

    operator fun contains(key: String) = get(getCleanKey(key)) != null

    fun cleanup() {
        val map = lruCache.synchronizedMap()
        for (entry in map) {
            if (isKeyDead(entry.key)) {
                remove(entry.key)
            }
        }
    }

    private fun getCleanKey(key: String): String {
        return when {
            caseSensitiveKeys -> key
            else -> key.toLowerCase(Locale.getDefault())
        }
    }

    private fun now(): Long {
        return dateProvider.now()
    }

    data class Builder(
        private var maxSize: Size = Size(16, Size.Unit.MB),
        private var caseSensitiveKeys: Boolean = true,
        private var timeAutoCleanup: Long = Config.WITHOUT_AUTO_CLEANUP,
        private var defaultLifetime: Long = Config.LIFETIME_FOREVER,
        private var dualCacheMode: DualCacheMode = DualCacheMode.AUTO,
        private var encryptStrategy: EncryptStrategy = NoEncryptStrategy()
    ) {
        fun maxSize(maxSize: Size) = apply { this.maxSize = maxSize }
        fun caseSensitiveKeys(caseSensitiveKeys: Boolean) = apply { this.caseSensitiveKeys = caseSensitiveKeys }
        fun autoCleanup(duration: Duration) = apply { this.timeAutoCleanup = duration.valueAsMs }
        fun defaultLifetime(duration: Duration) = apply { this.defaultLifetime = duration.valueAsMs }
        fun dualCacheMode(dualCacheMode: DualCacheMode) = apply { this.dualCacheMode = dualCacheMode }
        fun encryptStrategy(encryptStrategy: EncryptStrategy) = apply { this.encryptStrategy = encryptStrategy }

        fun <T> build(context: Context): Cache<T> = Cache(
            context, maxSize, encryptStrategy, caseSensitiveKeys,
            timeAutoCleanup, defaultLifetime, dualCacheMode
        )
    }

}
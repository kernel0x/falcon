package com.kernel.falcon

import com.kernel.falcon.models.DualCacheMode
import com.kernel.falcon.models.Duration

import org.junit.Test

import java.util.concurrent.TimeUnit

import com.kernel.falcon.Config.LIFETIME_FOREVER
import com.kernel.falcon.Config.WITHOUT_AUTO_CLEANUP
import org.junit.Assert.*

class BuilderTest: BaseTest(){

    @Test
    fun testDefaultBuilder() {
        val cache = Cache.Builder().build<Any>(context)

        assertTrue(cache.caseSensitiveKeys)
        assertEquals(WITHOUT_AUTO_CLEANUP, cache.timeAutoCleanup)
        assertEquals(LIFETIME_FOREVER, cache.defaultLifetime)
        assertEquals(DualCacheMode.AUTO, cache.dualCacheMode)
    }

    @Test
    fun testSettingTrueCaseSensitiveKeysBuilder() {
        val cache = Cache.Builder().caseSensitiveKeys(true).build<Any>(context)

        assertTrue(cache.caseSensitiveKeys)
    }

    @Test
    fun testSettingFalseCaseSensitiveKeysBuilder() {
        val cache = Cache.Builder().caseSensitiveKeys(false).build<Any>(context)

        assertFalse(cache.caseSensitiveKeys)
    }

    @Test
    fun testSettingZeroAutoCleanupSecondsBuilder() {
        val cache = Cache.Builder().autoCleanup(Duration.ZERO).build<Any>(context)

        assertEquals(WITHOUT_AUTO_CLEANUP, cache.timeAutoCleanup)
    }

    @Test
    fun testSetting10AutoCleanupSecondsBuilder() {
        val cache = Cache.Builder().autoCleanup(Duration(10, TimeUnit.MILLISECONDS)).build<Any>(context)

        assertEquals(10, cache.timeAutoCleanup)
    }

    @Test
    fun testSetting10AutoCleanupSecondsBuilder_usingSecondsTimeUnit() {
        val cache = Cache.Builder().autoCleanup(Duration(10, TimeUnit.SECONDS)).build<Any>(context)

        assertEquals(10000, cache.timeAutoCleanup)
    }

    @Test
    fun testSetting10AutoCleanupSecondsBuilder_usingMillisTimeUnit() {
        val cache = Cache.Builder().autoCleanup(Duration(10000, TimeUnit.MILLISECONDS)).build<Any>(context)

        assertEquals(10000, cache.timeAutoCleanup)
    }

    @Test
    fun testSetting10AutoCleanupSecondsBuilder_usingMicrosTimeUnit() {
        val cache = Cache.Builder().autoCleanup(Duration(10000000, TimeUnit.MICROSECONDS)).build<Any>(context)

        assertEquals(10000, cache.timeAutoCleanup)
    }

    @Test
    fun testSettingDefaultLifetimeBuilder_with10() {
        val cache = Cache.Builder().defaultLifetime(Duration(10, TimeUnit.MILLISECONDS)).build<Any>(context)

        assertEquals(10, cache.defaultLifetime)
    }

    @Test
    fun testSettingDefaultLifetimeBuilder_with10000MillisTimeUnits() {
        val cache = Cache.Builder().defaultLifetime(Duration(10000, TimeUnit.MILLISECONDS)).build<Any>(context)

        assertEquals(10000, cache.defaultLifetime)
    }

    @Test
    fun testSettingDefaultLifetimeBuilder_with10SecondsTimeUnits() {
        val cache = Cache.Builder().defaultLifetime(Duration(10, TimeUnit.SECONDS)).build<Any>(context)

        assertEquals(10000, cache.defaultLifetime)
    }

    @Test
    fun testSettingDefaultLifetimeBuilder_withZero() {
        val cache = Cache.Builder().defaultLifetime(Duration(0, TimeUnit.DAYS)).build<Any>(context)

        assertEquals(LIFETIME_FOREVER, cache.defaultLifetime)
    }

    @Test
    fun testSettingDefaultLifetimeBuilder_withMinusTen() {
        try {
            val cache = Cache.Builder().defaultLifetime(Duration(-10, TimeUnit.MILLISECONDS)).build<Any>(context)
            assertEquals(Integer.MAX_VALUE.toLong(), cache.defaultLifetime)
        } catch (e: IllegalArgumentException) {
            // its ok
            return
        }
        assert(false)
    }

    @Test
    fun testCacheDefaultLifetimeIsForever() {
        val cache = Cache.Builder().defaultLifetime(Duration(LIFETIME_FOREVER, TimeUnit.MILLISECONDS)).build<Any>(context)

        assertEquals(LIFETIME_FOREVER, cache.defaultLifetime)
    }

    @Test
    fun testCacheDualCacheModeEquals() {
        val cache = Cache.Builder().dualCacheMode(DualCacheMode.ONLY_DISK).build<Any>(context)

        assertEquals(DualCacheMode.ONLY_DISK, cache.dualCacheMode)
    }

    @Test
    fun testCacheDualCacheModeNotSame() {
        val cache = Cache.Builder().dualCacheMode(DualCacheMode.ONLY_RAM).build<Any>(context)

        assertNotSame(DualCacheMode.AUTO, cache.dualCacheMode)
    }

}

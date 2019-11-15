package com.kernel.falcon

import com.kernel.falcon.mock.MockDateProvider
import com.kernel.falcon.models.Duration

import org.junit.Test

import java.util.concurrent.TimeUnit

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull

class DefaultLifetimeTest : BaseTest() {

    @Test
    fun shouldHaveInfiniteLifetimeByDefault() {
        val cache = Cache.Builder().build<String>(context)
        val dateProvider = MockDateProvider()
        cache.dateProvider = dateProvider

        cache[A_KEY] = A_VALUE

        dateProvider.setFixed(threeDaysFromNow())

        assertNotNull(cache[A_KEY])
    }

    @Test
    fun testOneSecondLifetimeRightNow() {
        val cache = Cache.Builder().defaultLifetime(Duration.ONE_SECOND).build<String>(context)
        val dateProvider = MockDateProvider()
        cache.dateProvider = dateProvider

        cache[A_KEY] = A_VALUE

        assertNotNull(cache[A_KEY])
    }

    @Test
    fun testOneSecondLifetimeAfterOneSecond() {
        val cache = Cache.Builder().defaultLifetime(Duration.ONE_SECOND).build<String>(context)
        val dateProvider = MockDateProvider()
        cache.dateProvider = dateProvider

        cache[A_KEY] = A_VALUE

        dateProvider.setFixed(oneSecondFromNow())

        assertNull(cache[A_KEY])
    }

    @Test
    fun testOneSecondLifetimeAfterTwoHours() {
        val cache = Cache.Builder().defaultLifetime(Duration.ONE_SECOND).build<String>(context)
        val dateProvider = MockDateProvider()
        cache.dateProvider = dateProvider

        cache[A_KEY] = A_VALUE

        dateProvider.setFixed(twoHoursFromNow())

        assertNull(cache[A_KEY])
    }

    @Test
    fun testTwoSecondsLifetimeAfterOneSecond() {
        val cache = Cache.Builder().defaultLifetime(Duration(2, TimeUnit.SECONDS)).build<String>(context)
        val dateProvider = MockDateProvider()
        cache.dateProvider = dateProvider

        cache[A_KEY] = A_VALUE

        dateProvider.setFixed(oneSecondFromNow())

        assertNotNull(cache[A_KEY])
    }

}

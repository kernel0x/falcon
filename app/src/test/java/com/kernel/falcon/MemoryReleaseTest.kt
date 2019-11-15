package com.kernel.falcon

import com.kernel.falcon.models.Duration

import org.junit.Before
import org.junit.Test

import java.util.concurrent.TimeUnit

import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals

class MemoryCleanupTest : BaseTest() {

    @Before
    fun init() {
        val cache = Cache.Builder().autoCleanup(Duration(1, TimeUnit.SECONDS)).build<String>(context)
        cache.clear()
    }

    @Test
    @Throws(InterruptedException::class)
    fun manualCleanupMemoryWorks() {
        val cache = Cache.Builder().build<String>(context)
        cache.clear()

        //adding 3 values that will be alive for 1 second, 2 seconds, 3 seconds.
        cache["1", A_VALUE] = 1000
        cache["2", A_VALUE] = 2000
        cache["3", A_VALUE] = 3000

        //checking that forgettingOldValues work
        cache.cleanup()
        assertEquals(3, cache.sizeDeadAndAliveElements().toLong())

        Thread.sleep(1000)
        cache.cleanup()
        assertEquals(2, cache.sizeDeadAndAliveElements().toLong())

        Thread.sleep(1000)
        cache.cleanup()
        assertEquals(1, cache.sizeDeadAndAliveElements().toLong())

        Thread.sleep(1000)
        cache.cleanup()
        assertEquals(0, cache.sizeDeadAndAliveElements().toLong())
    }

    @Test
    @Throws(InterruptedException::class)
    fun autoCleanupMemoryWorks() {
        val cache = Cache.Builder().autoCleanup(Duration(1, TimeUnit.SECONDS)).build<String>(context)
        cache.clear()

        //adding 3 values that will be alive for 1 second, 2 seconds, 3 seconds.
        cache["1", A_VALUE] = 1000
        cache["2", A_VALUE] = 2000
        cache["3", A_VALUE] = 3000

        //checking that forgettingOldValues work
        assertEquals(3, cache.sizeDeadAndAliveElements().toLong())
        Thread.sleep(5000)
        assertEquals(0, cache.sizeDeadAndAliveElements().toLong())
    }

    @Test
    fun autoCleanupCreatesAThread() {
        val threadsBefore = Thread.activeCount()

        Cache.Builder().autoCleanup(Duration(1, TimeUnit.SECONDS)).build<Any>(context)

        val threadsAfter = Thread.activeCount()

        assertTrue(threadsBefore < threadsAfter)
    }

}

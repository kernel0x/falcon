package com.kernel.falcon

import com.kernel.falcon.models.Duration

import org.junit.Test

import java.util.concurrent.TimeUnit

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue

class ShutdownTest : BaseTest() {

    @Test
    fun shutdownClearsTheCache() {
        val cache = Cache.Builder().autoCleanup(Duration(1, TimeUnit.SECONDS)).build<Any>(context)
        cache.shutdown()
        assertEquals(0, cache.size())
    }

    @Test
    @Throws(InterruptedException::class)
    fun shutdownFinishesTheAutoCleanupThread() {
        val cache = Cache.Builder().autoCleanup(Duration(1, TimeUnit.SECONDS)).build<Any>(context)

        val threadsBeforeShutdown = Thread.activeCount()

        cache.shutdown()
        waitUntilThreadDies()

        val threadsAfterShutdown = Thread.activeCount()

        assertTrue(threadsBeforeShutdown > threadsAfterShutdown)
    }

    @Test
    fun shutdownDoesntCrashIfAutoCleanupIsOff() {
        val cache = Cache.Builder().build<Any>(context)
        cache.shutdown()
    }

    @Throws(InterruptedException::class)
    private fun waitUntilThreadDies() {
        Thread.sleep(1000)
    }

}

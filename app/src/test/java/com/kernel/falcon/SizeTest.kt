package com.kernel.falcon

import com.kernel.falcon.mock.MockDateProvider

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class SizeTest : BaseTest() {

    lateinit var cache: Cache<String>
    lateinit var dateProvider: MockDateProvider

    @Before
    fun init() {
        cache = Cache.Builder().build(context)
        cache.clear()
        dateProvider = MockDateProvider()
        cache.dateProvider = dateProvider
    }

    @Test
    fun sizeWorksForZeroElements() {
        assertTrue(cache.isEmpty())
        assertEquals(0, cache.size().toLong())
    }

    @Test
    fun sizeWorksForOneElement() {
        cache[A_KEY, A_VALUE] = ONE_SECOND

        assertFalse(cache.isEmpty())
        assertEquals(1, cache.size().toLong())
    }

    @Test
    fun sizeWorksForTwoElements() {
        cache[A_KEY, A_VALUE] = ONE_SECOND
        cache[B_KEY, B_VALUE] = THREE_DAYS

        assertFalse(cache.isEmpty())
        assertEquals(2, cache.size().toLong())
    }

    @Test
    fun sizeWorksAfterRemovingAllTheElements() {
        cache[A_KEY, A_VALUE] = ONE_SECOND
        cache[B_KEY, B_VALUE] = THREE_DAYS

        cache.clear()

        assertNull(cache[A_KEY])
        assertNull(cache[B_KEY])

        assertEquals(0, cache.size().toLong())
        assertEquals(0, cache.sizeAliveElements().toLong())
        assertEquals(0, cache.sizeDeadElements().toLong())
        assertEquals(0, cache.sizeDeadAndAliveElements().toLong())
        assertTrue(cache.isEmpty())
    }

    @Test
    fun sizeWorksAfterRemovingOldElements() {
        cache[A_KEY, A_VALUE] = ONE_SECOND
        cache[B_KEY, B_VALUE] = THREE_DAYS
        dateProvider.setFixed(twoHoursFromNow())

        cache.cleanup()

        assertNull(cache[A_KEY])
        assertEquals(B_VALUE, cache[B_KEY])

        assertEquals(1, cache.size().toLong())
        assertEquals(1, cache.sizeAliveElements().toLong())
        assertEquals(0, cache.sizeDeadElements().toLong())
        assertEquals(1, cache.sizeDeadAndAliveElements().toLong())
        assertFalse(cache.isEmpty())
    }

}

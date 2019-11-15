package com.kernel.falcon

import com.kernel.falcon.mock.MockDateProvider
import org.junit.Before
import org.junit.Test

import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

class KeySetTest : BaseTest() {

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
    fun keySetIsEmptyWhenCacheHasBeenJustCreated() {
        assertEquals(0, cache.keySetDeadAndAlive().size.toLong())
    }

    @Test
    fun keySetReturnsTheSameNumberAsAddedItems() {
        cache[A_KEY] = A_VALUE
        cache[B_KEY, B_VALUE] = THREE_DAYS

        assertEquals(2, cache.keySetDeadAndAlive().size.toLong())
    }

    @Test
    fun keySetReturnsOnlyAliveKeys() {
        cache["a"] = A_VALUE
        cache["b"] = A_VALUE
        cache["c", A_VALUE] = ONE_SECOND

        dateProvider.setFixed(threeDaysFromNow())

        assertEquals(2, cache.keySet().size.toLong())
    }

    @Test
    fun keySetAliveReturnsOnlyAliveKeys() {
        cache["a"] = A_VALUE
        cache["b"] = A_VALUE
        cache["c", A_VALUE] = ONE_SECOND

        dateProvider.setFixed(threeDaysFromNow())

        assertEquals(2, cache.keySetAlive().size.toLong())
    }

    @Test
    fun keySetDeadReturnsOnlyDeadKeys() {
        cache["a"] = A_VALUE
        cache["b"] = A_VALUE
        cache["c", A_VALUE] = ONE_SECOND

        dateProvider.setFixed(threeDaysFromNow())

        assertEquals(1, cache.keySetDead().size.toLong())
    }

    @Test
    fun keySetIsEmptyWhenClearingTheCache() {
        cache[A_KEY] = A_VALUE
        cache[B_KEY, B_VALUE] = THREE_DAYS
        cache.clear()

        assertEquals(0, cache.keySetStartingWith(A_KEY).size.toLong())
    }

    @Test
    fun keySetStartingWithShouldNeverCrash() {
        assertEquals(0, cache.keySetStartingWith(null).size.toLong())
        assertEquals(0, cache.keySetStartingWith("").size.toLong())
    }

    @Test
    fun setAndFindNone() {
        cache[A_KEY] = A_VALUE
        cache[B_KEY, B_VALUE] = THREE_DAYS

        assertEquals(0, cache.keySetStartingWith(A_VALUE).size.toLong())
    }

    @Test
    fun setAndFindOne() {
        cache[A_KEY] = A_VALUE
        cache[B_KEY, B_VALUE] = THREE_DAYS

        assertEquals(1, cache.keySetStartingWith(A_KEY).size.toLong())
    }

    @Test
    fun setAndFindMoreThanOne() {
        cache["weLoveAndroid"] = A_VALUE
        cache["weLoveLinux", B_VALUE] = THREE_DAYS
        cache["weHateNothing", B_VALUE] = THREE_DAYS

        assertEquals(2, cache.keySetStartingWith("weLove").size.toLong())
    }

    @Test
    fun setAndFindIfAlive() {
        cache[A_KEY, A_VALUE] = -1
        cache[B_KEY, B_VALUE] = THREE_DAYS

        assertEquals(0, cache.keySetAliveStartingWith(A_KEY).size.toLong())
        assertEquals(1, cache.keySetAliveStartingWith(B_KEY).size.toLong())
    }

    @Test
    fun keySetAlive() {
        cache[A_KEY] = A_VALUE
        cache[B_KEY, B_VALUE] = ONE_SECOND

        dateProvider.setFixed(threeDaysFromNow())

        assertTrue(cache.isKeyAlive(A_KEY))
        assertFalse(cache.isKeyAlive(B_KEY))

        assertFalse(cache.isKeyDead(A_KEY))
        assertTrue(cache.isKeyDead(B_KEY))
    }

}

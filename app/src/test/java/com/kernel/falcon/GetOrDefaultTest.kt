package com.kernel.falcon

import org.junit.Before
import org.junit.Test

import org.junit.Assert.assertEquals

class GetOrDefaultTest : BaseTest() {

    lateinit var cache: Cache<String>

    @Before
    fun init() {
        cache = Cache.Builder().build(context)
    }

    @Test
    fun valueIsReturnedIfItsPresent() {
        cache[A_KEY, BaseTest.A_VALUE] = ONE_SECOND

        val result = cache.get(A_KEY, "nothing")

        assertEquals(A_VALUE, result)
    }

    @Test
    fun defaultIsReturnedIfValueIsNotPresent() {
        cache.remove(A_KEY)

        val result = cache.get(A_KEY, "nothing")

        assertEquals("nothing", result)
    }

}

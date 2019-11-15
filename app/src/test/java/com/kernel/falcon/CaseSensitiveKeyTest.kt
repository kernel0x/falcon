package com.kernel.falcon

import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class CaseSensitiveKeyTest : BaseTest() {

    @Test
    fun shouldReturnTheSameIfIgnoringCaseSensitive() {
        val cache = Cache.Builder().caseSensitiveKeys(false).build<String>(context)
        cache[A_KEY.toLowerCase(), A_VALUE] = FOREVER

        assertEquals(A_VALUE, cache[A_KEY.toUpperCase()])
    }

    @Test
    fun shouldReturnNullIfUsingCaseSensitive() {
        val cache = Cache.Builder().caseSensitiveKeys(true).build<String>(context)
        cache[A_KEY.toLowerCase(), A_VALUE] = FOREVER

        assertNull(cache[A_KEY.toUpperCase()])
    }

    @Test
    fun shouldReturnNullIfUsingDefaultBuilder() {
        val cache = Cache.Builder().build<String>(context)
        cache[A_KEY.toLowerCase(), A_VALUE] = FOREVER

        assertNull(cache[A_KEY.toUpperCase()])
    }

}

package com.kernel.falcon

import org.junit.Before
import org.junit.Test

import org.junit.Assert.assertEquals

class MassiveDataTest : BaseTest() {

    lateinit var cache: Cache<Int>

    @Before
    fun init() {
        cache = Cache.Builder().build(context)
    }

    @Test
    fun worksWithLotsOfEntries() {
        val iterations = 10000
        for (i in 0 until iterations) {
            cache[i.toString(), i] = 0
        }
        for (i in 0 until iterations) {
            assertEquals(i, cache[i.toString()])
        }
    }

}

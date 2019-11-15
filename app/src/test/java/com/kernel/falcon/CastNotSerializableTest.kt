package com.kernel.falcon

import com.kernel.falcon.dummy.ObjectTestOne
import com.kernel.falcon.models.DualCacheMode
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CastNotSerializableTest : BaseTest() {

    lateinit var cache: Cache<ObjectTestOne>

    @Before
    fun init() {
        cache = Cache.Builder().build(context)
        cache.clear()
    }

    @Test
    fun saveObjectAndReturnSameInstance() {
        cache[A_KEY] = ObjectTestOne()

        assertThat(cache[A_KEY], instanceOf(ObjectTestOne::class.java))
    }

    @Test
    fun shouldCastObject() {
        cache[A_KEY] = ObjectTestOne()
        val objectTestOne = cache[A_KEY]

        assertThat(objectTestOne, instanceOf(ObjectTestOne::class.java))
    }

    @Test
    fun saveObjectAndReturnSameInstanceOnDisk() {
        cache[A_KEY, ObjectTestOne()] = DualCacheMode.ONLY_DISK

        Assert.assertNull(cache[A_KEY])
    }

    @Test
    fun shouldCastObjectOnDisk() {
        cache[A_KEY, ObjectTestOne()] = DualCacheMode.ONLY_DISK
        val objectTestOne = cache[A_KEY]

        Assert.assertNull(objectTestOne)
    }

    @Test
    fun saveObjectAndReturnSameInstanceOnRam() {
        cache[A_KEY, ObjectTestOne()] = DualCacheMode.ONLY_RAM

        assertThat(cache[A_KEY], instanceOf(ObjectTestOne::class.java))
    }

    @Test
    fun shouldCastObjectOnRam() {
        cache[A_KEY, ObjectTestOne()] = DualCacheMode.ONLY_RAM
        val objectTestOne = cache[A_KEY]

        assertThat(objectTestOne, instanceOf(ObjectTestOne::class.java))
    }

}

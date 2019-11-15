package com.kernel.falcon

import com.kernel.falcon.dummy.ObjectSerializableTestOne
import com.kernel.falcon.dummy.ObjectSerializableTestTwo
import com.kernel.falcon.models.DualCacheMode
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class CastTest : BaseTest() {

    lateinit var cache: Cache<ObjectSerializableTestOne>

    @Before
    fun init() {
        cache = Cache.Builder().build(context)
        cache.clear()
    }

    @Test
    fun saveObjectAndReturnSameInstance() {
        cache[A_KEY] = ObjectSerializableTestOne()

        assertThat(cache[A_KEY], instanceOf(ObjectSerializableTestOne::class.java))
    }

    @Test
    fun shouldCastObject() {
        cache[A_KEY] = ObjectSerializableTestOne()
        val objectTestOne = cache[A_KEY]

        assertThat(objectTestOne, instanceOf(ObjectSerializableTestOne::class.java))
    }

    @Test
    fun saveObjectAndReturnSameInstanceOnDisk() {
        cache[A_KEY, ObjectSerializableTestOne()] = DualCacheMode.ONLY_DISK

        assertThat(cache[A_KEY], instanceOf(ObjectSerializableTestOne::class.java))
    }

    @Test
    fun shouldCastObjectOnDisk() {
        cache[A_KEY, ObjectSerializableTestOne()] = DualCacheMode.ONLY_DISK
        val objectTestOne = cache[A_KEY]

        assertThat(objectTestOne, instanceOf(ObjectSerializableTestOne::class.java))
    }

    @Test
    fun saveObjectAndReturnSameInstanceOnRam() {
        cache[A_KEY, ObjectSerializableTestOne()] = DualCacheMode.ONLY_RAM

        assertThat(cache[A_KEY], instanceOf(ObjectSerializableTestOne::class.java))
    }

    @Test
    fun shouldCastObjectOnRam() {
        cache[A_KEY, ObjectSerializableTestOne()] = DualCacheMode.ONLY_RAM
        val objectTestOne = cache[A_KEY]

        assertThat(objectTestOne, instanceOf(ObjectSerializableTestOne::class.java))
    }

    @Test
    fun testCannotBeCast() {
        cache[A_KEY, ObjectSerializableTestOne()] = DualCacheMode.ONLY_RAM
        try {
            val objectTestOne: ObjectSerializableTestTwo = cache[A_KEY] as ObjectSerializableTestTwo
        } catch (e: ClassCastException) {
            // its ok
            return
        }
        assert(false)
    }

}

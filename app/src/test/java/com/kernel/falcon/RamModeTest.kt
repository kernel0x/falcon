package com.kernel.falcon

import com.kernel.falcon.dummy.ObjectSerializableTestOne
import com.kernel.falcon.models.DualCacheMode
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RamModeTest : BaseTest() {

    lateinit var cache: Cache<ObjectSerializableTestOne>

    @Before
    fun init() {
        cache = Cache.Builder().dualCacheMode(DualCacheMode.ONLY_RAM).build(context)
        cache.clear()
    }

    @Test
    fun saveObjectAndReceiveFromNewCache() {
        cache[A_KEY] = ObjectSerializableTestOne()
        val newCache = Cache.Builder().dualCacheMode(DualCacheMode.ONLY_RAM).build<Any>(context)
        Assert.assertNull(newCache[A_KEY])
    }

    @Test
    fun saveObjectInNewCacheAndReceiveFromOldCache() {
        val newCache = Cache.Builder().dualCacheMode(DualCacheMode.AUTO).build<Any>(context)
        newCache[A_KEY, ObjectSerializableTestOne()] = DualCacheMode.ONLY_RAM
        Assert.assertNull(cache[A_KEY])
    }

}

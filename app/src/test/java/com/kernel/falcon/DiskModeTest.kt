package com.kernel.falcon

import com.kernel.falcon.dummy.ObjectSerializableTestOne
import com.kernel.falcon.models.DualCacheMode
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class DiskModeTest : BaseTest() {

    lateinit var cache: Cache<ObjectSerializableTestOne>

    @Before
    fun init() {
        cache = Cache.Builder().dualCacheMode(DualCacheMode.ONLY_DISK).build(context)
        cache.clear()
    }

    @Test
    fun saveObjectAndReceiveFromNewCache() {
        cache[A_KEY] = ObjectSerializableTestOne()
        val newCache = Cache.Builder().dualCacheMode(DualCacheMode.ONLY_DISK).build<Any>(context)
        assertThat(newCache[A_KEY], instanceOf(ObjectSerializableTestOne::class.java))
    }

}

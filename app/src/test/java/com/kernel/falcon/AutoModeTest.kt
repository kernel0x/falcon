package com.kernel.falcon

import com.kernel.falcon.dummy.ObjectSerializableTestOne
import com.kernel.falcon.models.DualCacheMode
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test

class AutoModeTest : BaseTest() {

    @Test
    fun saveObjectAndReceiveFromNewCache() {
        val cache =
            Cache.Builder().dualCacheMode(DualCacheMode.AUTO).build<ObjectSerializableTestOne>(context)
        cache.clear()
        cache[A_KEY] = ObjectSerializableTestOne()
        val newCache = Cache.Builder().dualCacheMode(DualCacheMode.AUTO).build<Any>(context)
        assertThat(newCache[A_KEY], instanceOf(ObjectSerializableTestOne::class.java))
    }

}

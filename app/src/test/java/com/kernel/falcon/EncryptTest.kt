package com.kernel.falcon

import com.kernel.falcon.dummy.ObjectSerializableTestOne
import com.kernel.falcon.encrypt.Des3EncryptStrategy
import com.kernel.falcon.models.DualCacheMode
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException

class EncryptTest : BaseTest() {

    @Test
    fun testDes3EncryptStrategy() {
        val cache = Cache.Builder()
            .encryptStrategy(Des3EncryptStrategy(context, "qwertyuiopasdfghjklzxcvb", "vladimir"))
            .dualCacheMode(DualCacheMode.ONLY_DISK)
            .build<ObjectSerializableTestOne>(context)
        cache.clear()

        cache[A_KEY] = ObjectSerializableTestOne()

        assertThat(cache[A_KEY], instanceOf(ObjectSerializableTestOne::class.java))
    }

    @Test
    fun checkInputDes3EncryptStrategy() {
        try {
            val strategy = Des3EncryptStrategy(context, "unbreakable", "vladimir")
            strategy.encrypt("cuckoo")
        } catch (e: InvalidKeyException) {
            // its ok
        }

        try {
            val strategy = Des3EncryptStrategy(context, "qwertyuiopasdfghjklzxcvb", "ussr")
            strategy.encrypt("cuckoo")
        } catch (e: InvalidAlgorithmParameterException) {
            // its ok
        }
    }

    @Test
    fun checkValueDes3EncryptStrategy() {
        val strategy = Des3EncryptStrategy(context, "qwertyuiopasdfghjklzxcvb", "vladimir")
        assertEquals(strategy.encrypt("cuckoo"), "4tw9WkOWvCE=")
        assertEquals("cuckoo", strategy.decrypt("4tw9WkOWvCE="))
    }

}

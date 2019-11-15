package com.kernel.falcon

import com.kernel.falcon.models.DualCacheMode
import com.kernel.falcon.models.Size
import com.kernel.falcon.utils.RandomUtil
import org.junit.Assert
import org.junit.Test

class MaxSizeTest : BaseTest() {

    @Test
    fun testSaveWhenSizeIsFullOnAuto() {
        val cache =
            Cache.Builder().maxSize(Size(800, Size.Unit.B)).dualCacheMode(DualCacheMode.AUTO)
                .build<Int>(context)
        cache.clear()
        val iterations = 5
        for (i in 0 until iterations) {
            cache[i.toString()] = i
        }

        Assert.assertNull(cache[0.toString()])
        Assert.assertNull(cache[1.toString()])

        for (i in 2 until iterations) {
            Assert.assertEquals(i, cache[i.toString()])
        }
    }

    @Test
    fun testSaveWhenSizeIsFullOnRam() {
        val cache =
            Cache.Builder().maxSize(Size(800, Size.Unit.B)).dualCacheMode(DualCacheMode.ONLY_RAM)
                .build<Int>(context)
        val iterations = 5
        for (i in 0 until iterations) {
            cache[i.toString()] = i
        }

        Assert.assertNull(cache[0.toString()])
        Assert.assertNull(cache[1.toString()])

        for (i in 2 until iterations) {
            Assert.assertEquals(i, cache[i.toString()])
        }
    }

    @Test
    fun testSaveWhenSizeIsFullOnDisk() {
        val cache =
            Cache.Builder().maxSize(Size(800, Size.Unit.B)).dualCacheMode(DualCacheMode.ONLY_DISK)
                .build<Int>(context)
        cache.clear()
        val iterations = 5
        for (i in 0 until iterations) {
            cache[i.toString()] = i
        }

        Assert.assertNull(cache[0.toString()])
        Assert.assertNull(cache[1.toString()])
        //Assert.assertNull(cache[2.toString()])

        for (i in 3 until iterations) {
            Assert.assertEquals(i, cache[i.toString()])
        }
    }

}

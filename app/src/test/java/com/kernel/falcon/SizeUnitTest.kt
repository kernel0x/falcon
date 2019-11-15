package com.kernel.falcon

import com.kernel.falcon.models.Size
import org.junit.Assert
import org.junit.Test

class SizeUnitTest : BaseTest() {

    @Test
    fun testConvert() {
        Assert.assertEquals(10, Size(10, Size.Unit.B).valueAsByte)
        Assert.assertEquals(10000, Size(10, Size.Unit.KB).valueAsByte)
        Assert.assertEquals(10000000, Size(10, Size.Unit.MB).valueAsByte)
    }

}

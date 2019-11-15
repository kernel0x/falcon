package com.kernel.falcon

import com.kernel.falcon.models.CacheObject

import org.junit.Test

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class CacheObjectTest : BaseTest() {

    @Test
    fun testCreatedWithMaxLifetimeIsAliveRightNow() {
        val cacheObject = CacheObject<Any>(A_VALUE, now(), FOREVER)
        assertTrue(cacheObject.isAlive(now()))
    }

    @Test
    fun testCreatedWithMaxLifetimeIsAliveAfterThreeDays() {
        val cacheObject = CacheObject<Any>(A_VALUE, now(), FOREVER)
        assertTrue(cacheObject.isAlive(threeDaysFromNow()))
    }

    @Test
    fun testCreatedWithOneSecondOfLifetimeIsAliveRightNow() {
        val cacheObject = CacheObject<Any>(A_VALUE, now(), ONE_SECOND)
        assertTrue(cacheObject.isAlive(now()))
    }

    @Test
    fun testCreatedWithTwoHoursOfLifetimeIsAliveAfterASecond() {
        val cacheObject = CacheObject<Any>(A_VALUE, now(), TWO_HOURS)
        assertTrue(cacheObject.isAlive(oneSecondFromNow()))
    }

    @Test
    fun testCreatedWithOneSecondOfLifetimeIsNotAliveAfterTwoSeconds() {
        val cacheObject = CacheObject<Any>(A_VALUE, now(), ONE_SECOND)
        assertFalse(cacheObject.isAlive(twoHoursFromNow()))
    }

}

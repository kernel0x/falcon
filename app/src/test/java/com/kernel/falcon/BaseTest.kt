package com.kernel.falcon

import android.content.Context
import com.kernel.falcon.models.Duration
import org.junit.Before
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.io.File
import java.util.concurrent.TimeUnit

abstract class BaseTest {

    protected val context: Context = mock(Context::class.java)

    @Before
    fun before() {
        `when`(context.cacheDir).thenReturn(File("test"))
    }

    protected fun now(): Long {
        return System.currentTimeMillis()
    }

    protected fun oneSecondFromNow(): Long {
        return now() + ONE_SECOND
    }

    protected fun twoHoursFromNow(): Long {
        return now() + TWO_HOURS
    }

    protected fun threeDaysFromNow(): Long {
        return now() + THREE_DAYS
    }

    companion object {
        const val A_KEY = "aKey"
        const val A_VALUE = "aValue"
        const val B_KEY = "bKey"
        const val B_VALUE = "bValue"

        val FOREVER = Duration.ZERO.valueAsMs
        val ONE_SECOND = Duration(1, TimeUnit.SECONDS).valueAsMs
        val TWO_HOURS = Duration(2, TimeUnit.HOURS).valueAsMs
        val THREE_DAYS = Duration(3, TimeUnit.DAYS).valueAsMs
    }
}

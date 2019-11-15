package com.kernel.falcon.mock

import com.kernel.falcon.utils.DateProvider

class MockDateProvider : DateProvider {

    private var fakeNow: Long? = null

    fun setFixed(fakeNow: Long) {
        this.fakeNow = fakeNow
    }

    fun setSystem() {
        this.fakeNow = null
    }

    override fun now(): Long {
        return fakeNow ?: DateProvider.SYSTEM.now()
    }

}

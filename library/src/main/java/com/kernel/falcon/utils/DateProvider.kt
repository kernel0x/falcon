package com.kernel.falcon.utils

interface DateProvider {

    fun now(): Long

    companion object {
        val SYSTEM: DateProvider = object : DateProvider {
            override fun now(): Long {
                return System.currentTimeMillis()
            }
        }
    }

}

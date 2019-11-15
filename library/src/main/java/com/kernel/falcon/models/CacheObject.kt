package com.kernel.falcon.models

import com.kernel.falcon.Config.LIFETIME_FOREVER

class CacheObject<T>(
    val value: T,
    val timeCreation: Long,
    private val lifetime: Long): java.io.Serializable {

    fun isAlive(now: Long): Boolean {
        return when (lifetime) {
            LIFETIME_FOREVER -> true
            else -> timeCreation + lifetime > now
        }
    }
}
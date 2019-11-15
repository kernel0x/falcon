package com.kernel.falcon.encrypt

interface EncryptStrategy {

    fun encrypt(value: String): String
    fun decrypt(value: String): String

}
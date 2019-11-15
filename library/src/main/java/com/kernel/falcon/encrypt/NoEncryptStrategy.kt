package com.kernel.falcon.encrypt

class NoEncryptStrategy : EncryptStrategy {

    override fun encrypt(value: String): String {
        return value
    }

    override fun decrypt(value: String): String {
        return value
    }

}
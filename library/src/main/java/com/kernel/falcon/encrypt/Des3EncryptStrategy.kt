package com.kernel.falcon.encrypt

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.text.TextUtils

import com.kernel.falcon.utils.Des3Util
import com.kernel.falcon.utils.KeyStoreUtil

import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException

class Des3EncryptStrategy(
    private val context: Context,
    private val secretKey: String,
    private val iv: String) : EncryptStrategy {

    private val androidID: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    override fun encrypt(str: String): String {
        return try {
            Des3Util.encode(str, this.secretKey, this.iv)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override fun decrypt(str: String): String {
        return try {
            Des3Util.decrypt(str, this.secretKey, this.iv)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun createSecretKey(): String {
        val secretKey: String
        val str = androidID
        secretKey = if (str.length > 24) {
            str.substring(0, 24)
        } else {
            str + getStr(24 - str.length)
        }

        return secretKey
    }

    private fun get24Str(str: String): String {
        if (TextUtils.isEmpty(str)) return ""
        return if (str.length >= 24) {
            str.substring(0, 24)
        } else str + getStr(24 - str.length)
    }

    private fun getStr(num: Int): String {
        val builder = StringBuilder()
        for (i in 0 until num) {
            builder.append("a")
        }
        return builder.toString()
    }

    fun createKeyStoreSecretKey() {
        try {
            KeyStoreUtil.createKeys(context, context.packageName)
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }
    }

}
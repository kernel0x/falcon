package com.kernel.falcon.encrypt

import android.content.Context

import com.kernel.falcon.utils.KeyStoreUtil

import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException

class KeyStoreEncryptStrategy @JvmOverloads constructor(
    private val context: Context,
    alias: String = context.packageName) : EncryptStrategy {

    private val alias: String = context.packageName + alias

    init {
        createKeyStoreSecretKey(this.alias)
    }

    override fun encrypt(value: String): String {
        return try {
            KeyStoreUtil.encrypt(alias, value)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override fun decrypt(value: String): String {
        return try {
            KeyStoreUtil.decrypt(alias, value)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun createKeyStoreSecretKey(alias: String) {
        try {
            KeyStoreUtil.createKeys(context, alias)
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }
    }

}
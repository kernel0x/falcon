package com.kernel.falcon.utils;

import android.text.TextUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Des3Util {

    private static final String DESEDE_CBC_PKCS5_PADDING = "desede/CBC/PKCS5Padding";
    private static String encoding = "utf-8";

    /**
     * Encryption
     *
     * @param plainText to encrypt text
     * @param secretKey key
     * @param iv        offset
     * @return encrypted text
     * @throws Exception
     */
    public static String encode(String plainText, String secretKey, String iv) throws Exception {
        if (TextUtils.isEmpty(secretKey) || TextUtils.isEmpty(iv)) {
            throw new IllegalArgumentException("secret key or iv is empty");
        }
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        SecretKey deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(DESEDE_CBC_PKCS5_PADDING);
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(1, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64Util.encode(encryptData);
    }

    /***
     * Decryption
     *
     * @param encryptText to decrypt text
     * @param secretKey   key
     * @param iv          offset
     * @return decrypted text
     * @throws Exception
     */
    public static String decrypt(String encryptText, String secretKey, String iv) throws Exception {
        if (TextUtils.isEmpty(secretKey) || TextUtils.isEmpty(iv)) {
            throw new IllegalArgumentException("secret key or iv is empty");
        }
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        SecretKey deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(DESEDE_CBC_PKCS5_PADDING);
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(2, deskey, ips);
        byte[] decryptData = cipher.doFinal(Base64Util.decode(encryptText));
        return new String(decryptData, encoding);
    }

}
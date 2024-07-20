package com.program.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignUtils {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 100000;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 100000;

    public final static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDcG3/kviNYueK02Huu1/y7uGf5\n" +
            "DG4ChrhqkbfAqzP5faQgKcBZeRRkgXrg6QT9usxCpBpGvkxfqVR34Q7U5qYFY85y\n" +
            "ObADSkl2tt77g9eMiVlL5oKYAIPlzQzike/MD6LrnEHOyp0Irq8QoZ5gdnpQ3yJ2\n" +
            "WdIjwbQLzPrFtPFA1QIDAQAB";

    public final static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANwbf+S+I1i54rTY\n" +
            "e67X/Lu4Z/kMbgKGuGqRt8CrM/l9pCApwFl5FGSBeuDpBP26zEKkGka+TF+pVHfh\n" +
            "DtTmpgVjznI5sANKSXa23vuD14yJWUvmgpgAg+XNDOKR78wPouucQc7KnQiurxCh\n" +
            "nmB2elDfInZZ0iPBtAvM+sW08UDVAgMBAAECgYAUxAdp5kJhcZAg7belhD0U3M36\n" +
            "YiDS3jDx5POIGt7Zb/AXFLlP96tj3A9ivrk40vHIa8EK4ZNFBy2v7ay/i08oUDTO\n" +
            "r0Lo1YSmc9G+nmIiPvFLQAWIzKpjn8zpBsBYKsBSyAjq8vrAa4r9KTalF5ruNgEx\n" +
            "WXXC8bqegYyWW0UnwQJBAPU27hTLHB8CGi7zyayS6jTjdakenUXsbrYoBiK0bvB5\n" +
            "6QnFg0A2cXCu6HUTKcDx/7DENgWbjw4SKcPGi1mICgUCQQDlyd4nOsVIcsuZF9nv\n" +
            "arMYZwGeM6CLAsWf6ylgeg107b00t+A54fUs4kaOWQUDnoRmfr0X14A6Vk+ql6R7\n" +
            "JYSRAkBBmCcJwudL2Cke5DHPiyFBcpMX4Uua18spyP0TLYb7pvDSn1YjyCyCQxeF\n" +
            "sdGafmGybFozF9Clp/AqIaNHGN/tAkEAxZu3DtdspuQJkItBYLHKeHbEnm7ZZhIp\n" +
            "L2BFAfGUNvTn3Dkwe7aEaGfiszF8rWMZiyb8qE8rt39YHWUxDrHx8QJAPhkcNM37\n" +
            "7QqykmeDh+t6jH9tzFfpZw2qRdjUEld26bx+BUwHxqukq6edLfZ6uf5U9dmOfCvP\n" +
            "EEQY/tHQcYcitg==";

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(512);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        out.close();
        // 解密后的内容
        return out.toString("UTF-8");
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) {
        try {
            byte[] keyBytes = publicKey.getEncoded();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(key);
            signature.update(srcData.getBytes());
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception ignore) {
            return false;
        }
    }
}
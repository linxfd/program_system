package com.program.utils;

import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author linxf
 * @date 2024/9/8
 */
public class HashUtil {
    // 计算文件流的MD5值
    public static String computeMD5(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, length);
        }
        byte[] digest = messageDigest.digest();
        return Hex.encodeHexString(digest);
    }

}

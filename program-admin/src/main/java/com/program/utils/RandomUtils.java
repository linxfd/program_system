package com.program.utils;

import java.util.Random;

/**
 * @author linxf
 * @date 2024/8/1
 */
public class RandomUtils {
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String CHARSETNUMBER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    // 默认长度
    private static int LENGTH = 8;

    public RandomUtils(Integer length){
        LENGTH = length;
    }
    public static String generateUsername() {
        StringBuilder sb = new StringBuilder(LENGTH);
        Random random = new Random();

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(index));
        }

        return sb.toString();
    }
    public static String generatePassword() {
        StringBuilder sb = new StringBuilder(LENGTH);
        Random random = new Random();

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARSETNUMBER.length());
            sb.append(CHARSETNUMBER.charAt(index));
        }

        return sb.toString();
    }
}

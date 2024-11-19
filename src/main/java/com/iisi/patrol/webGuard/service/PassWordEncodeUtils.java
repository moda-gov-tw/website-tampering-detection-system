package com.iisi.patrol.webGuard.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class PassWordEncodeUtils {

    // 盐值定义为常量，使用 char[] 类型存储
    private final static char[] salt = "iwg".toCharArray();

    public static String encodePassword(char[] password) {
        char[] combined = null;
        byte[] byteData = null;
        byte[] encodedBytes = null;
        try {
            // 将盐值和密码合并到一个字符数组中
            combined = new char[salt.length + password.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(password, 0, combined, salt.length, password.length);

            // 将合并后的字符数组安全转换为字节数组
            byteData = new byte[combined.length];
            for (int i = 0; i < combined.length; i++) {
                byteData[i] = (byte) combined[i];
            }

            // 对字节数组进行 Base64 编码
            encodedBytes = Base64.getEncoder().encode(byteData);

            // 返回编码结果
            return new String(encodedBytes, StandardCharsets.UTF_8);

        } finally {
            // 清除敏感数据
            if (combined != null) {
                Arrays.fill(combined, '\0');  // 清除合并后的字符数组
            }
            if (byteData != null) {
                Arrays.fill(byteData, (byte) 0); // 清除字节数组
            }
            if (encodedBytes != null) {
                Arrays.fill(encodedBytes, (byte) 0); // 清除编码后的字节数组
            }
        }
    }

    public static String decodePassword(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            throw new IllegalArgumentException("输入的编码密码不能为空");
        }

        byte[] decodedBytes = null;
        char[] decodedCharArray = null;
        char[] passwordChars = null;

        try {
            // 解码 Base64 编码的密码
            decodedBytes = Base64.getDecoder().decode(encodedPassword.getBytes(StandardCharsets.UTF_8));

            // 将字节数组转换为字符数组
            decodedCharArray = new char[decodedBytes.length];
            for (int i = 0; i < decodedBytes.length; i++) {
                decodedCharArray[i] = (char) decodedBytes[i];
            }

            // 移除盐值部分并提取密码
            if (decodedCharArray.length > salt.length) {
                passwordChars = new char[decodedCharArray.length - salt.length];
                System.arraycopy(decodedCharArray, salt.length, passwordChars, 0, passwordChars.length);
            } else {
                throw new IllegalArgumentException("解码后的长度不足以移除盐值");
            }

            // 返回密码的 String 表示
            return new String(passwordChars);

        } finally {
            // 清除敏感数据
            if (decodedBytes != null) {
                Arrays.fill(decodedBytes, (byte) 0); // 清除解码后的字节数组
            }
            if (decodedCharArray != null) {
                Arrays.fill(decodedCharArray, '\0'); // 清除解码后的字符数组
            }
            if (passwordChars != null) {
                Arrays.fill(passwordChars, '\0'); // 清除密码字符数组
            }
        }
    }
}

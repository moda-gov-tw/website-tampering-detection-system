package com.iisi.patrol.webGuard.service;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Base64;

public class PassWordEncodeUtils {

    // 盐值定义为常量，使用 char[] 类型存储
    private final static char[] salt = "iwg".toCharArray();

    public static String encodePassword(char[] password) {
        char[] combined = null;
        byte[] encodedBytes = null;
        try {
            // 将盐值和密码合并到一个字符数组中
            combined = new char[salt.length + password.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(password, 0, combined, salt.length, password.length);

            // 将合并后的字符数组转换为字节并进行 Base64 编码
            encodedBytes = Base64.getEncoder().encode(new String(combined).getBytes());
            return new String(encodedBytes);

        } finally {
            // 清除敏感数据
            if (combined != null) {
                Arrays.fill(combined, '\0');  // 清除合并后的字符数组
            }
            if (encodedBytes != null) {
                Arrays.fill(encodedBytes, (byte) 0); // 清除编码后的字节数组
            }
        }
    }

    public static String decodePassword(String encodedPassword) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword.getBytes());
        String decodedStr = new String(decodedBytes);

        // 使用 char[] 存储解码后的结果
        char[] decodedCharArray = decodedStr.toCharArray();

        try {
            if (StringUtils.isNotBlank(decodedStr)) {
                // 移除盐值部分并返回密码
                char[] passwordChars = new char[decodedCharArray.length - salt.length];
                System.arraycopy(decodedCharArray, salt.length, passwordChars, 0, passwordChars.length);

                // 清除中间解码结果
                Arrays.fill(decodedCharArray, '\0');

                // 返回密码
                return new String(passwordChars);
            } else {
                // 解码失败，返回空字符串
                return "";
            }
        } finally {
            // 清除敏感数据
            Arrays.fill(decodedCharArray, '\0');  // 清除解码后的字符数组
        }
    }
}

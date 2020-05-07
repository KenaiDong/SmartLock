package com.xuzd.commons.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @date 2016年9月20日下午4:44:25
 *
 * @version v0.1
 * @since jdk 1.8
 * @description 加密工具类
 * @update
 */
public class EncryptUtil {

    /**
     * 二次加密 先MD5加密再 sha求hash
     *
     * @param inputText
     * @return
     */
    public static String md5AndSha(String inputText) {
        return sha(md5(inputText));
    }

    /**
     * 二次加密 先求hash再md5加密
     *
     * @param inputText
     * @return
     */
    public static String shaAndMd5(String inputText) {
        return md5(sha(inputText));
    }

    /**
     * md5加密
     *
     * @param inputText
     * @return
     */
    public static String md5(String inputText) {
        return encrypt(inputText, "md5");
    }

    /**
     * sha加密
     *
     * @param inputText
     * @return
     */
    public static String sha(String inputText) {
        return encrypt(inputText, "sha-1");
    }

    /**
     * md5又或者sha-1加密
     * @param inputText 需要加密的内容
     * @param algorithmName 加密的模式
     * @return
     */
    private static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes("UTF8"));
            byte s[] = m.digest();
            // m.digest(inputText.getBytes("UTF8"));
            return hex(s);
        } catch (NoSuchAlgorithmException e) {

        } catch (UnsupportedEncodingException e) {

        }
        return encryptText;
    }

    /**
     * 返回十六进制字符串
     *
     * @param arr
     * @return
     */
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(md5("UXSINO"));
        System.out.println(sha("UXSINO"));
        System.out.println(md5AndSha("UXSINO"));
        System.out.println(shaAndMd5("admin"));
        // System.out.println(MixCodeUtil.getUniqueCode());
    }

}


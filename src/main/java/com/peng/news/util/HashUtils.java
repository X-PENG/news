package com.peng.news.util;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/7/2 9:56
 */

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * get md5 hash string
 */
@Slf4j
public class HashUtils {
    private static char hexDigits[] = { '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };

    private static char charDigits[] = {
            '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'};

    /**
     * 把long型转换为某个长度的字符串
     * @param hid
     * @param len
     * @return
     */
    public static String convertToHashStr(long hid, int len) {
        StringBuffer sb = new StringBuffer();

        for(int i=0; i<len; i++) {
            char c = charDigits[(int) ((hid&0xff) % charDigits.length)];
            sb.append(c);
            hid = hid >> 6;
        }

        return sb.toString();
    }

    /**
     * 获取文件流的md5值（注意，inputstream读完后会被关闭）
     * @param input
     * @return
     */
    public static String getFileMd5(InputStream input) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            byte buffer[] = new byte[1024];
            int len = 0;
            try {
                while((len = input.read(buffer)) > 0) {
                    mdInst.update(buffer, 0, len);
                }
            } catch (IOException e1) {
            }
            input.close();

            byte[] digests = mdInst.digest();

            int j = digests.length;
            char str[] = new char[j * 2];

            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = digests[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.warn("", e);
        }

        return "";
    }

    public static String getSHA1(String str) {
        return getSHA1(str.getBytes());
    }

    public static String getSHA1(byte[] bytes) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-1");
            mdInst.update(bytes);
            byte[] digests = mdInst.digest();

            int j = digests.length;
            char str[] = new char[j * 2];

            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = digests[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            log.warn("", e);
        }

        return "";
    }

    public static String getMd5(String str) {
        return getMd5(str.getBytes());
    }

    /**
     * 获取字节流的md5值
     * @param bytes
     * @return
     */
    public static String getMd5(byte[] bytes) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(bytes);
            byte[] digests = mdInst.digest();

            int j = digests.length;
            char str[] = new char[j * 2];

            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = digests[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            log.warn("", e);
        }

        return "";
    }
}
package com.javaPro.myProject.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;


public class MD5Util {
    public MD5Util() {
    }

    public static String md5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            return bytesToHex(md5.digest());
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String md5slat(String str) {
        return md5(md5("news" + str + "xi1914"));
    }

    public static String bytesToHex(byte[] bytes) {
        BigInteger bigInt = new BigInteger(1, bytes);

        String hashtext;
        for(hashtext = bigInt.toString(16); hashtext.length() < 32; hashtext = "0" + hashtext) {
        }

        return hashtext;
    }

    public static void main(String[] args) {
//        System.out.println(md5("1234"));
        System.out.println(md5slat("!@2023shanshan"));
        System.out.println("UUIDUtil.getUUID() = " + UUIDUtil.getUUID().length());
    }
}

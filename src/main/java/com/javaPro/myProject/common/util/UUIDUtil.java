package com.javaPro.myProject.common.util;

import java.util.UUID;

//UIDUntis工具类，主要生成32位随机数
public class UUIDUtil {

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");

    }

}


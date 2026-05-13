package com.javaPro.myProject.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @create-date: 2023/5/10 10:19
 */
public class DateUtil {
    private static final String yyyymmdd = "yyyy-MM-dd";

    public static Date getDateByStr(String str){
        SimpleDateFormat sdf = new SimpleDateFormat(yyyymmdd);
        try {
            Date parse = sdf.parse(str);
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
        }
return null;
    }

    public static String getTimeStr(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(yyyymmdd);
        String format = sdf.format(date);
        return format;
    }
}

package org.loxf.metric.base.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hutingting on 2017/7/5.
 */
public class DateUtil {

    public static Date stringToDate(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(str);
            return date;
        }catch (Exception e){
            //todo 打印异常
            return null;
        }
    }

    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String str = sdf.format(date);
            return str;
        }catch (Exception e){
            return null;
        }
    }

    public static String dateToISODATEString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CHINA);;
        try {
            return sdf.format(date);
        }catch (Exception e){
            return null;
        }
    }

    public static Date turnToMongoDate(Date date) {
        return getTargetDate(date,8);
    }

    public static Date mongoDateTurnToJavaDate(Date date) {
       return getTargetDate(date,-8);
    }
    private static Date getTargetDate(Date date,int calendarAmount){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, calendarAmount);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return stringToDate(sdf.format(ca.getTime()));
    }

}

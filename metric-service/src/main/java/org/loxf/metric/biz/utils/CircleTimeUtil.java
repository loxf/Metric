package org.loxf.metric.biz.utils;

import org.loxf.metric.base.exception.MetricException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by luohj on 2017/5/8.
 */
public class CircleTimeUtil {
    private static Logger logger = LoggerFactory.getLogger(CircleTimeUtil.class);
    private CircleTimeUtil(){};

    public static String formatCircleTime(long circleTime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return formatter.format(new Date(circleTime));
    }
    public static String formatCircleTimeWithoutSSS(long circleTime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date(circleTime));
    }
    public static String formatCircleTimeYMD(long circleTime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date(circleTime));
    }
    /**
     * 获取当前账期的日开始时间 24小时制
     * @param circleTime
     * @return
     */
    public static Date getStartTime(long circleTime) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(new Date(circleTime));
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 获取当前账期的日结束时间 24 小时制
     * @param circleTime
     * @return
     */
    public static  Date getEndTime(long circleTime) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(new Date(circleTime));
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }


    /**
     * 获取当前账期的昨天结束时间 24小时制
     * @param circleTime
     * @return
     */
    public static Date getYesterdayEndTime(long circleTime){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(circleTime));
        calendar.add(Calendar.DATE,-1);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取当前账期的当月开始时间 24小时制
     * @param circleTime
     * @return
     */
    public static Date getMonthBeginTime(long circleTime){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(circleTime));
        calendar.set(Calendar.DAY_OF_MONTH,1);//当月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String formatCircleTimeByFormatter(long circleTime, String formatterStr){
        SimpleDateFormat formatter = new SimpleDateFormat(formatterStr);
        return formatter.format(new Date(circleTime));
    }

    public static String formatCircleTimeByParticleSize(String timeStr, int particleSize){
        try {
            if((particleSize>0 && particleSize==1440)||particleSize==-1) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                return formatter.format(formatter.parse(timeStr));
            } else if(particleSize==-2) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
                return formatter.format(formatter.parse(timeStr));
            } else if(particleSize==-3) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                return formatter.format(formatter.parse(timeStr));
            } else {
                return timeStr;
            }
        } catch (ParseException e) {
            logger.error("账期转化错误[" + particleSize + "]" + timeStr , e);
            return timeStr;
        }
    }
    public static String formatCircleTimeByParticleSize(Date date, int particleSize){
        if((particleSize>0 && particleSize==1440)||particleSize==-1) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date);
        } else if(particleSize==-2) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
            return formatter.format(date);
        } else if(particleSize==-3) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            return formatter.format(date);
        } else {
            return date.toString();
        }
    }

    public static Date formatStrToDate(String timeStr, int particleSize){
        try {
            if((particleSize>0 && particleSize==1440)||particleSize==-1) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                return formatter.parse(timeStr);
            } else if(particleSize==-2) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
                return formatter.parse(timeStr);
            } else if(particleSize==-3) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                return formatter.parse(timeStr);
            } else {
                return new Date(timeStr);
            }
        } catch (ParseException e) {
            logger.error("账期转化错误[" + particleSize + "]" + timeStr , e);
            return null;
        }
    }

    /**
     * @param currentTime 当前账期
     * @param interval 间隔 >0为分钟，-1为周 -2为月 -3 为年
     * @return
     */
    public static long getNextCircleTime(Date currentTime, long interval){
        long nextTime = 0;
        if(interval>0) {
            // 按分钟数计算
            nextTime = currentTime.getTime() + interval*60*1000;
        } else if(interval==-1){
            //按周
            nextTime = currentTime.getTime() + 7*interval*60*1000;
        } else if(interval==-2){
            //按月
            Calendar c = Calendar.getInstance();
            c.setTime(currentTime);
            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
            nextTime = c.getTime().getTime();
        } else if(interval==-3){
            //按年
            Calendar c = Calendar.getInstance();
            c.setTime(currentTime);
            c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
            nextTime = c.getTime().getTime();
        } else {
            throw new MetricException("账期间隔暂不支持当前参数：" + interval);
        }
        return nextTime;
    }
    public static void main(String args[]){
        System.out.println(CircleTimeUtil.formatStrToDate(
                "2017-06-05 00:00:00.0", 1440));
        System.out.println(CircleTimeUtil.formatStrToDate(
                "2017-06-05 00:00:00.0", -1));
        System.out.println(CircleTimeUtil.formatStrToDate(
                "2017-06-05 00:00:00.0", -2));
        System.out.println(CircleTimeUtil.formatStrToDate(
                "2017-06-05 00:00:00.0", -3));
    }
}

package com.sum.xlog.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间转换工具类
 */
public class DateUtil {

    private static final String TAG = "DateUtil";

    /**
     * 根据时间字符串及时间格式返回时间毫秒数
     *
     * @param dateString 时间字符串
     * @param timeType   时间格式
     */
    public static long string2Millis(String dateString, String timeType, boolean isTimeZone) {
        long result = 0;
        DateFormat dateFormat = new SimpleDateFormat(timeType, Locale.US);
        if (isTimeZone)
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = dateFormat.parse(dateString);
            result = date.getTime();
        } catch (Exception e) {
            Log.e(TAG, String.format("解析时间失败 dateString:%s, timeType:%s",dateString, timeType), e) ;
        }
        return result;
    }

    public static String millis2String(long millis, String timeType, boolean isTimeZone) {
        String result = "";
        DateFormat dateFormat = new SimpleDateFormat(timeType, Locale.US);
        if (isTimeZone)
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            result = dateFormat.format(new Date(millis));
        } catch (Exception e) {
            Log.e(TAG, String.format("解析时间失败 millis:%s, timeType:%s", millis, timeType), e);
        }
        return result;
    }

}

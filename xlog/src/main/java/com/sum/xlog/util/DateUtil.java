package com.sum.xlog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import android.content.Context;
import android.util.Log;

import com.sum.xlog.core.XLog;

/**
 * @class DateUtil
 * @brief 时间转换工具类
 * @author guanghua.xiao
 * @date 2013-8-6 下午6:02:36
 */
public class DateUtil {

	///配置是否使用utc时间戳
	private static final String TAG = DateUtil.class.getSimpleName();
	private static final String WDY_DATE_TIME_ZONE_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss z";
	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String TIME_PATTERN = "HH:mm:ss SSS";
	private static final String MONTH_DATE_TIME_PATTERN = "MM/dd HH:mm";

	/**
	 * @brief 格式: EEE, dd-MMM-yyyy HH:mm:ss z
	 */
	public static final int GMT_ENGLISH_PATTERN_FLAG = 0;

	/**
	 * @brief 格式: yyyy-MM-dd HH:mm:ss
	 */
	public static final int DATE_TIME_PATTERN_FLAG = 1;
	/**
	 * @brief 格式: yyyy-MM-dd
	 */
	public static final int DATE_PATTERN_FLAG = 2;
	/**
	 * @brief 格式: yyyy-MM-dd HH:mm
	 */
	public static final int MINUTE_PATTERN_FLAG = 3;
	/**
	 * @brief 格式：HH:mm:ss SSS
	 *  - 用于记录日志打印时间
	 */
	public static final int TIME_PATTERN_FLAG = 4;
	/**
	 * @brief 格式：MM/dd HH:mm
	 *  - 用于显示消息更新时间
	 */
	public static final int MONTH_DATE_TIME_PATTERN_FLAG = 5;
	/**
	 * 一天时间毫秒
	 */
	public static final long ONE_DAY_TIME = 1000  * 60 * 60 * 24L ;

	private DateUtil() {}

	/**
	 * @brief 将日期对象格式化成指定格式的字符串
	 * @param date
	 *            日期对象
	 * @param patternFlag
	 *            日期格式标识符
	 * @return String 字符串格式的日期
	 */
	public static String formatDate(Date date, int patternFlag) {
		SimpleDateFormat dateFormat;
		String dateString = "";
		if (date == null) {
			return dateString;
		}
		switch (patternFlag) {
		case GMT_ENGLISH_PATTERN_FLAG:
			dateFormat = new SimpleDateFormat(WDY_DATE_TIME_ZONE_PATTERN,
					Locale.ENGLISH);
			dateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
			dateString = dateFormat.format(date);
			break;
		case DATE_TIME_PATTERN_FLAG:
			dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
			dateString = dateFormat.format(date);
			break;
		case DATE_PATTERN_FLAG:
			dateFormat = new SimpleDateFormat(DATE_PATTERN);
			dateString = dateFormat.format(date);
			break;
		case MINUTE_PATTERN_FLAG:
			dateFormat = new SimpleDateFormat(MINUTE_PATTERN);
			dateString = dateFormat.format(date);
			break;
		case TIME_PATTERN_FLAG:
			dateFormat = new SimpleDateFormat(TIME_PATTERN);
			dateString = dateFormat.format(date);
			break;
		case MONTH_DATE_TIME_PATTERN_FLAG:
			dateFormat = new SimpleDateFormat(MONTH_DATE_TIME_PATTERN);
			dateString = dateFormat.format(date);
			break;
		default:
			XLog.i("Unknown patternFlag:" + patternFlag);
		}
		return dateString;
	}

	/**
	 * @brief 将字符串格式的日期解析为日期对象
	 * @param dateString
	 *            日期字符串
	 * @param patternFlag
	 *            格式标识符
	 * @return Date 解析出来的日期对象，如果解析失败，则返回null
	 */
	public static Date parseDate(String dateString, int patternFlag) {
		SimpleDateFormat dateFormat;
		Date date = null;
		if (dateString == null || "".equals(dateString.trim())) {
			return date;
		}
		dateString = dateString.trim();
		try {
			switch (patternFlag) {
			case GMT_ENGLISH_PATTERN_FLAG:
				dateFormat = new SimpleDateFormat(WDY_DATE_TIME_ZONE_PATTERN,
						Locale.ENGLISH);
				dateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
				date = dateFormat.parse(dateString);
				break;
			case DATE_TIME_PATTERN_FLAG:
				dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
				date = dateFormat.parse(dateString);
				break;
			case DATE_PATTERN_FLAG:
				dateFormat = new SimpleDateFormat(DATE_PATTERN);
				date = dateFormat.parse(dateString);
				break;
			case MINUTE_PATTERN_FLAG:
				dateFormat = new SimpleDateFormat(MINUTE_PATTERN);
				date = dateFormat.parse(dateString);
				break;
			case TIME_PATTERN_FLAG:
				dateFormat = new SimpleDateFormat(TIME_PATTERN);
				date = dateFormat.parse(dateString);
				break;
			case MONTH_DATE_TIME_PATTERN_FLAG:
				dateFormat = new SimpleDateFormat(MONTH_DATE_TIME_PATTERN);
				date = dateFormat.parse(dateString);
				break;
			default:
				Log.w(TAG, "Unknown patternFlag:" + patternFlag);
			}
		} catch (ParseException e) {
			Log.w(TAG, "[parseDate]", e);
		}
		return date;
	}

	/**
	 * 返回从当前日志开始算指定的天数之前的日期
	 * 
	 * @return
	 */
	public static Date getNowDateBefore(int days) {
		Date nowTime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowTime);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
		return now.getTime();
	}

	/**
	 * @brief 获取当前日期
	 * @return 当前日期的Date对象
	 */
	public static Date getNowDate() {
		Date nowTime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowTime);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.HOUR, 0);
		now.set(Calendar.AM_PM, Calendar.AM);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		return now.getTime();
	}

	/**
	 * @brief 获取当前月份
	 * @return Date 代表当前月份的Date对象
	 */
	public static Date getNowMonth() {
		
		Date nowTime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowTime);
		now.set(Calendar.DATE, 1);
		now.set(Calendar.HOUR, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.AM_PM, Calendar.AM);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		Log.d(TAG, now.toString());
		return now.getTime();
	}

	/**
	 * @brief 给startTime 添加指定的秒数
	 * @param startTime
	 *            要添加的时间
	 * @param seconds
	 *            要添加的秒数
	 * @return 返回的时间
	 */
	public static Date addSeconds(Date startTime, int seconds) {
		Calendar calendar = Calendar.getInstance();
		// 将字符串转成Date对象
		calendar.setTime(startTime);
		// 对Calendar对象加上分钟
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}

	/**
	 * @brief 根据市区转换当前显示时间
	 * @param time
	 *            时间字符串
	 * @param fromZone
	 *            安排会议时区
	 * @param toZone
	 *            手机当前所在时区
	 * @return 转换后的时间
	 */
	public static String parseTimeZone(String time, String fromZone,
			String toZone) {
		// 将传进来的字符串转化成时区
		TimeZone fromzone = TimeZone.getTimeZone(fromZone);
		TimeZone tozone = TimeZone.getTimeZone(toZone);

		// 输入格式转换对象
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 设置时区
		inputFormat.setTimeZone(fromzone);
		Date date = null;
		try {
			// 将字符串格式转化成Date类型
			date = inputFormat.parse(time);
		} catch (ParseException e) {
			XLog.e("Exception while parsing string date", e);
		}

		// 输出格式转换对象
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 设置时区
		outputFormat.setTimeZone(tozone);
		// 获取转换后的值
		return outputFormat.format(date);
	}



	

	/**
	 * @brief
	 * @param timeFormate
	 * @param timemillis
	 * @return
	 */
	public static String pareConferenceTime2String(String timeFormate,
			long timemillis) {
		String result;
		// 输出格式转换对象
		SimpleDateFormat outputFormat = new SimpleDateFormat(timeFormate);
		Date date = new Date(timemillis);
		result = outputFormat.format(date);
		return result;
	}

	/**
	 * @brief 根据日期显示类型将字符串日期转为Date
	 * @param str
	 *            字符串日期
	 * @param formateType
	 *            日期显示类型
	 * @return Date对象
	 */
	public static Date string2DateByFromateType(String str, String formateType) {
		Date date = null;
		try {
			date = new SimpleDateFormat(formateType).parse(str);
		} catch (ParseException e) {
			XLog.e("Exception while parsing string date", e);
		}
		return date;
	}

	/**
	 * @brief 将时间字串转换为Date类型 Date包含日期和时间
	 * @param str
	 *            要转换的字串
	 * @return 返回日期类型
	 */
	public static Date string2LongDate(String str) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd HH:mm").parse(str);
		} catch (ParseException e) {
			XLog.e("Exception while parsing string date", e);
		}
		return date;
	}
	
	/**
	 * @brief 将long类型的时间转成要求格式显示
	 * @param context
	 *            上下文文本对象
	 * @param longTime
	 *            long类型时间
	 * @return String 转换后的时间
	 */
	public static String parseLongTimeToString(Context context, long longTime) {
		String result;
		// 将传入的long类型时间转成Calendar对象
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(longTime);
		// 将当前时间转成Calendar对象
		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.setTimeInMillis(System.currentTimeMillis());

		// 分别获取相应的年、月、日
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int todayYear = todayCalendar.get(Calendar.YEAR);
		int todayMonth = todayCalendar.get(Calendar.MONTH);
		int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);

		// 如果年月日都相同，表示今天，则显示 时：分
		if (year == todayYear && month == todayMonth && day == todayDay) {
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			// 如果为个位数，则在前面加0
			result = ((hour > 9) ? hour : "0" + hour) + ":"
					+ (minute > 9 ? minute : "0" + minute);
			// 如果day+1==todayDay，则表示昨天，显示为昨天
		} else {
			result = (month + 1) + "-" + day;
		}
		// 返回转换后的形式
		return result;
	}

	
	/**
	 * @brief 以秒为单位返回当前系统时间
	 */ 
	public static int getCurrentTimeSeconds(){
		return (int)(System.currentTimeMillis()/1000);
	}
		
	/**
	 * @brief 以秒为单位返回今天0点时分的秒数
	 * **/
	public static long getToday00Time(){
		
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, 0);    
		cal.set(Calendar.SECOND, 0);   
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
}

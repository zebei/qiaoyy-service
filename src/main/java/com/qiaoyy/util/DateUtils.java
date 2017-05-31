package com.qiaoyy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 2017/4
 *
 */
public class DateUtils {
	/**
	 *  DATE PATTERN 
	 */
    public static final String PATTERN_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_YMDH = "yyyy-MM-dd HH";
    public static final String PATTERN_YMD = "yyyy-MM-dd";
    public static final String PATTERN_YM = "yyyy-MM";
    public static final String PATTERN_Y = "yyyy";
    
	/**
	 * 时间转换成字符串
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}
	
	/**
     * 从字符串解析为日期型
     * @param str
     * @param format
     * @return
     */
    public static Date parse(final String str, final String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {}
        return null;
    }
    
    /**
     * 增加、减少指定天数
     * 
     * @param date
     * @param day
     *            要增加的天数（减少则为 负数）
     * @return
     */
    public static Date addDay(final Date date, int day) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, day);
            return c.getTime();
    }
}

/**
 * ProjectName:test_day
 * PackageName:com.example.test_day
 * FileNmae:DateHepler.java
 */
package cn.sbx.deeper.moblie.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期辅助类
 * 
 * @author 王克
 * 
 */
public class DateHepler {
	
	private static String mYear;  
    private static String mMonth;  
    private static String mDay;  
    private static String mWay;
	/**
	 * 取本周7天的第一天（周一的日期）
	 */
	public static String getNowWeekBegin(Calendar calendar) {
		int mondayPlus;
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			mondayPlus = 0;
		} else {
			mondayPlus = 1 - dayOfWeek;
		}
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();

		// DateFormat df = DateFormat.getDateInstance();
		// String preMonday = df.format(monday);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String preMonday = sdf.format(monday);

		return preMonday;
	}

	public static String getFirstDayOfNextWeek(Calendar calendar) {
		calendar.add(Calendar.WEEK_OF_YEAR, 1);// 增加一个星期
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {// 如果当前不是星期一
			calendar.add(Calendar.DATE, -1);// 减一天
		}

		// DateFormat df = DateFormat.getDateInstance();
		// String preMonday = df.format(calendar.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String preMonday = sdf.format(calendar.getTime());

		return preMonday;// 返回Date对象
	}

	public static String getLastDayOfNextWeek(Calendar calendar) {
		// 获取上周一日期
		calendar.add(Calendar.WEEK_OF_MONTH, -1);
		calendar.set(Calendar.DAY_OF_WEEK, 2);
		// DateFormat df = DateFormat.getDateInstance();
		// String preMonday = df.format(calendar.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String preMonday = sdf.format(calendar.getTime());

		return preMonday;// 返回Date对象
	}

	/**
	 * 获取当前日期
	 * 
	 * @param
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTodayDate() {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String todayDate = formatter.format(curDate);

		return todayDate;
	}

	public static String[] getStringDate(String date) throws ParseException {
		String[] weeks = new String[7];// 返回的这周的日期
		String[] a = date.split("-");
		int week = getDayOfWeek(a[0], a[1], a[2]);// 获取周几
		int minWeek = 0;
		int maxWeek = 7;
		String format = "yyyy-MM-dd";

		if (week == 1) {// 如果是周日（老外是从周日开始算一周，所以有点恶心）
			weeks[6] = date;
			for (int i = 5; i >= 0; i--) {
				weeks[i] = getFormatDateAdd(getStrToDate(date, format), -1,
						format);
				date = weeks[i];
			}
		} else {
			int temp = week - 2;
			weeks[temp] = date;
			for (int i = temp - 1; i >= minWeek; i--) {
				weeks[i] = getFormatDateAdd(getStrToDate(date, format), -1,
						format);
				date = weeks[i];
			}
			date = weeks[temp];
			for (int i = temp + 1; i < maxWeek; i++) {
				weeks[i] = getFormatDateAdd(getStrToDate(date, format), 1,
						format);
				date = weeks[i];
			}
		}

		return weeks;
	}

	/**
	 * 根据指定的年、月、日返回当前是星期几。1表示星期天、2表示星期一、7表示星期六。
	 * 
	 * @param year
	 * @param month
	 *            month是从1开始的12结束
	 * @param day
	 * @return 返回一个代表当期日期是星期几的数字。1表示星期天、2表示星期一、7表示星期六。
	 */
	public static int getDayOfWeek(String year, String month, String day) {
		Calendar cal = new GregorianCalendar(new Integer(year).intValue(),
				new Integer(month).intValue() - 1, new Integer(day).intValue());
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 取得给定日期加上一定天数后的日期对象.
	 * 
	 * @param date
	 *            给定的日期对象
	 * @param amount
	 *            需要添加的天数，如果是向前的天数，使用负数就可以.
	 * @param format
	 *            输出格式.
	 * @return Date 加上一定天数以后的Date对象.
	 */
	public static String getFormatDateAdd(Date date, int amount, String format) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(GregorianCalendar.DATE, amount);
		return getFormatDateTime(cal.getTime(), format);
	}

	/**
	 * 根据给定的格式与时间(Date类型的)，返回时间字符串。最为通用。<br>
	 * 
	 * @param date
	 *            指定的日期
	 * @param format
	 *            日期格式字符串
	 * @return String 指定格式的日期字符串.
	 */
	public static String getFormatDateTime(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 返回制定日期字符串的date格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date getStrToDate(String date, String format)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}
	public static String getCurrentDate() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return dateFormatter.format(date);
	}
	
	 public static String StringData(){  
	        final Calendar c = Calendar.getInstance();  
	        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
	        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份  
	        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份  
	        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码  
	        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  
	        if("1".equals(mWay)){  
	            mWay ="天";  
	        }else if("2".equals(mWay)){  
	            mWay ="一";  
	        }else if("3".equals(mWay)){  
	            mWay ="二";  
	        }else if("4".equals(mWay)){  
	            mWay ="三";  
	        }else if("5".equals(mWay)){  
	            mWay ="四";  
	        }else if("6".equals(mWay)){  
	            mWay ="五";  
	        }else if("7".equals(mWay)){  
	            mWay ="六";  
	        }  
//	        return mYear + "年" + mMonth + "月" + mDay+"日"+"/星期"+mWay;  
	        return "星期"+mWay;
	    }  
}

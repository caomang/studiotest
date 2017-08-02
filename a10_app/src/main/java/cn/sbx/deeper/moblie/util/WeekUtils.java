package cn.sbx.deeper.moblie.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WeekUtils {

	private WeekUtils(){}
	
	private static WeekUtils instance = new WeekUtils();
	
	public static WeekUtils getInstace() {
		return instance;
	}
	
	private static int weeks = 0;
	private static DateFormat df=new SimpleDateFormat("yyyy-MM-dd");   
    // 获得当前日期与本周一相差的天数
    private int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期一是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    // 获得上周星期一的日期
    public String getPreviousMonday() {
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
//        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        weeks++;
        return preMonday;
    }
    
 // 获得上周星期二的日期
    public String getPreviousTuesday() {
    	String monday = getPreviousMonday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
    
 // 获得上周星期三的日期
    public String getPreviousWendesday() {
    	String monday = getPreviousTuesday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得上周星期四的日期
    public String getPreviousThursday() {
    	String monday = getPreviousWendesday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得上周星期五的日期
    public String getPreviousFriday() {
    	String monday = getPreviousThursday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得上周星期六的日期
    public String getPreviousSaturday() {
    	String monday = getPreviousFriday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得上周星期日的日期
    public String getPreviousSunday() {
    	String monday = getPreviousSaturday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
    
 // 获得本周星期一的日期
    public String getCurrentMonday() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
//        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    
 // 获得本周星期二的日期
    public String getCurrentTuesday() {
    	String monday = getCurrentMonday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得本周星期三的日期
    public String getCurrentWendesday() {
    	String monday = getCurrentTuesday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得本周星期四的日期
    public String getCurrentThursday() {
    	String monday = getCurrentWendesday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得本周星期五的日期
    public String getCurrentFriday() {
    	String monday = getCurrentThursday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得本周星期六的日期
    public String getCurrentSaturday() {
    	String monday = getCurrentFriday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得本周星期日的日期
    public String getCurrentSunday() {
    	String monday = getCurrentSaturday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }

    // 获得下周星期一的日期
    public String getNextMonday() {
        weeks++;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
//        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        weeks--;
        return preMonday;
    }
    
 // 获得下周星期二的日期
    public String getNextTuesday() {
    	String monday = getNextMonday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
    
 // 获得下周星期三的日期
    public String getNextWendesday() {
    	String monday = getNextTuesday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得下周星期四的日期
    public String getNextThursday() {
    	String monday = getNextWendesday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得下周星期五的日期
    public String getNextFriday() {
    	String monday = getNextThursday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得下周星期六的日期
    public String getNextSaturday() {
    	String monday = getNextFriday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
 // 获得下周星期日的日期
    public String getNextSunday() {
    	String monday = getNextSaturday();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
    	try {
			date = sdf.parse(monday);
			date = new Date(date.getTime() + 24*3600*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return sdf.format(date);
    }
}

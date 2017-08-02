package cn.sbx.deeper.moblie.util;

public class DateUtils {
	/**
	 * 设定显示文字
	 */
	public static String getInterval(int time) {
		String txt = null;
		if (time >= 0) {
			long day = time / (24 * 3600);// 天
			long hour = time % (24 * 3600) / 3600;// 小时
			long minute = time % 3600 / 60;// 分钟
			long second = time % 60;// 秒
			// txt = " 剩余时间：" + day + "天" + hour + "小时" + minute + "分" + second
			// + "秒";
			txt = " 剩余时间：" + hour + "小时" + minute + "分" + second + "秒";
		} else {
			txt = "已过期";
		}
		return txt;
	}

	/**
	 * 获取提前提醒的秒数
	 */
	public static int getAlarmTiqian(String tiqian) {
		int time = 0;
		time = Integer.valueOf(tiqian) * 60;
		return time;
	}
}

package cn.sbx.deeper.moblie.util;

import android.util.Log;

/**
 * 封装Log工具类
 * @author Administrator
 *
 */
public class LogUtil {
	private static boolean isDebug = true;//当开发完毕需要置为false
	
	/**
	 * 打印i级别log
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg){
		if(isDebug){
			Log.i(tag, msg);
		}
	}
	/**
	 * 打印i级别log
	 * @param tag
	 * @param msg
	 */
	public static void i(Object object,String msg){
		if(isDebug){
			Log.i(object.getClass().getSimpleName(), msg);
		}
	}
	
	/**
	 * 打印e级别log
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg){
		if(isDebug){
			Log.e(tag, msg);
		}
	}
	
	/**
	 * 打印e级别log
	 * @param //tag
	 * @param msg
	 */
	public static void e(Object object,String msg){
		if(isDebug){
			Log.e(object.getClass().getSimpleName(), msg);
		}
	}
}

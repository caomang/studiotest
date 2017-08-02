package cn.sbx.deeper.moblie.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 
 * @author terry.C
 * the class manage vpn util
 */
public class VpnUtils {

	public static String vpnHost = "vpn.sinopec.com";
	public static int vpnPort = 443;
	
	/**
	 * 判断网络是否连接
	 * 
	 * @return
	 */
	public static boolean isConnect(Context mContext) {
		// 获取手机所有连接管理对象（包括对wifi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log.e("异常", "网络连接异常");
		}
		return false;
	}
}

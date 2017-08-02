package cn.sbx.deeper.moblie.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityManagerWrapper {
	private Context context;

	public ConnectivityManagerWrapper(Context context) {
		this.context = context;
	}

	/**
	 * 检测网络链接是否可用<br/>
	 * 
	 * 使用权限：android.permission.ACCESS_NETWORK_STATE
	 * 
	 * @return
	 */
	public boolean isNetworkReachable() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo currNetworkInfo = cm.getActiveNetworkInfo();
		if (currNetworkInfo == null)
			return false;
		return (currNetworkInfo.getState() == NetworkInfo.State.CONNECTED);
	}

	/**
	 * 检测网络类型
	 * 
	 * @return
	 */
	public int checkConnectionType() {
		ConnectivityManager mManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo current = mManager.getActiveNetworkInfo();
		if (current == null) {
			return -1;
		}
		return current.getType();
	}

}
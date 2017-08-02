package cn.sbx.deeper.moblie.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class JudgeNet {
	public static boolean isNetworkConnected(Context context) {  
	    if (context != null) {  
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	        if (mNetworkInfo != null) {  
	            return mNetworkInfo.isAvailable();  
	        }  
	    }  
	    return false;  
	}
	
	public static boolean isNull(String msg) { 
		if(msg==null){
			return true;
		}else return msg.toLowerCase() == "null" || "".equals(msg) || msg.length() == 0;
	}
	
}

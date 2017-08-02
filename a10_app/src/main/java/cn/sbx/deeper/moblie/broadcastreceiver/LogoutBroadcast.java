package cn.sbx.deeper.moblie.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.sbx.deeper.moblie.util.WebUtils;

public class LogoutBroadcast extends BroadcastReceiver {
	public static final String ACTION_LOGINOUT_SUCCESS = "com.zed3.sipua.loginout_success";
	public static final String LOGINOUT_STATUS = "loginoutstatus";
	private boolean b = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_LOGINOUT_SUCCESS)) {
			Bundle bundle = intent.getExtras();
			b = bundle.getBoolean(LOGINOUT_STATUS);
			if (b = true) {
				WebUtils.loginStatus = false;
				System.out.println("-------集群通注销成功----------");
			} else {
				System.out.println("-------集群通注销失败----------");
			}
		}
	}

}

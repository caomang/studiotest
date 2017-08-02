package cn.sbx.deeper.moblie.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.sbx.deeper.moblie.util.WebUtils;

public class LoginBroadcast extends BroadcastReceiver {
	public static final String ACTION_LOGIN_SUCCESS = "com.zed3.sipua.login_success";
	public static final String LOGIN_STATUS = "loginstatus";
	public static final String LOGIN_FAIL_RESULT = "fail";
	private String messageString = "";
	private boolean b = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_LOGIN_SUCCESS)) {
			Bundle bundle = intent.getExtras();
			b = bundle.getBoolean(LOGIN_STATUS);
			messageString = bundle.getString(LOGIN_FAIL_RESULT);
			if (b = true) {
				System.out.println("-------集群通登陆成功----------");
				WebUtils.loginStatus = true;
			} else {
				System.out.println("-------集群通登陆失败----------" + messageString);
			}
		}

	}

}

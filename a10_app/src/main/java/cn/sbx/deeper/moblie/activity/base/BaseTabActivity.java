package cn.sbx.deeper.moblie.activity.base;

import android.app.TabActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;

import cn.sbx.deeper.moblie.MobileApplication;


@SuppressWarnings("deprecation")
public class BaseTabActivity extends TabActivity {
	public Display display = null;
	public SharedPreferences sp;
	MobileApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		display = getWindowManager().getDefaultDisplay();
		sp = getSharedPreferences("sys_config", Context.MODE_PRIVATE);
		application = MobileApplication.getInstance();
		application.activityStack.add(this);
	}
	public void initMainView() {
		
	}
    
}

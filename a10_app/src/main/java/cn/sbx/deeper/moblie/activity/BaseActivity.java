package cn.sbx.deeper.moblie.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import cn.sbx.deeper.moblie.MobileApplication;

/**
 * 所有activity的基类，处理所有activity共同的事件
 * 
 */
public class BaseActivity extends FragmentActivity {
	protected int screenWidth;
	protected int screenHeight;
	MobileApplication application;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		application = MobileApplication.getInstance();
		application.activityStack.add(this);
	}
}

package cn.sbx.deeper.moblie.broadcastreceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import petrochina.ghzy.a10fieldwork.R;

public class BroadcastReceiverUpdate extends BroadcastReceiver {
	public static final String countAction = "com.sunbox.mobile.office.alert";
	SharedPreferences sps;

	AlertDialog softUpdateDialog;

	@Override
	public void onReceive(Context mContext, Intent intent) {
		// TODO Auto-generated method stub
		sps = PreferenceManager.getDefaultSharedPreferences(mContext);
		if (countAction.equals(intent.getAction())) {
			String path = intent.getStringExtra("path");
			String updateLog = intent.getStringExtra("updateLog");
			dialogUpdate(mContext, updateLog, path);
		}
	}

	private void dialogUpdate(final Context mContext, final String updateLog, final String path) {
		// 如果本地已经下载完了包
		LayoutInflater factoryHis = LayoutInflater.from(mContext);// 提示框
		View viewDialog = null;
		viewDialog = factoryHis.inflate(R.layout.dialog_update_layout, null);
		softUpdateDialog = new AlertDialog.Builder(mContext).setView(viewDialog).setCancelable(false).create();
		Button btn_dialog_ok = (Button) viewDialog.findViewById(R.id.btn_dialog_ok);
		btn_dialog_ok.setText("开始安装");
		TextView textView = (TextView) viewDialog.findViewById(R.id.updatecontent);
		textView.setText(updateLog);
		btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				installApk(mContext, new File(path));
				sps.edit().putString("localApk", "").commit();
				softUpdateDialog.dismiss();
			}
		});
		Button btn_dialog_cancel = (Button) viewDialog.findViewById(R.id.btn_dialog_cancel);
		btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				softUpdateDialog.dismiss();
			}
		});
		ButtonHandler buttonHandler = new ButtonHandler(softUpdateDialog);
		// 设定对话框的处理Handler
		try {
			Field field = softUpdateDialog.getClass().getDeclaredField("mAlert");
			field.setAccessible(true);
			// 获得mAlert变量的值
			Object obj = field.get(softUpdateDialog);
			field = obj.getClass().getDeclaredField("mHandler");
			field.setAccessible(true);
			// 修改mHandler变量的值，使用新的ButtonHandler类
			field.set(obj, buttonHandler);
		} catch (Exception e) {
		}
		// 显示对话框
		Message msg = new Message();
		msg.what = 0;
		msg.setTarget(new MHandler());
		msg.sendToTarget();
	}

	// 定义按钮的响应,覆写系统的默认处理
	class ButtonHandler extends Handler {

		private WeakReference<DialogInterface> mDialog;

		public ButtonHandler(DialogInterface dialog) {
			mDialog = new WeakReference<DialogInterface>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
				break;
			}
		}
	}

	class MHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				softUpdateDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				softUpdateDialog.show();// 显示对话框
				break;
			}
			super.handleMessage(msg);
		}

	}

	// 安装apk
	protected void installApk(Context mContext, File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		mContext.startActivity(intent);
		((Activity) mContext).finish();
	}
}

package cn.sbx.deeper.moblie.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.sbx.deeper.moblie.MobileApplication;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 提示对话框帮助类
 * 
 * @author f.jiang
 * 
 */
public class AlertUtils {
	static final String mTag = "AlertUtils";

	private AlertUtils() {
		// deny
	}

	/**
	 * 警告对话框
	 * 
	 * @param context
	 *            不解释
	 * @param message
	 *            对话框显示消息
	 * 
	 * @author f.jiang
	 */
	// public static void showWarning(final Context context, final String
	// message) {
	// CharSequence title = context.getString(R.string.warning_title);
	// CharSequence buttonText = context.getString(R.string.btn_ok_title);
	// Builder builder = new Builder(context);
	// builder.setTitle(title).setIcon(R.drawable.dg_warning)
	// .setMessage(message).setPositiveButton(buttonText, null);
	// builder.create().show();
	// }

	/**
	 * 确认对话框
	 * 
	 * @param context
	 *            不解释
	 * @param title
	 *            对话框标题
	 * @param message
	 *            对话框显示消息
	 * @param positiveText
	 *            确定按钮显示文字
	 * @param negativeText
	 *            取消按钮显示文字
	 * @param positiveListener
	 *            确定按钮响应事件
	 * @param negativeListener
	 *            取消按钮显示事件
	 * 
	 * @author f.jiang
	 */
	public static void showConfirm(final Context context, final String title,
			final String message, final String positiveText,
			final String negativeText, final OnClickListener positiveListener,
			final OnClickListener negativeListener) {
		AlertDialog.Builder buider = new AlertDialog.Builder(context);
		buider.setTitle(title);
		buider.setMessage(message);
		buider.setPositiveButton(positiveText, positiveListener);
		buider.setNegativeButton(negativeText, negativeListener);
		buider.create().show();
	}

	// /**
	// * 检查网络状态，如果网络不联通，则提示用户进行设置，其中设置选项有：
	// * <li>设置网络</li>跳转到网络设置界面，进行网络设置
	// * <li>退出系统</li>退出当前系统
	// */
	// public static void showNetworkState(final Context context) {
	// AlertDialog.Builder ab = new AlertDialog.Builder(context);
	// ab.setTitle(R.string.net_abnormal);
	// ab.setMessage(R.string.net_abnormal_set);
	// ab.setPositiveButton(R.string.net_set, new
	// DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// context.startActivity(new Intent(
	// android.provider.Settings.ACTION_WIRELESS_SETTINGS));
	// dialog.cancel();
	// ((Activity)context).finish();
	// }
	// });
	// ab.setNegativeButton(R.string.exit_system, new
	// DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// android.os.Process.killProcess(android.os.Process.myPid());
	// }
	// });
	// ab.create().show();
	// }

	// public static DialogFragment showDialog(FragmentActivity fActivity,
	// String message, AsyncTask<?, ?, ?> asynTask, boolean isRunning) {
	// FragmentTransaction ft = fActivity.getSupportFragmentManager()
	// .beginTransaction();
	// OverlayProgressFragment overlayProgressFragment =
	// OverlayProgressFragment.newInstance();
	// overlayProgressFragment.setAsynTask(asynTask);
	// overlayProgressFragment.setMessage(message);
	// overlayProgressFragment.show(ft, message);
	// return overlayProgressFragment;
	// }
	static ImageView imageView;

	public static ProgressHUD showDialog(Context context, CharSequence message,
			final AsyncTask<?, ?, ?> asynTask, boolean cancelable) {
		if(MobileApplication.netType == 0){
			Toast.makeText(context, R.string.no_available_net, Toast.LENGTH_LONG).show();
		}
		final ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
		dialog.setTitle("");
		dialog.setContentView(R.layout.progress_hud);
		imageView = (ImageView) dialog.findViewById(R.id.spinnerImageView);
		Animation operatingAnim = AnimationUtils.loadAnimation(context,
				R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			imageView.startAnimation(operatingAnim);
		}

		if (message == null || message.length() == 0) {
			dialog.findViewById(R.id.message).setVisibility(View.GONE);
		} else {
			TextView txt = (TextView) dialog.findViewById(R.id.message);
			txt.setText(message);
		}
//		DisplayMetrics dm = new DisplayMetrics();
//
//		((Activity) context).getWindowManager().getDefaultDisplay()
//				.getMetrics(dm);
//
//		Rect rect = new Rect();
//
//		View view = ((Activity) context).getWindow().getDecorView();
//
//		view.getWindowVisibleDisplayFrame(rect);
//		LayoutParams lay = dialog.getWindow().getAttributes();
//
//		lay.height = dm.heightPixels - rect.top;
//
//		lay.width = dm.widthPixels;

		dialog.setCanceledOnTouchOutside(true);
		// dialog.setCancelable(!cancelable);
		dialog.setOnCancelListener(null);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.2f;
//		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
//		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface arg0, int keyCode,
//					KeyEvent arg2) {
//				// TODO Auto-generated method stub
//				if (keyCode == KeyEvent.KEYCODE_BACK) {
//					System.out.println("9999999999999");
//					if (asynTask != null) {
//						asynTask.cancel(true);
//					}
//					dialog.dismiss();
//					return true;
//				} else if (keyCode == KeyEvent.KEYCODE_MENU) {
//					return true;
//				}
//				return false;
//			}
//		});

		return dialog;
	}

	public static ProgressHUD showDialog1(Context context, CharSequence message,
										 final AsyncTask<?, ?, ?> asynTask, boolean cancelable) {
		final ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
		dialog.setTitle("");
		dialog.setContentView(R.layout.progress_hud);
		imageView = (ImageView) dialog.findViewById(R.id.spinnerImageView);
		Animation operatingAnim = AnimationUtils.loadAnimation(context,
				R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			imageView.startAnimation(operatingAnim);
		}

		if (message == null || message.length() == 0) {
			dialog.findViewById(R.id.message).setVisibility(View.GONE);
		} else {
			TextView txt = (TextView) dialog.findViewById(R.id.message);
			txt.setText(message);
		}
//		DisplayMetrics dm = new DisplayMetrics();
//
//		((Activity) context).getWindowManager().getDefaultDisplay()
//				.getMetrics(dm);
//
//		Rect rect = new Rect();
//
//		View view = ((Activity) context).getWindow().getDecorView();
//
//		view.getWindowVisibleDisplayFrame(rect);
//		LayoutParams lay = dialog.getWindow().getAttributes();
//
//		lay.height = dm.heightPixels - rect.top;
//
//		lay.width = dm.widthPixels;

		dialog.setCanceledOnTouchOutside(true);
		// dialog.setCancelable(!cancelable);
		dialog.setOnCancelListener(null);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.2f;
//		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
//		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface arg0, int keyCode,
//					KeyEvent arg2) {
//				// TODO Auto-generated method stub
//				if (keyCode == KeyEvent.KEYCODE_BACK) {
//					System.out.println("9999999999999");
//					if (asynTask != null) {
//						asynTask.cancel(true);
//					}
//					dialog.dismiss();
//					return true;
//				} else if (keyCode == KeyEvent.KEYCODE_MENU) {
//					return true;
//				}
//				return false;
//			}
//		});

		return dialog;
	}

	
	public static ProgressHUD showDialogSingle(Context context, CharSequence message,
			final AsyncTask<?, ?, ?> asynTask, boolean cancelable) {
		if(MobileApplication.netType == 0){
			Toast.makeText(context, R.string.no_available_net, Toast.LENGTH_LONG).show();
		}
		final ProgressHUD dialog = ProgressHUD.getInstance(context, R.style.ProgressHUD);
		dialog.setTitle("");
		dialog.setContentView(R.layout.progress_hud);
		imageView = (ImageView) dialog.findViewById(R.id.spinnerImageView);
		Animation operatingAnim = AnimationUtils.loadAnimation(context,
				R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			imageView.startAnimation(operatingAnim);
		}

		if (message == null || message.length() == 0) {
			dialog.findViewById(R.id.message).setVisibility(View.GONE);
		} else {
			TextView txt = (TextView) dialog.findViewById(R.id.message);
			txt.setText(message);
		}
//		DisplayMetrics dm = new DisplayMetrics();
//
//		((Activity) context).getWindowManager().getDefaultDisplay()
//				.getMetrics(dm);
//
//		Rect rect = new Rect();
//
//		View view = ((Activity) context).getWindow().getDecorView();
//
//		view.getWindowVisibleDisplayFrame(rect);
//		LayoutParams lay = dialog.getWindow().getAttributes();
//
//		lay.height = dm.heightPixels - rect.top;
//
//		lay.width = dm.widthPixels;

		dialog.setCanceledOnTouchOutside(true);
		// dialog.setCancelable(!cancelable);
//		dialog.setOnCancelListener(null);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.2f;
//		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		if(dialog!=null){
			dialog.show();
		}
		return dialog;
	}


/*	public static DialogFragment showApproveDetailForm(
			FragmentActivity fActivity, String message, Bundle bundle) {
		FragmentTransaction ft = fActivity.getSupportFragmentManager()
				.beginTransaction();
		ApproveDetaiFromFragment adf = ApproveDetaiFromFragment
				.newInstance(bundle);
		adf.setMessage(message);
		adf.show(ft, message);
		return adf;
	}*/

	// public static android.app.ProgressHUD showDialogActivity(Activity
	// activity, String message, AsyncTask<?, ?, ?> asynTask, boolean isRunning)
	// {
	// android.app.FragmentTransaction ft = activity.getFragmentManager()
	// .beginTransaction();
	// OverlayProgressActivity overlayProgressFragment =
	// OverlayProgressActivity.newInstance();
	// overlayProgressFragment.setAsynTask(asynTask);
	// overlayProgressFragment.show(ft, message);
	// return overlayProgressFragment;
	// }

}

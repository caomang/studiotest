package cn.sbx.deeper.moblie.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.SinopecMenu;
import cn.sbx.deeper.moblie.domian.SoftInfo;
import cn.sbx.deeper.moblie.logic.MainLoadingListener;
import cn.sbx.deeper.moblie.logic.MainProcess;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.MenuType;
import cn.sbx.deeper.moblie.util.MenuTypeUtil;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 获取菜单信息并进行菜单选择的中间组件
 * 
 * @author terry.C
 * 
 */
public class SwitchMenuActivity extends BaseActivity implements
		MainLoadingListener {
	private static final String TAG = SwitchMenuActivity.class.getSimpleName();
	private ProgressHUD overlayProgress;
	Context mContext;
	boolean hasCalled = false;
	public static SoftInfo info;
	String warningPushUsername;
	String warningPushPassword;

	public SwitchMenuActivity() {
		mContext = SwitchMenuActivity.this;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		DataCache.numsOfMoudleHasNotification = 0;
		// 这里获取布局的样式
		Intent intent = getIntent();
		if (intent != null) {
			warningPushUsername = intent.getStringExtra("warningPushUsername");
			warningPushPassword = intent.getStringExtra("warningPushPassword");
		}
		MainProcess mainProcess = new MainProcess();
		mainProcess.initState(this, this);// 传过去上下文， 和MainLoadingListener接口
		overlayProgress = AlertUtils.showDialog(SwitchMenuActivity.this,
				getString(R.string.msg_switch_loading), null, false);
		getWindow().setBackgroundDrawableResource(R.drawable.ic_login_bg);
	}

	@Override
	public void checkUpdate(SoftInfo softInfo) {
		// String version = getVersionName();
		// Log.i(TAG, "begin check update" + softInfo.getVersion() + "======="
		// +version);
		// info = softInfo;
		// if(softInfo != null) {
		// softInfo.setLastVersion(VersionUtils.getVersionName());
		// Intent i = new Intent(this, UpdateSoftActivity.class);
		// i.putExtra("info", (Serializable) softInfo);
		// startActivity(i);
		// if(Float.parseFloat(version) <
		// Float.parseFloat(softInfo.getVersion())){
		// showUpdateDialog(softInfo);
		// }
		// }
		// if(hasCalled) {
		// finish();
		// }else {
		// hasCalled = true;
		// }
		// Log.i(TAG, "end check update");
	}

	@Override
	public void initMainView() {
		if (overlayProgress != null)
			overlayProgress.dismiss();
		SinopecMenu menu = DataCache.sinopecMenu;
		MenuType mt = MenuTypeUtil.chooseMenuType(menu.layout);
		if (mt != null) {
			switch (mt) {
			case TOP:
				Intent topIntent = new Intent(mContext, MainActivity.class);
				topIntent.putExtra("type", "top");
				topIntent.putExtra("info", info);
				startActivity(topIntent);
				break;
			case BOTTOM:
				Intent bottomIntent = new Intent(mContext, MainActivity.class);
				bottomIntent.putExtra("type", "bottom");
				bottomIntent.putExtra("info", info);
				startActivity(bottomIntent);
				break;
			case SQUARED:
				/*Intent squaredIntent = new Intent(mContext,
						SinopecSquaredActivity.class);
				startActivity(squaredIntent);*/
				break;
			case LEFT:
//				Intent leftIntent = new Intent(mContext,
//						SinopecLeftMenuActivity.class);
//				startActivity(leftIntent);
				break;
			case ALL:
				DataCache.layoutType = "all";
				Intent allIntent = new Intent(mContext,
						SinopecAllMenuActivity.class).putExtra(
						"warningPushUsername", warningPushUsername).putExtra(
						"warningPushPassword", warningPushPassword);
				startActivity(allIntent);
				break;
			default:
				break;
			}
		}
		finish();
		// if(hasCalled) {
		// finish();
		// }else {
		// hasCalled = true;
		// }
	}

	private void showUpdateDialog(SoftInfo info) {
		// sp.edit().putBoolean("updateLogRead", false).commit();
		Builder builer = new Builder(mContext);
		builer.setTitle("更新提示"
				+ ("1".equals(info == null ? "0" : info.getForced()) ? " : 需要立即更新"
						: ""));
		if (!"".equals(info.getUpdateLog().trim())) {
			builer.setMessage(info.getUpdateLog());
		}
		builer.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// downLoadApk();
			}
		});
		if ("0".equals(info == null ? "0" : info.getForced())) {
			builer.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
		}
		AlertDialog dialog = builer.create();
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		if ("1".equals(info == null ? "0" : info.getForced())) {
			dialog.setCancelable(false);
		}
		dialog.show();
	}

	// 从服务器下载APK
	protected void downLoadApk() {
		new AsyncTask<Void, Integer, File>() {
			ProgressDialog pd1 = null;
			private volatile boolean running = true;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd1 = new ProgressDialog(mContext);
				pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd1.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						cancel(true);
						finish();
					}
				});
				if ("1".equals(info == null ? "0" : info.getForced())) {
					pd1.setCancelable(false);
				}
				pd1.setTitle("提示");
				pd1.setMessage("正在下载更新,请稍候..");
				pd1.show();
			}

			@Override
			protected void onPostExecute(File result) {
				super.onPostExecute(result);
				pd1.dismiss();
				if (result != null) {
					// 安装APK
					installApk(result);
				} else {
					Toast.makeText(mContext, "下载失败,请检查网络连接", Toast.LENGTH_SHORT).show();
					finish();
				}
			}

			protected void onCancelled() {
				running = false;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				pd1.setProgress(values[0]);
			}

			@Override
			protected File doInBackground(Void... params) {
				try {
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						URL url = new URL((info.getDownloadUrl()));
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setConnectTimeout(10000);
						InputStream is = conn.getInputStream();
						System.out.println("conn.getContentLength():"
								+ conn.getContentLength());
						pd1.setMax(conn == null ? 100 : conn.getContentLength());
						File file = new File(
								Environment.getExternalStorageDirectory(),
								Constants.home_cache + "/oa/MobieOffice.apk");
						FileOutputStream fos = new FileOutputStream(file);
						BufferedInputStream bis = new BufferedInputStream(is);

						byte[] buffer = new byte[1024];
						int len;
						int total = 0;
						while ((len = bis.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
							total += len;
							publishProgress(total);
						}
						fos.close();
						bis.close();
						is.close();
						if (!running) {
							return null;
						}
						return file;
					} else {
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}.execute();
	}

	// 安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
		finish();
	}

	private String getVersionName() {
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}

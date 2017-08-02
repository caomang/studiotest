package cn.sbx.deeper.moblie.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sbx.deeper.moblie.contrants.Constants;
import petrochina.ghzy.a10fieldwork.R;

/**
 * download attachement util
 * 
 * @author terry.C
 * 
 */
public class DownloadUtils {

	public static ProgressDialog _pd;

	public static void showProgress(String title, String message,
			final AsyncTask<?, ?, ?> task, Activity mContext) {
		if (_pd == null) {
			_pd = ProgressDialog.show(mContext, title, message);
			_pd.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (task != null
							&& task.getStatus() == AsyncTask.Status.RUNNING) {
						task.cancel(true);
						System.out.println("Task[" + task + "] is cancelled");
					}
				}
			});
		} else
			_pd.show();
	}

	public static void dismissProgress() {
		if (_pd != null && _pd.isShowing()) {
			_pd.cancel();
			_pd = null;
		}
	}

	public static void downloadIntranetFile(final Activity mContext,
			final String filePath, final String serverPath) {
		new AsyncTask<String, Void, String>() {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				dismissProgress();

				if (result != null) {
					// 获取到信的pdf地址
					// readPdf(Uri.fromFile(result));"http://adasd.doc" 不支持此格式文件
					System.out.println("result:" + result);
					String extraName = result.substring(result.lastIndexOf('.') + 1);
					if(!Constants.attachmentTypesList.contains(extraName)){
						Toast.makeText(mContext, mContext.getString(R.string.attachment_not_support), Toast.LENGTH_SHORT).show();
					}else
					downLoadRealPdf(result, mContext);
				} else {
					// 下载失败
					Toast.makeText(mContext, "解析失败，请检查网络", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgress("提示", "正在解析地址...", this, mContext);
			}

			@Override
			protected String doInBackground(String... params) {
				try {
					return ConnectionManager.getHttpGetPDFUrl(filePath, serverPath);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}.execute(filePath);
	}

	private static void readPdf(Uri uri, String type, final Activity mContext) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(/* uri */uri, type);
		try {
			mContext.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			// No application to view, ask to download one
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("文件打开失败");
			builder.setMessage("没有找到合适的文件阅读器，是否到Android Market下载一个？");
			builder.setPositiveButton("立刻访问",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent marketIntent = new Intent(Intent.ACTION_VIEW);
							marketIntent.setData(Uri
									.parse("market://details?id=com.adobe.reader"));
							mContext.startActivity(marketIntent);
						}
					});
			builder.setNegativeButton("暂不访问", null);
			builder.create().show();
		}
	}

	public static void downLoadRealPdf(String path, final Activity mContext) {
		new AsyncTask<String, Void, File>() {
			@Override
			protected void onPostExecute(File result) {
				super.onPostExecute(result);
				dismissProgress();
				if (result != null) {
					// 获取到信的pdf地址
					String type = MimeTypeMap.getSingleton()
							.getMimeTypeFromExtension(
									MimeTypeMap.getFileExtensionFromUrl(result
											.getAbsolutePath()));
					readPdf(Uri.fromFile(result), type, mContext);
				} else {
					// 下载失败
					Toast.makeText(mContext, "下载失败，请检查网络",Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgress("提示", "正在下载附件...", this, mContext);
			}

			@Override
			protected File doInBackground(String... params) {
				String path = params[0];
				// System.out.println("downLoadPath:" + path);
				try {
					File file = null;
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
//					HttpURLConnection conn = ProxyCheck.myHttpURLConnection(path);
					conn.setConnectTimeout(10*60*1000);
					InputStream in = conn.getInputStream();

					String fileName = "";
					fileName = url.getFile();
					String extraName = fileName.substring(fileName
							.lastIndexOf('.') + 1);
					// System.out.println("fileName + extraName :" + fileName);
					file = new File(Environment.getExternalStorageDirectory(),
							Constants.home_cache+"/oa/temp." + extraName);

					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					return file;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}.execute(path);
	}

	public static void downloadInternetFile() {

	}
}

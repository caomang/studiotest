package cn.sbx.deeper.moblie.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUtil
{
	/** 下载文件总长度 */
	private int totalSize;
	/** 下载文件进度 */
	private int progress;
	/** 下载文件 */
	private File downFile = null;

	/**
	 * 文件下载
	 * @param downUrl 下载链接
	 * @return 下载的文件
	 */
	public File downloadFile(String downUrl)
	{
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			Log.d("url", "download - " + downUrl);
			try
			{
				URL url = new URL(downUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(1000 * 5);
				totalSize = conn.getContentLength();
				if (totalSize <= 0)
				{
					return null;
				}
				progress = 0;
				InputStream is = conn.getInputStream();
				String filename = downUrl.substring(downUrl.lastIndexOf("/") + 1);// 获得文件名
				Log.d("url", "filename - " + filename);
//				downFile = new File(Environment.getExternalStorageDirectory(), filename);
				downFile = new File(FileCache.apkDir, filename);
				FileOutputStream fos = new FileOutputStream(downFile);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = bis.read(buffer)) != -1)
				{
					fos.write(buffer, 0, len);
					progress += len;
					// System.out.println("progress = " + progress);
				}
				fos.flush();
				fos.close();
				bis.close();
				is.close();
				conn.disconnect();
			}
			catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return downFile;
	}

	/**
	 * 安装APK文件
	 * @param  //APK文件名
	 * @param mContext
	 */
	public void installApk(Context mContext, File apkFile)
	{
		if (!apkFile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}

	/** 监听下载进度 */
	public void setOnDownloadListener(IOnDownloadListener listener)
	{
		listener.updateNotification(progress, totalSize, downFile);
	}
	
	/**
	 * 监听接口
	 */
	public interface IOnDownloadListener
	{
		/**
		 * 更新下载进度
		 * @param progress 下载进度值
		 * @param totalSize 文件总大小
		 * @param downFile 下载的文件
		 */
		void updateNotification(int progress, int totalSize, File downFile);
	}

}

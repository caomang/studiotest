package cn.sbx.deeper.moblie.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry;

public class FileCache {

	public static final String serializableName = "seria_cache";

	// 存储序列化的数据
	public static void setSerializableCacheFile(Context context,
			SinopecApproveDetailEntry detailEntry) {

		File file = new File(context.getCacheDir(), serializableName);
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(detailEntry);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获取序列化里面的数据
	public static SinopecApproveDetailEntry getSerializableCacheFile(
			Context context) {
		SinopecApproveDetailEntry entry = null;
		try {
			ObjectInputStream os = new ObjectInputStream(new FileInputStream(
					new File(context.getCacheDir().getPath() + "/"
							+ serializableName)));
			// String resultData = StreamUtils.retrieveContent(os);
			entry = (SinopecApproveDetailEntry) os.readObject();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entry;
	}

	/**
	 * 清除缓存文件
	 * 
	 * @param dir
	 */
	public static void deleteCacheFile(String dir) {
		File file = new File(dir);
		if (file != null) {
			File[] files = file.listFiles();
			for (int i = 0; files != null && i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteCacheFile(files[i].getAbsolutePath());
				}
				if (!files[i].isDirectory()) {
					files[i].delete();
				}
			}
		}
	}

	// 删除文件夹下面的文件
	public static void deleteFiles(File file) {
		try {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] fileChilds = file.listFiles();
				for (int i = 0; i < fileChilds.length; i++) {
					deleteFiles(fileChilds[i]);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void deleteOAFile() {
		try {
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				String absolutePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				File file = new File(absolutePath + "/" + Constants.home_cache
						+ "/oa");
				if (!file.exists()) {
					file.mkdirs();
				} else {
					deleteFiles(file);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 对保密附件的处理
	public static void deleteSecretFile() {
		try {
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				String absolutePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				File file = new File(absolutePath + "/" + Constants.home_cache
						+ "/secret");
				if (!file.exists()) {
					file.mkdirs();
				} else {
					deleteFiles(file);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private File cacheDir;
	public static File apkDir;
	public static File pollingDir;

	public FileCache(Context context) {
		// Find the dir to save cached images
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(
					Environment.getExternalStorageDirectory(),
					Constants.home_cache);
			apkDir = new File(
					Environment.getExternalStorageDirectory(),
					Constants.home_cache + "/apk");
			pollingDir = new File(
					Environment.getExternalStorageDirectory(),
					Constants.home_cache + "/polling");
		} else {
			cacheDir = context.getCacheDir();
			apkDir = context.getCacheDir();
			pollingDir = context.getCacheDir();
		}
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		if (!apkDir.exists())
			apkDir.mkdirs();
		if (!pollingDir.exists())
			pollingDir.mkdirs();
	}

	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

	// 根据路径创建缓存文件夹
	public void createFileCaChe(String path) {

		File destDir = new File(path);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
	}

}

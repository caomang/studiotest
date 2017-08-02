package cn.sbx.deeper.moblie.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.sbx.deeper.moblie.contrants.Constants;
import petrochina.ghzy.a10fieldwork.R;


/**
 * 管理皮肤的类
 * 
 * @author terry.C
 * 
 */
public class SkinManager {

	private static final String TAG = "SkinManager";
	SharedPreferences sp;
	Context mContext;
	Display display;

	public SkinManager(Context mContext) {
		this.mContext = mContext;
		display = ((WindowManager) (mContext
				.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
		sp = mContext.getSharedPreferences("sys_config", Context.MODE_PRIVATE);
	}

	/**
	 * 从Assert文件夹获取图片 路径由imageName拼好传进来
	 * 
	 * @param imageName
	 * @param width
	 * @param height
	 * @return
	 */
	public BitmapDrawable getAssertDrawable(String imageName, int width,
			int height) {
		if (imageName == null || "".equals(imageName)) {
			return null;
		}
		// String icName = imageName.concat(".jpg");
		InputStream is = null;
		try {
			is = mContext.getAssets().open(imageName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (is == null) {
			if (Constants.DEBUG)
				Log.i(TAG, "image is not found");
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDensity = mContext.getResources().getDisplayMetrics().densityDpi;
		options.inTargetDensity = mContext.getResources().getDisplayMetrics().densityDpi;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		String imageType = options.outMimeType;
		options.inSampleSize = calculateInSampleSize(options, width, height);
		// options.inScaled = true;
		options.inJustDecodeBounds = false;
		if (Constants.DEBUG) {
			Log.i(TAG, "imageHeight:" + imageHeight + "...imageWidth:"
					+ imageWidth + "...imageType:" + imageType);
		}
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
		// Bitmap bitmap = BitmapFactory.decodeStream(is);
		if (Constants.DEBUG) {
			Log.i(TAG, "imageHeight:" + bitmap.getHeight() + "...imageWidth:"
					+ bitmap.getWidth() + "...options.inDensity:"
					+ options.inDensity);

		}
		BitmapDrawable bd = new BitmapDrawable(mContext.getResources(),bitmap);
		return bd;
	}

	/**
	 * 获取来自任意资源文件的图片 图片文件目录
	 * /data/data/packageName/app_skin（/app_blue-skin/app_red-skin）
	 * 
	 * @param imageName
	 * @param width
	 * @param height
	 * @return
	 */
	public BitmapDrawable getSkinBitmapDrawable(String imageName, int width,
			int height) {
		if (imageName == null || "".equals(imageName)) {
			return null;
		}
		String icName = imageName.concat(".png");
		File f = mContext.getDir(Constants.skin_folder, Context.MODE_PRIVATE);
		if (Constants.DEBUG)
			Log.i(TAG, "base file path:" + f.getAbsolutePath());
		File image = new File(f.getAbsolutePath() + File.separator + icName);
		if (Constants.DEBUG)
			Log.i(TAG, "image path:" + f.getAbsolutePath() + File.separator
					+ icName);
		if (!image.exists()) {
			if (Constants.DEBUG)
				Log.i(TAG, "image is not found");
			// new Thread(new downloadImageTask(imageName)).start();
			return null;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDensity = mContext.getResources().getDisplayMetrics().densityDpi;
		options.inTargetDensity = mContext.getResources().getDisplayMetrics().densityDpi;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(f.getAbsolutePath() + File.separator + icName,
				options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		String imageType = options.outMimeType;
		options.inSampleSize = calculateInSampleSize(options, width, height);
		// options.inScaled = true;
		options.inJustDecodeBounds = false;
		if (Constants.DEBUG)
			Log.i(TAG, "imageHeight:" + imageHeight + "...imageWidth:"
					+ imageWidth + "...imageType:" + imageType);
		Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath()
				+ File.separator + icName, options);
		BitmapDrawable bd = new BitmapDrawable(mContext.getResources(), bitmap);
		return bd;
	}

	/**
	 * 从备用文件夹获取来自网络下载的菜单图片 图片文件目录 /data/data/packageName/app_skin
	 * 
	 * @param imageName
	 * @param width
	 * @param height
	 * @return
	 */
	public BitmapDrawable getSkinDrawable(String imageName, int width,
			int height) {
		if (imageName == null || "".equals(imageName)) {
			return null;
		}
		// String icName = imageName.concat(".png");
		String icName = imageName;
		File f = mContext.getDir(Constants.skin_folder, Context.MODE_PRIVATE);
		if (Constants.DEBUG)
			Log.i(TAG, "base file path:" + f.getAbsolutePath());
		File image = new File(f.getAbsolutePath() + File.separator + icName);
		if (Constants.DEBUG)
			Log.i(TAG, "image path:" + f.getAbsolutePath() + File.separator
					+ icName);
		if (!image.exists()) {
			if (Constants.DEBUG)
				Log.i(TAG, "image is not found");
			// new Thread(new downloadImageTask(imageName)).start();
			return null;
		}
		if (Constants.DEBUG) {
			Log.i(TAG, "从文件夹:" + Constants.skin_folder + ",中找到图片:" + icName);
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDensity = mContext.getResources().getDisplayMetrics().densityDpi;
		options.inTargetDensity = mContext.getResources().getDisplayMetrics().densityDpi;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(f.getAbsolutePath() + File.separator + icName,
				options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		String imageType = options.outMimeType;
		if (null == imageType) {
			return null;
		}
		options.inSampleSize = calculateInSampleSize(options, width, height);
		// options.inScaled = true;
		options.inJustDecodeBounds = false;
		if (Constants.DEBUG)
			Log.i(TAG, "imageHeight:" + imageHeight + "...imageWidth:"
					+ imageWidth + "...imageType:" + imageType);
		Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath()
				+ File.separator + icName, options);
		BitmapDrawable bd = new BitmapDrawable(mContext.getResources(), bitmap);
		return bd;
	}

	/**
	 * tabar 根据皮肤文件生成selector------MainActivity
	 * 
	 * @param context
	 * @param idNormal
	 * @param idPressed
	 * @param //idFocused
	 * @param //idUnable
	 * @return
	 */
	public StateListDrawable newSelector(Context context, String idNormal,
			String idPressed, int width, int height) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = getAssertDrawable(idNormal, width, height);
		Drawable pressed = getAssertDrawable(idPressed, width, height);
		if (normal == null || pressed == null) {
			// 此处需要从网络下载皮肤包里缺失的图片资源放到/data/data/packageName/app_skin 文件夹
			String image1 = idNormal.substring(idNormal.lastIndexOf("/") + 1);
			String image2 = idNormal.substring(idNormal.lastIndexOf("/") + 1);
			normal = getSkinDrawable(image1, width, height);
			pressed = getSkinDrawable(image2, width, height);
			if (normal == null || pressed == null) {
				if (!DataCache.imageQueue.contains(idNormal.substring(idNormal
						.lastIndexOf("/") + 1))) {
					DataCache.imageQueue.add(idNormal.substring(idNormal
							.lastIndexOf("/") + 1));
					if (Constants.DEBUG) {
						Log.i(TAG, "没有从主题文件夹:" + Constants.skin_folder
								+ "中找到图片，即将从网络下载:" + normal);
					}
//					new Thread(new mainMenuDownloadImageTask(image1, image2,
//							Constants.skin_folder)) {
//					}.start();
				}
				return null;
			}
		}
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] { android.R.attr.state_checked }, pressed);
		bg.addState(new int[] { android.R.attr.state_pressed }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	/**
	 * 根据资源文件生成selector------MainMenuFragment
	 * 
	 * @param context
	 * @param idNormal
	 * @param idPressed
	 * @return
	 */
	public StateListDrawable newSelectorFromResourceByMenu(Context context,
			String idNormal, String idPressed, int width, int height) {
		StateListDrawable bg = new StateListDrawable();
		int nomalResourceId = getResourceId(context, idNormal, "drawable",
				context.getPackageName());
		int selectResourceId = getResourceId(context, idPressed, "drawable",
				context.getPackageName());
		Drawable normal = null;
		Drawable pressed = null;
		if (nomalResourceId == 0) {
			normal = getSkinDrawable(idNormal.concat(".png"), width, height);
		} else {
			normal = context.getResources().getDrawable(nomalResourceId);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = true;
			// options.inDensity =
			// context.getResources().getDisplayMetrics().densityDpi;
			// options.inTargetDensity =
			// context.getResources().getDisplayMetrics().densityDpi;
			// BitmapFactory.decodeResource(mContext.getResources(),
			// nomalResourceId, options);
			// options.inSampleSize = calculateInSampleSize(options, width,
			// height);
			// // options.inScaled = true;
			// options.inJustDecodeBounds = false;
			// Bitmap bitmap =
			// BitmapFactory.decodeResource(mContext.getResources(),
			// nomalResourceId,options);
			// normal = new BitmapDrawable(bitmap);
		}
		if (selectResourceId == 0) {
			pressed = getSkinDrawable(idPressed.concat(".png"), width, height);
		} else {
			pressed = context.getResources().getDrawable(selectResourceId);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = true;
			// options.inDensity =
			// context.getResources().getDisplayMetrics().densityDpi;
			// options.inTargetDensity =
			// context.getResources().getDisplayMetrics().densityDpi;
			// BitmapFactory.decodeResource(mContext.getResources(),
			// selectResourceId, options);
			// options.inSampleSize = calculateInSampleSize(options, width,
			// height);
			// // options.inScaled = true;
			// options.inJustDecodeBounds = false;
			// Bitmap bitmap =
			// BitmapFactory.decodeResource(mContext.getResources(),
			// selectResourceId,options);
			// pressed = new BitmapDrawable(bitmap);
		}
		if (normal == null || pressed == null) {
			if (!DataCache.imageQueue.contains(idNormal)) {
				if (Constants.DEBUG) {
					Log.i(TAG, "既没有从资源文件中找到图片：" + idNormal + ",也没有从默认文件夹:"
							+ Constants.skin_folder + "中找到图片，即将从网络下载");
				}
				DataCache.imageQueue.add(idNormal);
//				new Thread(new mainMenuDownloadImageTask(
//						idNormal.concat(".png"), idPressed.concat(".png"),
//						Constants.skin_folder)).start();
			}
			normal = context.getResources().getDrawable(
					R.drawable.ic_default_item1);
			pressed = context.getResources().getDrawable(
					R.drawable.ic_default_item2);
		}
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] { android.R.attr.state_checked }, pressed);
		bg.addState(new int[] { android.R.attr.state_pressed }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	public StateListDrawable newSelectorFromResourceByMenuDefault(
			Context context) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = null;
		Drawable pressed = null;
		normal = context.getResources()
				.getDrawable(R.drawable.ic_default_item1);
		pressed = context.getResources().getDrawable(
				R.drawable.ic_default_item2);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] { android.R.attr.state_checked }, pressed);
		bg.addState(new int[] { android.R.attr.state_pressed }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	/**
	 * 根据资源文件生成selector------MainActivity
	 * 
	 * @param context
	 * @param idNormal
	 * @param idPressed
	 * @return
	 */
	public StateListDrawable newSelectorFromResource(Context context,
			String idNormal, String idPressed, int width, int height) {
		StateListDrawable bg = new StateListDrawable();
		int nomalResourceId = getResourceId(context, idNormal, "drawable",
				context.getPackageName());
		int selectResourceId = getResourceId(context, idPressed, "drawable",
				context.getPackageName());
		Drawable normal = null;
		Drawable pressed = null;
		if (nomalResourceId == 0) {
			if (Constants.DEBUG)
				Log.i(TAG, "没有从默认资源文件中找到图片" + idNormal + "，即将从默认文件夹中去寻找");
			normal = getSkinDrawable(idNormal.concat(".png"), width, height);
		} else {
			normal = context.getResources().getDrawable(nomalResourceId);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = true;
			// options.inDensity =
			// context.getResources().getDisplayMetrics().densityDpi;
			// options.inTargetDensity =
			// context.getResources().getDisplayMetrics().densityDpi;
			// BitmapFactory.decodeResource(mContext.getResources(),
			// nomalResourceId, options);
			// options.inSampleSize = calculateInSampleSize(options, width,
			// height);
			// // options.inScaled = true;
			// options.inJustDecodeBounds = false;
			// Bitmap bitmap =
			// BitmapFactory.decodeResource(mContext.getResources(),
			// nomalResourceId,options);
			// normal = new BitmapDrawable(bitmap);
		}
		if (selectResourceId == 0) {
			pressed = getSkinDrawable(idPressed.concat(".png"), width, height);
		} else {
			pressed = context.getResources().getDrawable(selectResourceId);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = true;
			// options.inDensity =
			// context.getResources().getDisplayMetrics().densityDpi;
			// options.inTargetDensity =
			// context.getResources().getDisplayMetrics().densityDpi;
			// BitmapFactory.decodeResource(mContext.getResources(),
			// selectResourceId, options);
			// options.inSampleSize = calculateInSampleSize(options, width,
			// height);
			// // options.inScaled = true;
			// options.inJustDecodeBounds = false;
			// Bitmap bitmap =
			// BitmapFactory.decodeResource(mContext.getResources(),
			// selectResourceId,options);
			// pressed = new BitmapDrawable(bitmap);
		}
		if (normal == null || pressed == null) {
			if (!DataCache.imageQueue.contains(idNormal)) {
				if (Constants.DEBUG)
					Log.i(TAG, "既没有从资源文件找到图片：" + idNormal
							+ ",也没有从默认文件夹找到,即将下载图片");
				DataCache.imageQueue.add(idNormal);
//				new Thread(new mainMenuDownloadImageTask(
//						idNormal.concat(".png"), idPressed.concat(".png"),
//						Constants.skin_folder)).start();
			}
			normal = context.getResources().getDrawable(
					R.drawable.ic_default_bar1);
			pressed = context.getResources().getDrawable(
					R.drawable.ic_default_bar2);
		}
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] { android.R.attr.state_checked }, pressed);
		bg.addState(new int[] { android.R.attr.state_pressed }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	/**
	 * MainMenuFragment 根据皮肤文件生成item背景图片
	 * 
	 * @param context
	 * @param drawableName
	 * @return
	 */
	public BitmapDrawable bitmapFromSkinFolder(Context context,
			String drawableName, int width, int height) {
		return getAssertDrawable(drawableName, width, height);
	}

	/**
	 * MainMenuFragment 根据资源文件生成item背景图片
	 * 
	 * @param context
	 * @param drawableName
	 * @return
	 */
	public BitmapDrawable bitmapFromResource(Context context,
			String drawableName, int width, int height) {
		BitmapDrawable bg = null;
		int nomalResourceId = getResourceId(context, drawableName, "drawable",
				context.getPackageName());
		if (nomalResourceId == 0) {
			bg = getSkinDrawable(drawableName, width, height);
		} else {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inDensity = context.getResources().getDisplayMetrics().densityDpi;
			options.inTargetDensity = context.getResources()
					.getDisplayMetrics().densityDpi;
			BitmapFactory.decodeResource(mContext.getResources(),
					nomalResourceId, options);
			int imageHeight = options.outHeight;
			int imageWidth = options.outWidth;
			String imageType = options.outMimeType;
			options.inSampleSize = calculateInSampleSize(options, width, height);
			// options.inScaled = true;
			options.inJustDecodeBounds = false;
			if (Constants.DEBUG) {
				Log.i(TAG, "height:" + height + "...width:" + width);
				Log.i(TAG, "imageHeight:" + imageHeight + "...imageWidth:"
						+ imageWidth + "...imageType:" + imageType);
			}
			Bitmap bitmap = BitmapFactory.decodeResource(
					mContext.getResources(), nomalResourceId, options);
			if (Constants.DEBUG) {
				Log.i(TAG, "bitmap size:" + bitmap.getWidth() + "...height:"
						+ bitmap.getHeight());
			}
			bg = new BitmapDrawable(bitmap);
			// bg = (BitmapDrawable)
			// context.getResources().getDrawable(nomalResourceId);
		}
		if (bg == null) {
			// new Thread(new mainMenuDownloadImageTask(drawableName)).start();
			Bitmap bitmap = BitmapFactory.decodeResource(
					mContext.getResources(),
					R.drawable.ic_nevagation_item_default);
			bg = new BitmapDrawable(bitmap);
			// bg = (BitmapDrawable)
			// context.getResources().getDrawable(R.drawable.ic_nevagation_item_default);
		}
		return bg;
	}

	public int getResourceId(Context context, String name, String type,
			String packageName) {
		Resources themeResources = null;
		PackageManager pm = context.getPackageManager();
		try {
			themeResources = pm.getResourcesForApplication(packageName);
			return themeResources.getIdentifier(name, type, packageName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * MainMenuFragmeny 加载item的任务
	 * 
	 */
	class mainMenuDownloadImageTask implements Runnable {
		String imageName1;
		String imageName2;
		String themeFolder;

		public mainMenuDownloadImageTask(String imageName1, String imageName2,
				String themeFolder) {
			this.imageName1 = imageName1;
			this.imageName2 = imageName2;
			this.themeFolder = themeFolder;
		}

		public void run() {
			InputStream is = null;
			File rootFile = mContext.getDir(themeFolder, Context.MODE_PRIVATE);
			File targetFile = null;
			File targetFile2 = null;
			try {
				String imageUrl = WebUtils.rootUrl
						+ "/Integrated/GetPicture.aspx?catalog=android@800_1280@"
						+ DataCache.themeMap.get(themeFolder) + "&filename="
						+ imageName1;
				HttpParams httpParameters = new BasicHttpParams();
				DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
				HttpPost httpPost = new HttpPost(imageUrl);
				if (Constants.DEBUG)
					Log.i(TAG, "download image imageUrl1:" + imageUrl);
				httpPost.setHeader("EquipType", "Android");
				httpPost.setHeader("EquipSN", WebUtils.deviceId);
				httpPost.setHeader("Soft", WebUtils.packageName);
				httpPost.setHeader("Tel", WebUtils.phoneNumber);
				httpPost.setHeader("Cookie", WebUtils.cookie);
				HttpResponse response = hc.execute(httpPost);
				HttpEntity entity = response.getEntity();
				if (200 == response.getStatusLine().getStatusCode()) {
					is = entity.getContent();
					if (!"text/html; charset=utf-8".equals(entity
							.getContentType().getValue())) {
						if (Constants.DEBUG) {
							Log.i(TAG, "image1 Content-type:"
									+ entity.getContentType().getValue());
						}
						targetFile = new File(rootFile.getAbsolutePath()
								+ File.separator + imageName1);
						OutputStream os = new FileOutputStream(targetFile);
						int len = 0;
						byte[] buffer = new byte[1024];
						while ((len = is.read(buffer)) != -1) {
							os.write(buffer, 0, len);
						}
						os.close();
					}
				}
				imageUrl = WebUtils.rootUrl
						+ "/Integrated/GetPicture.aspx?catalog=android@800_1280@"
						+ DataCache.themeMap.get(themeFolder) + "&filename="
						+ imageName2;
				hc = new DefaultHttpClient();
				httpPost = new HttpPost(imageUrl);
				if (Constants.DEBUG)
					Log.i(TAG, "download image imageUrl2:" + imageUrl);
				httpPost.setHeader("EquipType", "Android");
				httpPost.setHeader("EquipSN", WebUtils.deviceId);
				httpPost.setHeader("Soft", WebUtils.packageName);
				httpPost.setHeader("Tel", WebUtils.phoneNumber);
				httpPost.setHeader("Cookie", WebUtils.cookie);
				response = hc.execute(httpPost);
				entity = response.getEntity();
				if (200 == response.getStatusLine().getStatusCode()) {
					is = entity.getContent();
					if (!"text/html; charset=utf-8".equals(entity
							.getContentType().getValue())) {
						if (Constants.DEBUG) {
							Log.i(TAG, "image2 Content-type:"
									+ entity.getContentType().getValue());
						}
						targetFile2 = new File(rootFile.getAbsolutePath()
								+ File.separator + imageName2);
						OutputStream os = new FileOutputStream(targetFile2);
						int len = 0;
						byte[] buffer = new byte[1024];
						while ((len = is.read(buffer)) != -1) {
							os.write(buffer, 0, len);
						}
						os.close();
					}
				}
				if (targetFile != null && targetFile2 != null) {
					if (Constants.DEBUG) {
						Log.i(TAG,
								"下载到本地缺失的图片!:" + targetFile.getAbsolutePath()
										+ "..." + targetFile2.getAbsolutePath());
					}
					Intent intent = new Intent(Constants.MODIFY_APP_MENU_NUM);
					mContext.sendBroadcast(intent);
					mContext.sendBroadcast(new Intent(
							Constants.MODIFY_APP_MAIN_SKIN));
				}
			} catch (Exception e) {
				if (Constants.DEBUG)
					e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						if (Constants.DEBUG)
							e.printStackTrace();
					}
				}
			}
		}
	}

	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		if (Constants.DEBUG)
			System.out.println("inSampleSize:" + inSampleSize);
		return inSampleSize;
	}
}

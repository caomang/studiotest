package cn.sbx.deeper.moblie.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import cn.sbx.deeper.moblie.contrants.Constants;

/**
 * 处理图片工具类
 * 
 * @author f.jiang
 * 
 */
public class ImageUtils {

	// 图片异步加载器
	public static AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
	private static Bitmap bmp;

	public static Bitmap retrieveBitmap(String url, String tfilename,
			int lwidth, int lheight) {

		URL m;
		InputStream i = null;
		try {

			m = new URL(url);
			i = (InputStream) m.getContent();
			// save to file
			File tfile = new File(Environment.getExternalStorageDirectory(),
					"/" + Constants.home_cache + "/oa/" + tfilename + ".jpg");
			if (!tfile.exists()) {
				tfile.getParentFile().mkdirs();
				FileOutputStream fos = new FileOutputStream(tfile);
				byte[] buff = new byte[1024];
				int len;
				while ((len = i.read(buff)) > 0) {
					fos.write(buff, 0, len);
				}
			}

			// return bmpFromFile(tfile, lwidth, lheight);
			return bmpFromFile(tfile, lwidth);

		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		} finally {
			if (i != null)
				try {
					i.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	protected static Bitmap bmpFromFile(File tfile, int lwidth, int lheight) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		// tells the BitmapFactory class to just give us the bounds of the
		// image rather than attempting to decode the image itself.
		bmpFactoryOptions.inJustDecodeBounds = true;
		bmp = BitmapFactory.decodeFile(tfile.getAbsolutePath(),
				bmpFactoryOptions);

		int wratio = (int) Math
				.ceil((bmpFactoryOptions.outWidth / (float) lwidth));
		int hratio = (int) Math
				.ceil((bmpFactoryOptions.outHeight / (float) lheight));
		if (wratio > 1 && hratio > 1) {
			if (wratio > hratio) {
				// Width ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = wratio;
			} else {
				// Height ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = hratio;

			}
		}

		// Decode it for real
		bmpFactoryOptions.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(tfile.getAbsolutePath(),
				bmpFactoryOptions);
		if (tfile.exists()) {
			tfile.deleteOnExit();
		}
		return bmp;
	}

	/**
	 * added by st.wang 0709
	 * 
	 * @param tfile
	 * @param lwidth
	 * @return
	 */
	protected static Bitmap bmpFromFile(File tfile, int lwidth) {

		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		// tells the BitmapFactory class to just give us the bounds of the
		// image rather than attempting to decode the image itself.
		bmpFactoryOptions.inJustDecodeBounds = true;
		bmp = BitmapFactory.decodeFile(tfile.getAbsolutePath(),
				bmpFactoryOptions);

		int wratio = (int) Math
				.ceil((bmpFactoryOptions.outWidth / (float) (lwidth / 2)));
		if (wratio > 1) {
			bmpFactoryOptions.inSampleSize = wratio;
		}

		// Decode it for real
		bmpFactoryOptions.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(tfile.getAbsolutePath(),
				bmpFactoryOptions);
		if (tfile.exists()) {
			tfile.deleteOnExit();
		}
		return bmp;
	}

	public static Bitmap retrieveBitmapFromIndirectUrl(String url,
			final int lwidth, final int lheight) {
		String tfilename = url.replaceAll("\\W", "");
		try {
			System.out.println("url110=" + url);

			// save to file
			File tfile = new File(Environment.getExternalStorageDirectory(),
					"/" + Constants.home_cache + "/oa/" + tfilename + ".jpg");
			if (tfile.exists())
				return bmpFromFile(tfile, lwidth, lheight);

			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
			// HttpPost httpPost = new HttpPost(WebUtils.add_url
			// + "ForIpad/ToDoList/ToDoPdf.aspx");
			// httpPost.setHeader("cookie", WebUtils.cookie);
			// httpPost.setHeader("DeviceId", WebUtils.deviceId);
			// HttpPost httpPost = new HttpPost(url);
			HttpGet httpGet = new HttpGet(url);
			// List<NameValuePair> nvpairs = new ArrayList<NameValuePair>();
			// nvpairs.add(new BasicNameValuePair("pdfurl", url.trim()));
			// UrlEncodedFormEntity refe = new UrlEncodedFormEntity(nvpairs);
			// httpPost.setEntity(refe);
			HttpResponse res = hc.execute(httpGet);
			if (res.getStatusLine().getStatusCode() == 200) {
				InputStream in = res.getEntity().getContent();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				String result = new String(baos.toByteArray());

				return retrieveBitmap(result, tfilename, lwidth, lheight);
			} else {
				// 下载失败
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void shrinkFromFile(String tfilePath, String outPath,
			int lwidth, int lheight) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmp = BitmapFactory.decodeFile(tfilePath, bmpFactoryOptions);

		int tw = lwidth;
		int th = lheight;
		if (bmp.getWidth() < lwidth)
			tw = bmp.getWidth();
		if (bmp.getHeight() < lheight)
			th = bmp.getHeight();
		Bitmap shrinkedBmp = Bitmap.createScaledBitmap(bmp, tw, th, true);
		FileOutputStream out = null;
		try {

			out = new FileOutputStream(outPath);
			shrinkedBmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * added by wangst bitmap 打图片处理
	 * 
	 * @param
	 * @param
	 * @param
	 * @return
	 */

	public static Bitmap getBitmapImage(String imagePath) {
		Bitmap bm = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 240 * 240);
		opts.inJustDecodeBounds = false;
		try {
			bm = BitmapFactory.decodeFile(imagePath, opts);
		} catch (OutOfMemoryError err) {
		}
		return bm;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}

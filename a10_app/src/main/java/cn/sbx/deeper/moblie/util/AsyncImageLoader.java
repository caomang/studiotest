package cn.sbx.deeper.moblie.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

public class AsyncImageLoader {

	private HashMap<String, SoftReference<Drawable>> imageCache;
	private HashMap<String, SoftReference<Bitmap>> bitmapCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		bitmapCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();

			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				if (imageCallback != null) {
					imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public Bitmap loadBitmap(final String imageUrl,final int width,final int height,
			final BitmapCallback bitmapCallback) {
		if (bitmapCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = bitmapCache.get(imageUrl);
			Bitmap b = softReference.get();

			if (b != null) {
				return b;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				if (bitmapCallback != null) {
					bitmapCallback.bitmapLoaded((Bitmap) message.obj, imageUrl);
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap b = loadBitmapFromUrl(imageUrl,width,height);
				bitmapCache.put(imageUrl, new SoftReference<Bitmap>(b));
				Message message = handler.obtainMessage(0, b);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Drawable d = null;
		if (i != null) {
			d = Drawable.createFromStream(i, "src");
		}
		return d;
	}

	public static Bitmap loadBitmapFromUrl(String url,int lwidth,int lheight) {
		String tfilename = url.replaceAll("\\W", "");
		return ImageUtils.retrieveBitmap(url,tfilename,lwidth,lheight);
	}

	public interface ImageCallback {
		void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

	public interface BitmapCallback {
		void bitmapLoaded(Bitmap bitmap, String imageUrl);
	}
}

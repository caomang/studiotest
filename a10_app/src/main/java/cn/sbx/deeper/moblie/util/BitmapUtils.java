package cn.sbx.deeper.moblie.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class BitmapUtils {
	public static Bitmap drawableToBitmap(Drawable drawable, int width,int height) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						width,
						height,
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, width,
				height);
		drawable.draw(canvas);
		return bitmap;
	}
}

package cn.sbx.deeper.moblie.util;

import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.WeakHashMap;

public class CacheUtils {
	public static WeakHashMap<Integer, View> items = new WeakHashMap<Integer, View>();
	public static HashMap<String, ImageView> bubbleImages = new HashMap<String, ImageView>();
}

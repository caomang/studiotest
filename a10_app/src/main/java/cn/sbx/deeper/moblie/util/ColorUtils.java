package cn.sbx.deeper.moblie.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class ColorUtils {

	/**
	 * 颜色选择器
	 * @param normalColor 颜色id
	 * @param selected 选中颜色id
	 * @return
	 */
	public static StateListDrawable newSelector(int normalColor, int selected) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = new ColorDrawable(normalColor);
		Drawable pressed = new ColorDrawable(selected);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] { android.R.attr.state_checked }, pressed);
		bg.addState(new int[] { android.R.attr.state_pressed }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	public static StateListDrawable newSelectorDrable(Context context, int normalDrable, int selected) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = context.getResources().getDrawable(normalDrable);
		Drawable pressed = new ColorDrawable(selected);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] { android.R.attr.state_checked }, pressed);
		bg.addState(new int[] { android.R.attr.state_pressed }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	public static StateListDrawable newDrawble(Context context, int normalDrable, int selected) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = context.getResources().getDrawable(normalDrable);
		Drawable pressed = context.getResources().getDrawable(selected);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] { android.R.attr.state_checked }, pressed);
		bg.addState(new int[] { android.R.attr.state_pressed }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}
}

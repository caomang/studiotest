package com.sunboxsoft.monitor.utils;

import android.widget.TextView;

public class DataIsNull {

	public static void setValue(TextView view, String string) {
		if (view != null) {
			if (string==null||string.equals("null")) {
				view.setText("");
			} else {
				view.setText(string);
			}
		}
	}
}

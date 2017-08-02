package cn.sbx.deeper.moblie.util;

import android.content.Context;
import android.text.Html;
import android.text.TextPaint;

import java.util.regex.Pattern;

/**
 * 处理文字显示的类
 * 
 * @author terry.C
 * 
 */

public class TextUtils {
	public static String parseText(String text) {
		if (text == null || "".equals(text.trim())) {
			return text;
		}
		String newString = "";
		try {
			Integer i = Integer.parseInt(text);
			if (null != i) {
				if (i >= 0 && i <= 9) {
					newString = " " + i + " ";
				} else {
					newString = i + "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return text;
		}
		return newString;
	}

	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static String Trim(String string) {
		if (string == null) {
			return "";
		}
		return string.trim();
	}

	public static String[] transPdf(String sourcePath) {
		String[] s = new String[2];
		if (sourcePath != null && !"".equals(sourcePath.trim())) {
			String name = sourcePath.substring(sourcePath.indexOf("]", 1) + 1,
					sourcePath.indexOf("[", 2));
			String value = sourcePath.substring(sourcePath.indexOf("\"") + 1,
					sourcePath.lastIndexOf("\""));
			s[0] = name;
			s[1] = value;
		}
		return s;
	}

	public static float calculateTextWidth(String text) {
		TextPaint paint = new TextPaint();
		return paint.measureText(Html.fromHtml(text).toString());
	}

	public static String HtmlText(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		Pattern p_script;
		java.util.regex.Matcher m_script;
		Pattern p_style;
		java.util.regex.Matcher m_style;
		Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
																										// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
																									// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			/* 空格 —— */
			// p_html = Pattern.compile("\\ ", Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = htmlStr.replaceAll(" ", " ");

			textStr = htmlStr;

		} catch (Exception e) {
		}
		return textStr;
	}

	public static String parseHtml(String s) {
		String content = s;
		content = content.replaceAll("&amp;", "");
		content = content.replaceAll("quot;", "\"");
		content = content.replaceAll("lt;", "<");
		content = content.replaceAll("gt;", ">");
		content = content.replaceAll("gt;", ">");
		content = content.replaceAll("nbsp;", " ");
		content = content.replaceAll("ldquo;", "“");
		content = content.replaceAll("rdquo;", "”");
		return content;
	}
}

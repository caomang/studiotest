package cn.sbx.deeper.moblie.util;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class JsonUtil {

	public static boolean isBadJson(String json) {
		return !isGoodJson(json);
	}

	/**
	 * 判断一个字符串是否是json串
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isGoodJson(String json) {
		if (StringUtil.isEmpty(json)) {
			return false;
		}
		try {
			new JsonParser().parse(json);
			return true;
		} catch (JsonParseException e) {
			return false;
		}
	}

	/**
	 * 将InputStream转换成String
	 * 
	 * @param in
	 *            InputStream
	 * @return String
	 * @throws Exception
	 * 
	 */
	final static int BUFFER_SIZE = 4096;

	public static String InputStreamTOString(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), "UTF-8");
	}
}

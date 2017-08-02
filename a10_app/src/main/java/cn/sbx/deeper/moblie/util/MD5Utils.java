package cn.sbx.deeper.moblie.util;

import org.apache.commons.codec.digest.DigestUtils;


public class MD5Utils {

	// MD5加码。32位
	public static String getValues(String inStr) {
		String md5 = inStr + inStr.length();
		md5 = DigestUtils.md5Hex(md5).toUpperCase();
		md5 = md5.substring(0, 2) + md5.substring(md5.length() - 1) + inStr;
		md5 = DigestUtils.md5Hex(md5).toUpperCase();
		return md5;
	}

}

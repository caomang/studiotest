package cn.sbx.deeper.moblie.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
	/**
	 * 根据InputStream获取内容
	 * 
	 * @param in
	 * @return
	 */
	public static String retrieveContent(InputStream in) {
		StringBuilder sb = new StringBuilder();
		byte[] buff = new byte[1024];
		int len = 0;
		try {
			while ((len = in.read(buff)) > 0) {
				sb.append(new String(buff, 0, len, "UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	public static void writeDataToFile(String filename,String msg){
    	try {
			FileOutputStream fos=new FileOutputStream(filename);
			byte[] buf=msg.getBytes();
			fos.write(buf);
			fos.close();
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static String getSDPATH() {
		String imagepath = null;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			imagepath = android.os.Environment.getExternalStorageDirectory() + "/";// 你的SD卡路径
		} else {
			imagepath = android.os.Environment.getDataDirectory() + "/" + "com.sbx.deeper.moblie" + "/";// 你的本地路径
		}
		return imagepath;
	}
	

	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}

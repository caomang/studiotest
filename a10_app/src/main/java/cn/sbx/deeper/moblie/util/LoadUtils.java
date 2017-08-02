package cn.sbx.deeper.moblie.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoadUtils {

	public static byte[] load(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);
		in.close();
		out.close();
		return out.toByteArray();
	}

}

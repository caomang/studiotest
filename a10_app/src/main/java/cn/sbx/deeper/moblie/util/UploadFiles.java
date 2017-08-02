package cn.sbx.deeper.moblie.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.sbx.deeper.moblie.domian.PhotoAudio;
import cn.sbx.deeper.moblie.domian.ResultInfo;


public class UploadFiles {

	/**
	 * 上传文件请求
	 * 
	 * @param urlStr
	 * @param headers
	 *            http头参数
	 * @param params
	 *            字段
	 * @param fileFieldName
	 * @param //fileName
	 * @param //file
	 * @return
	 */
	public static ResultInfo postFile(String urlStr,
			Map<String, String> headers, Map<String, String> params,
			String fileFieldName, List<PhotoAudio> imgBmpList) {
		ResultInfo resultInfo = new ResultInfo();
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		HttpURLConnection conn = null;
		InputStream input = null;
		DataOutputStream output = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(50 * 1000);
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false);
			conn.setRequestMethod("POST"); // Post方式
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", "UTF-8");

			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);

			// http头
			if (headers != null) {
				Iterator<Entry<String, String>> it = headers.entrySet()
						.iterator();
				while (it.hasNext()) {
					Entry<String, String> entry = it.next();
					conn.addRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + LINEND);
					sb.append("Content-Type: text/plain; charset=" + CHARSET
							+ LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
					sb.append(LINEND);
					sb.append(entry.getValue());
					sb.append(LINEND);
				}
			}
			sb.append("");

			output = new DataOutputStream(conn.getOutputStream());
			if (params != null) {
				output.write(sb.toString().getBytes());
			}
			// 发送文件数据

			if (imgBmpList.size() > 0) {
				for (PhotoAudio filePath : imgBmpList) {
					File file = new File(filePath.getUrl());
					if (file != null && file.exists()) {
						if (fileFieldName == null
								|| fileFieldName.trim().length() == 0) {
							fileFieldName = "image";
						}
						StringBuilder sb1 = new StringBuilder();
						sb1.append(PREFIX);
						sb1.append(BOUNDARY);
						sb1.append(LINEND);
						sb1.append("Content-Disposition: form-data; name=\""
								+ fileFieldName + "\"; filename=\""
								+ file.getName() + "\"" + LINEND);
						sb1.append("Content-Type: application/octet-stream"
								+ LINEND);
						sb1.append(LINEND);

						System.out.println("filesb:" + sb1.toString());
						output.write(sb1.toString().getBytes());
						FileInputStream fileInput = new FileInputStream(file);
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = fileInput.read(buffer)) != -1) {
							output.write(buffer, 0, len);
						}
						fileInput.close();
						output.write(LINEND.getBytes());
					}
				}
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			output.write(end_data);
			output.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			if (res == HttpURLConnection.HTTP_OK) {
				// 读数据
				input = conn.getInputStream();
				// LogUtils.logV(DataCollectionUtils.inputStream2String(input));
//				 String resultData = StreamUtils.retrieveContent(input);
				resultInfo = DataUtil.submitDataParser(input);
				return resultInfo;
			} else {
			}
		} catch (Exception e) {
			System.out.println("文件操作异常");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (output != null) {
					output.close();
					output = null;
				}
				if (input != null) {
					input.close();
					input = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			conn.disconnect();
		}
		return resultInfo;
	}
}

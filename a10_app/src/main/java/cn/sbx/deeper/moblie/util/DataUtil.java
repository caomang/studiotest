package cn.sbx.deeper.moblie.util;

import android.util.Xml;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.domian.ResultInfo;
import cn.sbx.deeper.moblie.domian.linesinfo;

public class DataUtil {

	// 向服务器发送数据
	public static ResultInfo sendXML(String xmlString) {
		ResultInfo result = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(WebUtils.uploadgpsUrl);
			// httpPost.setHeader("cookie", WebUtils.cookie);
			// httpPost.setHeader("DeviceId", WebUtils.deviceId);
			// httpPost.setHeader("AndroidID", WebUtils.androidId);
			// httpPost.setHeader("EquipType", "Android");
			setHeader(httpPost);
			// httpPost.setHeader("EquipSN", WebUtils.deviceId);
			// httpPost.setHeader("Soft", WebUtils.packageName);
			// httpPost.setHeader("Tel", WebUtils.phoneNumber);
			// httpPost.setHeader("status", status);// 0 开始，1，结束，2运动中
			// httpPost.setHeader("batchId", batchId);// 0 开始，1，结束，2运动中
			// httpPost.setHeader("RouteCode", routecode);// 0 开始，1，结束，2运动中
			// MultipartEntity multiPart = new MultipartEntity();
			// // multiPart.addPart("UserId", new StringBody(username));
			// multiPart.addPart("submit",
			// new StringBody(xmlString, Charset.forName("UTF-8")));
			// // multiPart.addPart("Coordinate",
			// // new StringBody(Coordinate, Charset.forName("UTF-8")));
			// httpPost.setEntity(multiPart);
			// HttpResponse res = httpClient.execute(httpPost);
			// System.out.println("res.getStatusLine().getStatusCode()="
			// + res.getStatusLine().getStatusCode());
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("key1", URLEncoder
					.encode(xmlString, "utf-8")));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream is = response.getEntity().getContent();
				// byte[] data1 = LoadUtils.load(is);
				// System.out.println("new String(---------------): "
				// + new String(data1, "UTF-8"));

				// 流以 字符串的形式打印
				// <result><message>成功</message><key>1</key></result>\
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int event = parser.getEventType();
				while (event != XmlPullParser.END_DOCUMENT) {
					switch (event) {
					case XmlPullParser.START_TAG:
						if ("result".equals(parser.getName())) {
							result = new ResultInfo();
						} else if ("message".equals(parser.getName())) {
							result.message = parser.nextText();
						} else if ("key".equals(parser.getName())) {
							result.key = parser.nextText();
						}
						break;
					}
					event = parser.next();
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void setHeader(HttpPost httpPost) {
		httpPost.setHeader("EquipType", "Android");
		httpPost.setHeader("EquipSN", WebUtils.deviceId);
		httpPost.setHeader("Soft", WebUtils.packageName);
		httpPost.setHeader("Tel", WebUtils.phoneNumber);
		httpPost.setHeader("Cookie", WebUtils.cookie);
		httpPost.setHeader("network", WebUtils.networkType);
	}

	public static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	// 提交数据之后解析出来的数据
	public static ResultInfo submitDataParser(InputStream inputStream) {
		try {
			ResultInfo resultInfo = new ResultInfo();
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(inputStream, "utf-8");
//			byte[] data1 = LoadUtils.load(inputStream);
//			System.out.println("new String(------------): "
//					+ new String(data1, "UTF-8"));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if ("message".equalsIgnoreCase(parser.getName())) {
						resultInfo.message = parser.nextText();
					} else if ("key".equalsIgnoreCase(parser.getName())) {
						resultInfo.key = parser.nextText();
					} else if ("currNode".equalsIgnoreCase(parser.getName())) {
						resultInfo.currNode = parser.nextText();
					}else if ("displayDevice".equalsIgnoreCase(parser.getName())) {
						resultInfo.displayDevice = parser.nextText();
					}
					break;

				default:
					break;
				}
				event = parser.next();
			}
			return resultInfo;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	// 向服务器发送数据
	public static List<linesinfo> getline(String path) {
		List<linesinfo> linesinfos = new ArrayList<linesinfo>();
		linesinfo result = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(path);
			HttpResponse res = httpClient.execute(httpPost);
			System.out.println("res.getStatusLine().getStatusCode()="
					+ res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200
					|| res.getStatusLine().getStatusCode() == 500) {
				InputStream is = res.getEntity().getContent();
				// String resultData = StreamUtils.retrieveContent(is);
				// 流以 字符串的形式打印
				// <result><message>成功</message><key>1</key></result>\
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int event = parser.getEventType();
				while (event != XmlPullParser.END_DOCUMENT) {
					switch (event) {
					case XmlPullParser.START_TAG:
						if ("Route".equals(parser.getName())) {
							result = new linesinfo();
							result.RouteName = parser.getAttributeValue(null,
									"RouteName");
							result.RouteCode = parser.getAttributeValue(null,
									"RouteCode");
							linesinfos.add(result);
						}

						break;
					}

					event = parser.next();
				}
				return linesinfos;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linesinfos;
	}

}

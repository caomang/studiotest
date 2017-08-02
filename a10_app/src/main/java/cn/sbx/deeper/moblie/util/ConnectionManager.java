package cn.sbx.deeper.moblie.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.sbx.deeper.moblie.contrants.Constants;

public class ConnectionManager {

	private static final String TAG = "ConnectionManager";

	/**
	 * 
	 * @param path1
	 *            pdf的内网地址
	 * @param path2
	 *            解析pdf的地址
	 * @return pdf解析后可以下载的地址
	 * @throws Exception
	 */
	public static String getHttpGetPDFUrl(String path1, String path2)
			throws Exception {
		String url = "";
		InputStream inputStream = null;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				10 * 60 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10 * 60 * 1000);
		DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(path2 + "&key1="
				+ URLEncoder.encode(path1, "utf-8"));
		Log.i(TAG,
				"download fileurl:" + path2 + "&key1="
						+ URLEncoder.encode(path1, "utf-8"));
		setHeader(httpPost);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("key1", path1));
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		HttpResponse httpResponse = hc.execute(httpPost);
		inputStream = httpResponse.getEntity().getContent();
		byte[] b = new byte[1024];
		int len = 0;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		while ((len = inputStream.read(b)) != -1) {
			stream.write(b, 0, len);
		}
		stream.close();
		url = new String(stream.toByteArray()); // 把读取的数据转换成字符串
		return url;
	}

	/**
	 * 
	 * @param path1
	 *            pdf的内网地址
	 * @param path2
	 *            解析pdf的地址
	 * @return pdf解析后可以下载的地址
	 * @throws Exception
	 */
	public static String getHttpGetPDFUrl_trans2p0(String path1, String path2)
			throws Exception {
		String url = "";
		InputStream inputStream = null;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				10 * 60 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10 * 60 * 1000);
		DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
		String requestData = "&prm1=" + URLEncoder.encode(path1) + "&prm2=1";
		HttpGet httpGet = new HttpGet(path2 + requestData);
		System.out.println("移动门户的附件解析地址：" + path2 + requestData);
		httpGet.addHeader("Cookie", WebUtils.cookie);
		HttpResponse httpResponse = hc.execute(httpGet);
		inputStream = httpResponse.getEntity().getContent();
		byte[] b = new byte[1024];
		int len = 0;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		while ((len = inputStream.read(b)) != -1) {
			stream.write(b, 0, len);
		}
		stream.close();
		url = new String(stream.toByteArray()); // 把读取的数据转换成字符串
		return url;
	}

	/**
	 * 解析pdf地址成可以在外网上下载的地址
	 * 
	 * @param path1
	 *            pdf的内网地址
	 * @param path2
	 *            解析pdf的地址
	 * @return pdf解析后可以下载的地址
	 * @throws Exception
	 */
	public static String getHttpClientPDFUrl(String path1, String path2)
			throws Exception {
		InputStream inputStream = null;
		HttpParams httpParameters = new BasicHttpParams();
		DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(path2);
		httpPost.addHeader("Cookie", WebUtils.cookie);
		httpPost.addHeader("DeviceId", WebUtils.deviceId);
		httpPost.addHeader("pdfurl", path1);
		List<NameValuePair> lists = new ArrayList<NameValuePair>();
		NameValuePair pair1 = new BasicNameValuePair("prm1",
				URLEncoder.encode(path1));
		NameValuePair pair2 = new BasicNameValuePair("prm2", "1");
		lists.add(pair1);
		lists.add(pair2);
		HttpEntity httpEntity = new UrlEncodedFormEntity(lists, "UTF-8");
		httpPost.setEntity(httpEntity);
		HttpResponse httpResponse = hc.execute(httpPost);
		inputStream = httpResponse.getEntity().getContent();
		byte[] b = new byte[1024];
		int len = 0;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		while ((len = inputStream.read(b)) != -1) {
			stream.write(b, 0, len);
		}
		stream.close();
		String url = new String(stream.toByteArray()); // 把读取的数据转换成字符串
		return url;
	}

	/**
	 * 
	 * @param cookie
	 * @param path
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public static InputStream getInputStream(String cookie, String path,
			String deviceId) throws Exception {
		byte[] data = cookie.getBytes();
		// URL url = new URL(path);
		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		HttpURLConnection conn = ProxyCheck.myHttpURLConnection(path);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("DeviceId", deviceId);
		conn.setRequestProperty("EquipType", "Android");
		conn.setRequestProperty("EquipSN", WebUtils.deviceId);
		conn.setRequestProperty("Soft", WebUtils.packageName);
		conn.setRequestProperty("Tel", WebUtils.phoneNumber);
		conn.setRequestProperty("cookie", cookie);
		conn.setRequestProperty("Cookie", cookie);
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(data);

		if (conn.getResponseCode() == 200) {
			// String msg = conn.getResponseMessage();
			// System.out.println(msg);

			// byte[] data1 = LoadUtils.load(conn.getInputStream());
			// String contentType = conn.getContentType();
			// String charset =
			// contentType.substring(contentType.indexOf("charset=") + 8);
			//
			// System.out.println("new String(data, charset): " + new
			// String(data1, charset));

			return conn.getInputStream();
		} else {
			return null;
		}
	}

	public static InputStream receiverMainMenuStream(String path)
			throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("EquipType", "Android");
		conn.setRequestProperty("EquipSN", WebUtils.deviceId);
		conn.setRequestProperty("Soft", WebUtils.packageName);
		conn.setRequestProperty("Tel", WebUtils.phoneNumber);
		if (conn.getResponseCode() == 200) {
			// String msg = conn.getResponseMessage();
			// System.out.println(msg);

			// byte[] data1 = LoadUtils.load(conn.getInputStream());
			// String contentType = conn.getContentType();
			// String charset =
			// contentType.substring(contentType.indexOf("charset=") + 8);
			//
			// System.out.println("new String(data, charset): " + new
			// String(data1, charset));

			return conn.getInputStream();
		} else {
			return null;
		}
	}

	public static InputStream getInputStream1(String cookie, String path,
			String deviceId, String prm1, String prm2) throws Exception {
		byte[] data = (cookie + "&prm1=" + prm1 + "&prm2=" + prm2).getBytes();
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("DeviceId", deviceId);
		conn.setRequestProperty("EquipType", "Android");
		conn.setRequestProperty("EquipSN", WebUtils.deviceId);
		conn.setRequestProperty("Soft", WebUtils.packageName);
		conn.setRequestProperty("Tel", WebUtils.phoneNumber);
		conn.setRequestProperty("cookie", cookie);
		conn.setRequestProperty("Cookie", cookie);
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(data);

		if (conn.getResponseCode() == 200) {
			// String msg = conn.getResponseMessage();
			// System.out.println(msg);

			// byte[] data1 = LoadUtils.load(conn.getInputStream());
			// String contentType = conn.getContentType();
			// String charset =
			// contentType.substring(contentType.indexOf("charset=") + 8);
			//
			// System.out.println("new String(data, charset): " + new
			// String(data1, charset));

			return conn.getInputStream();
		} else {
			return null;
		}
	}

	public static InputStream getInputStream2(String cookie, String path,
			String deviceId, String prm1) throws Exception {
		HttpParams httpParameters = new BasicHttpParams();
		DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(path);
		setHeader(httpPost);
		List<NameValuePair> lists = new ArrayList<NameValuePair>();
		NameValuePair pair2 = new BasicNameValuePair("key1", prm1);
		lists.add(pair2);
		HttpEntity httpEntity = new UrlEncodedFormEntity(lists, "UTF-8");
		httpPost.setEntity(httpEntity);

		HttpResponse httpResponse = hc.execute(httpPost);

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			return httpResponse.getEntity().getContent();
		} else {
			return null;
		}
	}

	public static InputStream getInputStream2(String cookie, String path,
			String deviceId, String prm1, String prm2) throws Exception {
		HttpParams httpParameters = new BasicHttpParams();
		DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(path);
		setHeader(httpPost);
		List<NameValuePair> lists = new ArrayList<NameValuePair>();
		NameValuePair pair2 = new BasicNameValuePair("key1", prm1);
		NameValuePair pair1 = new BasicNameValuePair("key2", prm2);
		lists.add(pair2);
		HttpEntity httpEntity = new UrlEncodedFormEntity(lists, "UTF-8");
		httpPost.setEntity(httpEntity);

		HttpResponse httpResponse = hc.execute(httpPost);

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			return httpResponse.getEntity().getContent();
		} else {
			return null;
		}
	}

	public static InputStream getInputStreamByGet(String cookie, String path,
			String deviceId) throws Exception {
		byte[] data = cookie.getBytes();
		URL url = new URL(path);
		Log.i(TAG, "getInputStreamByGet:" + path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(timeOut);
		conn.setRequestProperty("DeviceId", deviceId);
		conn.setRequestProperty("EquipType", "Android");
		conn.setRequestProperty("EquipSN", WebUtils.deviceId);
		conn.setRequestProperty("Soft", WebUtils.packageName);
		conn.setRequestProperty("Tel", WebUtils.phoneNumber);
		conn.setRequestProperty("Cookie", cookie);
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(data);
		if (conn.getResponseCode() == 200) {
			// String msg = conn.getResponseMessage();
			// System.out.println(msg);
			return conn.getInputStream();
		} else {
			return null;
		}
	}

	public static InputStream getInputStreamForMatters(String cookie,
			String path) throws Exception {
		byte[] data = cookie.getBytes();
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("EquipType", "Android");
		conn.setRequestProperty("EquipSN", WebUtils.deviceId);
		conn.setRequestProperty("Soft", WebUtils.packageName);
		conn.setRequestProperty("Tel", WebUtils.phoneNumber);
		conn.setRequestProperty("Cookie", cookie);
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(data);
		if (conn.getResponseCode() == 302) {
			String msg = conn.getResponseMessage();
			System.out.println(msg);
			return conn.getInputStream();
		} else {
			return null;
		}
	}

	/**
	 * added fuctions
	 */
	// 创建HttpClient对象
	// public static HttpClient httpClient = new DefaultHttpClient();
	public static int timeOut = 20 * 1000;

	public static InputStream getConnection(String urlPath) {

		InputStream is = null;
		try {
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(timeOut);
			conn.setRequestMethod("POST");// 默认是get
			conn.setRequestProperty("Accept", "*/*");
			// 如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException
			conn.setRequestProperty("Content-type",
					"application/x-java-serialized-object");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Cookie", WebUtils.cookie);// 设置cookie

			is = conn.getInputStream();// 请求
			// 取得sessionid.
			String value = conn.getHeaderField("set-cookie");
			String sessionid = null;
			if (value != null) {
				sessionid = value.substring(0, value.indexOf(";"));
			}
			// 发送设置cookie：
			if (sessionid != null) {
				conn.setRequestProperty("Cookie", sessionid);
			}
			BufferedReader breader = new BufferedReader(new InputStreamReader(
					is, "gbk"));
			String str = breader.readLine();
			while (str != null) {
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return is;
	}

	public static InputStream getConnectionByGet(String url) {
		// 创建HttpGet对象
		HttpGet get = new HttpGet(url);
		get.setHeader("Accept", "*/*");
		get.setHeader("Accept-Language", "zh-CN");
		get.setHeader("Charset", "UTF-8");
		get.setHeader("Connection", "keep-alive");
		get.setHeader("Cookie", WebUtils.cookie);// cookie
		// 设置超时时间
		HttpParams params = get.getParams();
		HttpConnectionParams.setConnectionTimeout(params, timeOut);
		HttpConnectionParams.setSoTimeout(params, timeOut);
		DefaultHttpClient hc = HttpUtils.initHttpClient(params);

		// 发送GET请求
		HttpResponse httpResponse;
		try {
			httpResponse = hc.execute(get);
			// 如果服务器成功地返回响应
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)// 200
			{
				HttpEntity httpEntity = httpResponse.getEntity();
				// EntityUtils.getContentCharSet(httpResponse.getEntity());
				// EntityUtils.toByteArray(httpResponse.getEntity());
				// 获取服务器传来的cookie
				List<Cookie> cookies = hc
						.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None cookie");
				} else {
					for (Cookie ick : cookies) {
						String name = ick.getName().toString();
						System.out.println("cookie-name "
								+ ick.getName().toString());
						System.out.println("cookie-value "
								+ URLDecoder.decode(ick.getValue().toString(),
										"utf-8"));
						if ("mike".equals(name)) {
							System.out.println("cookie ok");
						}
					}
				}
				// 获取服务器响应字符串,默认编码类型是iso-8859-1，需要转换成utf-8
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				// 将String类型转换成InputStream类型
				InputStream is = new ByteArrayInputStream(result.getBytes());
				// return result;
				return is;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getConnectionByPost(String url,
			Map<String, String> rawParams) {
		// 创建HttpPost对象。
		HttpPost post = new HttpPost(url);
		post.setHeader("Accept", "*/*");
		post.setHeader("Accept-Language", "zh-CN");
		post.setHeader("Charset", "UTF-8");
		post.setHeader("Connection", "keep-alive");
		post.setHeader("Cookie", "name = pike");
		// 设置超时时间
		HttpParams param = post.getParams();

		HttpConnectionParams.setConnectionTimeout(param, timeOut);
		DefaultHttpClient hc = HttpUtils.initHttpClient(param);

		try {
			// 如果传递参数个数比较多的话可以对传递的参数进行封装
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String key : rawParams.keySet()) {
				// 封装请求参数
				params.add(new BasicNameValuePair(key, rawParams.get(key)));
			}
			// 设置请求参数
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			// 发送POST请求
			HttpResponse httpResponse = hc.execute(post);
			// 如果服务器成功地返回响应
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)// 200
			{
				HttpEntity httpEntity = httpResponse.getEntity();
				// 获取服务器传来的cookie
				List<Cookie> cookies = hc
						.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None cookie");
				} else {
					for (Cookie ick : cookies) {
						String name = ick.getName().toString();
						System.out.println("cookie-name "
								+ ick.getName().toString());
						System.out.println("cookie-value "
								+ URLDecoder.decode(ick.getValue().toString(),
										"utf-8"));
						if ("pike".equals(name)) {
							System.out.println("cookie ok");
						}
					}
				}
				// 获取服务器响应字符串,默认编码类型是iso-8859-1，需要转换成utf-8
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				// 将String类型转换成InputStream类型
				InputStream is = new ByteArrayInputStream(result.getBytes());
				// return result;
				return is;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * post上传文件
	 * 
	 * @param //Url
	 */
	public boolean UploadFiles(String url, File file) {

		if (url == null || "".equals(url) || file == null) {
			return false;
		}
		// 创建HttpPost对象。
		HttpPost post = new HttpPost(url);
		// post.setHeader("Accept", "*/*");
		// post.setHeader("Accept-Language", "zh-CN");
		// post.setHeader("Charset", "UTF-8");
		try {
			// 设置超时时间
			HttpParams params = post.getParams();
			HttpConnectionParams.setConnectionTimeout(params, timeOut);
			DefaultHttpClient hc = HttpUtils.initHttpClient(params);
			// http://192.168.1.112:8080/tomcatServer/xml
			// file && contentType
			// multipart/form-data
			FileEntity entity = new FileEntity(file, "multipart/form-data");// binary/octet-stream
																			// application/octet-stream
			post.setEntity(entity);
			HttpResponse httpResponse = hc.execute(post);
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.d("打印信息：", "上传成功");
				return true;
			} else {
				Log.d("上传失败", "fail");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * obtain main menu stream
	 * 
	 * @param url
	 * @return 获取menu的数量
	 */
	public static InputStream receiveMainStream(String url) {
		InputStream is = null;
		try {
			// DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(url);
			Log.i(TAG, "load main menu path:" + url);
			httpPost.setHeader("EquipType", "Android");
			httpPost.setHeader("EquipSN", WebUtils.deviceId);
			httpPost.setHeader("Soft", WebUtils.packageName);
			httpPost.setHeader("Tel", WebUtils.phoneNumber);
			httpPost.setHeader("Cookie", WebUtils.cookie);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (WebUtils.role.equals("1")) {
				// 如果用户是开发者
				if (Constants.developerMode.equals("")) {
					Constants.developerMode = "1";
				}
			} else if (WebUtils.role.equals("0")) {
				// 如果用户不是开发者
				Constants.developerMode = "0";
			}
			// params -----IsDev CVersion SCode
			nameValuePairs.add(new BasicNameValuePair("IsDev",
					Constants.developerMode));
			// nameValuePairs.add(new BasicNameValuePair("CVersion",
			// VersionUtils.getVersionName()));
			// nameValuePairs.add(new BasicNameValuePair("SCode",
			// WebUtils.packageName));
			nameValuePairs.add(new BasicNameValuePair("CVersion", "0.0"));
			nameValuePairs.add(new BasicNameValuePair("SCode",
					Constants.testPackage));
			Log.i(TAG, "IsDev:" + Constants.developerMode + "...:SCode:"
					+ Constants.testPackage);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (200 == response.getStatusLine().getStatusCode()) {
				is = entity.getContent();

				// int leng = 0;
				// byte[] arr = new byte[1024];
				// while ((leng = is.read(arr)) != 0) {
				// System.out.println(new String(arr, 0, leng) +
				// " ===============获取menu的数量 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				// }
			}
			return is;
		} catch (Exception e) {
			e.printStackTrace();
			return is;
		}

	}

	/**
	 * obtain main menu auth stream
	 * 
	 * @param url
	 * @return
	 */
	public static InputStream receiveMainAuthStream(String url, String version) {
		InputStream is = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(url);
			Log.i(TAG, "load main menu auth path:" + url);
			httpPost.setHeader("EquipType", "Android");
			httpPost.setHeader("EquipSN", WebUtils.deviceId);
			httpPost.setHeader("Soft", WebUtils.packageName);
			httpPost.setHeader("Tel", WebUtils.phoneNumber);
			httpPost.setHeader("Cookie", WebUtils.cookie);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			// params -----IsDev CVersion SCode
			nameValuePairs.add(new BasicNameValuePair("IsDev",
					Constants.developerMode));
			// nameValuePairs.add(new BasicNameValuePair("CVersion",
			// VersionUtils.getVersionName()));
			// nameValuePairs.add(new BasicNameValuePair("SCode",
			// WebUtils.packageName));
			nameValuePairs.add(new BasicNameValuePair("CVersion", version));
			nameValuePairs.add(new BasicNameValuePair("SCode",
					Constants.testPackage));
			Log.i(TAG, "IsDev:" + Constants.developerMode + "...:SCode:"
					+ Constants.testPackage);
			nameValuePairs.add(new BasicNameValuePair("userid", UserInfo
					.getInstance().getUsername()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = hc.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (200 == response.getStatusLine().getStatusCode()) {
				is = entity.getContent();
			}
			return is;
		} catch (Exception e) {
			e.printStackTrace();
			return is;
		}

	}

	private static void setHeader(HttpPost httpPost) {
		httpPost.setHeader("EquipType", "Android");
		httpPost.setHeader("EquipSN", WebUtils.deviceId);
		httpPost.setHeader("Soft", WebUtils.packageName);
		httpPost.setHeader("Tel", WebUtils.phoneNumber);
		httpPost.setHeader("Cookie", WebUtils.cookie);
	}
}

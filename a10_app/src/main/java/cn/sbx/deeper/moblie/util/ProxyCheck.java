package cn.sbx.deeper.moblie.util;

import net.arraynetworks.vpn.VPNManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

import cn.sbx.deeper.moblie.contrants.Constants;

public class ProxyCheck {
	private static String hostName = "127.0.0.1";
	public static DefaultHttpClient myHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// 设置超时时间
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, ConnectionManager.timeOut);
		HttpConnectionParams.setSoTimeout(params, ConnectionManager.timeOut);
		if (Constants.useVPN) {
			HttpHost httpHost = new HttpHost(hostName, VPNManager.getInstance()
					.getHttpProxyPort());
			httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
					httpHost);
		}
		return httpClient;
	}

	public static URLConnection myURLConnection(String url) throws IOException {
		URL connectUrl = new URL(url);
		URLConnection uc = null;
		if (Constants.useVPN) {
			SocketAddress sa = new InetSocketAddress(hostName, VPNManager
					.getInstance().getHttpProxyPort());
			java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
					sa);
			uc = connectUrl.openConnection(proxy);
		} else {
			uc = connectUrl.openConnection();
		}

		return uc;
	}

	public static HttpURLConnection myHttpURLConnection(String url)
			throws IOException {
		URL connectUrl = new URL(url);
		HttpURLConnection conn = null;
		if (Constants.useVPN) {
			SocketAddress sa = new InetSocketAddress(hostName, VPNManager
					.getInstance().getHttpProxyPort());
			java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
					sa);
			conn = (HttpURLConnection) connectUrl.openConnection(proxy);
		} else {
			conn = (HttpURLConnection) connectUrl.openConnection();
		}

		return conn;
	}

	private static DefaultHttpClient httpClient;
	private static int DEFAULT_SOCKET_TIMEOUT = 10000;
	private static int DEFAULT_HOST_CONNECTIONS = 1;
	private static int DEFAULT_MAX_CONNECTIONS = 1;
	private static int DEFAULT_SOCKET_BUFFER_SIZE = 2048;

	public static synchronized HttpClient getHttpClient() {
		if (httpClient == null) {
			final HttpParams httpParams = new BasicHttpParams();

			// timeout: get connections from connection pool
			ConnManagerParams.setTimeout(httpParams, 10000);
			// timeout: connect to the server
			HttpConnectionParams.setConnectionTimeout(httpParams,
					DEFAULT_SOCKET_TIMEOUT);
			// timeout: transfer data from server
			HttpConnectionParams.setSoTimeout(httpParams,
					DEFAULT_SOCKET_TIMEOUT);

			// set max connections per host
			ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
					new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));
			// set max total connections
			ConnManagerParams.setMaxTotalConnections(httpParams,
					DEFAULT_MAX_CONNECTIONS);

			// use expect-continue handshake
			HttpProtocolParams.setUseExpectContinue(httpParams, true);
			// disable stale check
			HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);

			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

			HttpClientParams.setRedirecting(httpParams, false);

			// set user agent
			// String userAgent =
			// "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
			// HttpProtocolParams.setUserAgent(httpParams, userAgent);

			// disable Nagle algorithm
			HttpConnectionParams.setTcpNoDelay(httpParams, true);

			HttpConnectionParams.setSocketBufferSize(httpParams,
					DEFAULT_SOCKET_BUFFER_SIZE);

			// scheme: http and https
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager manager = new ThreadSafeClientConnManager(
					httpParams, schemeRegistry);
			httpClient = new DefaultHttpClient(manager, httpParams);
		}

		return httpClient;
	}
}

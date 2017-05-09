package com.lee.xqq.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	private static HttpClient httpClient;
	private HttpPost httpPost = null;
	private HttpGet httpGet = null;
	private HttpPut httpPut = null;
	private HttpDelete httpDelete = null;
	private HttpResponse httpResponse = null;
	private HttpResult result;

	protected static final String CHARSET = HTTP.UTF_8;
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	private static int timeoutConnection = 15000;
	private static int timeoutSocket = 10000;
	private static final int BUFFER_SIZE = 1024 * 10;
	private static final String useragent = "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
			+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1";

	protected HttpUtil() {
		result = new HttpResult();
	}

	public HttpResult getResponse() {
		return result;
	}

	protected static String encode(String text) {
		try {
			return URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected static synchronized HttpClient getHttpClient() {
		if (null == httpClient) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setUserAgent(params, useragent);
			ConnManagerParams.setTimeout(params, timeoutConnection);
			HttpConnectionParams
					.setConnectionTimeout(params, timeoutConnection);
			HttpConnectionParams.setSoTimeout(params, timeoutSocket);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			httpClient = new DefaultHttpClient(conMgr, params);
		}
		return httpClient;
	}

	private void httpMethod(String url, String method, byte[] data)
			throws ClientProtocolException, IOException {
		method = method.toUpperCase(Locale.getDefault());
		StringEntity reqEntity = null;
		if (data != null) {
			reqEntity = new StringEntity(new String(data), HTTP.UTF_8);
			reqEntity.setContentType("application/x-www-form-urlencoded");
		}
		if (method.equals(GET)) {
			httpGet = new HttpGet(url);
			httpResponse = getHttpClient().execute(httpGet);
		} else if (method.equals("POST")) {
			httpPost = new HttpPost(url);
			if (reqEntity != null)
				httpPost.setEntity(reqEntity);
			httpResponse = getHttpClient().execute(httpPost);
		} else if (method.equals("PUT")) {
			httpPut = new HttpPut(url);
			if (reqEntity != null)
				httpPut.setEntity(reqEntity);
			httpResponse = getHttpClient().execute(httpPut);
		} else if (method.equals("DELETE")) {
			httpDelete = new HttpDelete(url);
			httpResponse = getHttpClient().execute(httpDelete);
		}
	}

	/**
	 * @param url
	 * @param method
	 * @param data
	 */
	protected void httpRequestText(String url, String method, byte[] data) {
		try {
			httpMethod(url, method, data);
			// httpResponse.setLocale(Locale.CHINA);
			int code = httpResponse.getStatusLine().getStatusCode();
			result.setCode(code);
			// System.out.println("code->" + code);
			if (code == HttpStatus.SC_OK) {
				result.setResponse(EntityUtils.toString(
						httpResponse.getEntity(), "UTF-8"));
			}
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			result.setException(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result.setException(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * @param url
	 * @param method
	 * @param data
	 */
	protected void httpRequestByte(String url, String method, byte[] data) {
		method = method.toUpperCase(Locale.getDefault());
		try {
			httpMethod(url, method, data);
			// httpResponse.setLocale(Locale.CHINA);
			int code = httpResponse.getStatusLine().getStatusCode();
			result.setCode(code);
			// System.out.println("code->" + code);
			if (code == HttpStatus.SC_OK) {
				InputStream is = httpResponse.getEntity().getContent();
				byte[] buffer = new byte[BUFFER_SIZE];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int ch;
				while ((ch = is.read(buffer)) != -1)
					bos.write(buffer, 0, ch);
				bos.flush();
				result.setData(bos.toByteArray());
				is.close();
				bos.close();
			}
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			result.setException(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result.setException(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * @param url
	 * @param method
	 * @param data
	 */
	protected void httpRequest(String url, String method, byte[] data) {
		method = method.toUpperCase(Locale.getDefault());
		try {
			httpMethod(url, method, data);
			// httpResponse.setLocale(Locale.CHINA);
			int code = httpResponse.getStatusLine().getStatusCode();
			result.setCode(code);
			// System.out.println("code->" + code);
			if (code == HttpStatus.SC_OK) {
				result.setStream(httpResponse.getEntity().getContent());
			}
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			result.setException(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result.setException(e.toString());
			e.printStackTrace();
		}
	}

	public static HttpUtil requestText(String url, String method) {
		return requestText(url, method, null);
	}

	public static HttpUtil requestText(String url, String method, byte[] data) {
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.httpRequestText(url, method, data);
		return httpUtil;
	}

	public static HttpUtil requestBytes(String url, String method) {
		return requestBytes(url, method, null);
	}

	public static HttpUtil requestBytes(String url, String method, byte[] data) {
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.httpRequestByte(url, method, data);
		return httpUtil;
	}

	public static HttpUtil requestStream(String url, String method) {
		return requestStream(url, method, null);
	}

	public static HttpUtil requestStream(String url, String method, byte[] data) {
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.httpRequest(url, method, data);
		return httpUtil;
	}

	/**
	 */
	public void abort() {
		if (httpGet != null)
			httpGet.abort();
		if (httpPost != null)
			httpPost.abort();
		if (httpPut != null)
			httpPut.abort();
		if (httpDelete != null)
			httpDelete.abort();
		httpGet = null;
		httpPost = null;
		httpPut = null;
		httpDelete = null;
	}
	
	public static void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

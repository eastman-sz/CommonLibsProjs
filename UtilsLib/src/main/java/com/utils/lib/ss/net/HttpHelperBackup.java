package com.utils.lib.ss.net;

/**
 * 网络访问类，主要用来轮询。
 * @author E
 */
public class HttpHelperBackup {

/*	private static final String CHARSET = "utf-8";
	private static final int TIME_OUT = 10 * 1000;
	
	public static final int RESULT_OK = 9999999;
	public static final int NET_ERROR_CODE = 9999998;
	
	public static final String NET_ERROR_STRING = "NetError";
	
	*//**
	 * Send request to server (Basic method).
	 * @param urls 地址数组
	 * @param params params to send
	 * @return result returned
	 * @throws Exception exception caused
	 *//*
	public static String sendRequest(String[] urls , HashMap<String, String> params) throws Exception{
		URLConnection resultUrlConnection = sendPostRequest(urls, params);
		String result = read2String(resultUrlConnection.getInputStream()).toString();
		return result;
	}
	
	*//**
	 * Send request to server.
	 * @param context
	 * @param urls 地址数组
	 * @param params params to send
	 * @return returned
	 * @throws Exception exception caused
	 *//*
	public static String sendRequest(Context context ,String[] urls , HashMap<String, String> params) throws Exception{
		if (!NetStatus.checkNetWorkStatus(context)) {
			return NET_ERROR_STRING;
		}
		String result = sendRequest(urls, params);
		return result;
	}	
	
	*//**
	 * 异步发送请求到服务器,通过Handler返回请求结果，网络异常等。
	 * @param urls 地址数组
	 * @param params 传送的参数
	 * @param handler 返回的请求结果
	 *//*
	public static void sendRequest(Context context ,String[] urls , HashMap<String, String> params , Handler handler){
		sendRequest(context ,urls, params, handler, -1);
	}
	
	*//**
	 * 异步发送请求到服务器,通过Handler返回请求结果，网络异常等。
	 * @param urls 地址数组
	 * @param params 传送的参数
	 * @param handler 返回的请求结果
	 * @param int Handler在返回结果是的WHATS, 如果是1,则用默认的IWYWhats.REQEST_RESULT还回结果,否则用传入的WHATS
	 *//*
	public static void sendRequest(Context context ,final String[] urls , final HashMap<String, String> params , final Handler handler, final int resultWhats){
		if (!NetStatus.checkNetWorkStatus(context)) {
			if (null != handler) {
				handler.sendEmptyMessage(NET_ERROR_CODE);
			}
			return;
		}
		ThreadPool.add(new Runnable() {
			@Override
			public void run() {
				try {
					String result = sendRequest(urls, params);
					if (null != handler) {
						handler.obtainMessage(-1 == resultWhats ? RESULT_OK : resultWhats, result).sendToTarget();
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (null != handler) {
						handler.sendEmptyMessage(NET_ERROR_CODE);
					}
				}
			}
		});
	}
	
	*//**
	 * 异步发送请求到服务器,通过Handler返回请求结果，网络异常等。
	 * @param urls 地址数组
	 * @param params 传送的参数
	 * @param handler 返回的请求结果
	 * @param int Handler在返回结果是的WHATS, 如果是1,则用默认的IWYWhats.REQEST_RESULT还回结果,否则用传入的WHATS
	 *//*
	public static void sendRequest(Context context ,final String[] urls , final HashMap<String, String> params , final Handler handler, final Message msg){
		if (!NetStatus.checkNetWorkStatus(context)) {
			if (null != handler) {
				handler.sendEmptyMessage(NET_ERROR_CODE);
			}
			return;
		}
		ThreadPool.add(new Runnable() {
			@Override
			public void run() {
				try {
					String result = sendRequest(urls, params);
					if (null != handler) {
						if (null == msg) {
							handler.obtainMessage(RESULT_OK, result).sendToTarget();
						}else {
							Bundle bundle = new Bundle();
							bundle.putString("result", result);
							msg.setData(bundle);
							handler.sendMessage(msg);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (null != handler) {
						handler.sendEmptyMessage(NET_ERROR_CODE);
					}
				}
			}
		});
	}
	
	private static URLConnection sendPostRequest(String[] urls , HashMap<String, String> params) throws Exception {
		int length = urls.length;
		if (length == 0) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		Set<Entry<String, String>> entrys = null;
		if (null != params && !params.isEmpty()) {
			entrys = params.entrySet();
			for (Map.Entry<String, String> entry : entrys) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (null != key && null != value) {
					builder.append(key).append("=")
					.append(URLEncoder.encode(value, CHARSET))
					.append("&");
				}
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		HttpURLConnection conn  = sendPost(urls, 0, builder);
		return conn;
	}
	
	private static  HttpURLConnection sendPost(String[] urls , int urlIndex, StringBuilder builder) throws Exception{
		String requestUrl = urls[urlIndex];
		int urlSize = urls.length;
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setConnectTimeout(TIME_OUT);
		OutputStream out = conn.getOutputStream();
		out.write(builder.toString().getBytes(CHARSET));
		int responseCode = conn.getResponseCode(); 
		if (responseCode != 200 && urlIndex < (urlSize-1)) {
			urlIndex ++ ;
			sendPost(urls, urlIndex, builder);
		}
		return conn;
	}
	
	*//**
	 * 将输入流转为字符流。
	 * @param inStream 输入流
	 * @return 内容。
	 * @throws Exception 异常
	 *//*
	private static String read2String(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.flush();
		outSteam.close();
		inStream.close();
		return new String(outSteam.toByteArray(), CHARSET);
	}*/

}

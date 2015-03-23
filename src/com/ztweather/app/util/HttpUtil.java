package com.ztweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	public static void sendHttpRequest(final String address,final HttpCallbackListner listner){
		new Thread(new Runnable() {
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String buf = null;
					while((buf=reader.readLine())!=null){
						response.append(buf);
					}
					if(listner !=null){
						listner.onFinish(response.toString());
					}
				} catch (Exception e) {
					if(listner !=null){
						listner.onError(e);
					}
				}finally{
					if(connection !=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}

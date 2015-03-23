package com.ztweather.app.util;
/**
 *回调服务返回的结果
 * @author Administrator
 *
 */
public interface HttpCallbackListner {
	void onFinish(String response);
	
	void onError(Exception e);
}

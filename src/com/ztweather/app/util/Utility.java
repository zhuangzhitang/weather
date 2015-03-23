package com.ztweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.ztweather.app.db.ZtWeatherDB;
import com.ztweather.app.model.City;
import com.ztweather.app.model.County;
import com.ztweather.app.model.Province;

public class Utility {
 
	/*
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvinceResponse(ZtWeatherDB zDb,String response){
		if(!TextUtils.isEmpty(response)){	
			String[] provinces = response.split(",");
			if(provinces !=null && provinces.length>0){
				for(String p:provinces){
					String[] arry = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(arry[0]);
					province.setProvinceName(arry[1]);
					zDb.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 解析和處理服務器返回的市級數據
	 */
	public static boolean handleCitysResponse(ZtWeatherDB zDb,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] citys = response.split(",");
			if(citys != null && citys.length > 0){
				for(String c : citys){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					zDb.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 解析和处理服务器返回的县级数据
	 */
	public static boolean handleCountiesResponse(ZtWeatherDB zDb,String response,int cityId){
		if (!TextUtils.isEmpty(response)) {
			String[] counties = response.split(",");
			if (counties != null && counties.length > 0) {
				for (String c : counties ) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					zDb.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 解析服务器返回的json数据，并将解析的数据存储到本地
	 */
	public static void handleWeatherReponse(Context context,String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将服务器返回的所有天气信息存储到SharePreferences 文件
	 */
	public static void saveWeatherInfo(Context context,String cityName,
			String weatherCode,String temp1,String temp2,String weatherDesp,
			String publishTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
											.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2",temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
		
	}
}

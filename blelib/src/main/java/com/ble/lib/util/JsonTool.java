package com.ble.lib.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.text.TextUtils;
/**
 * JSON工具类。
 * @author E
 */
public class JsonTool {
	
	/**
	 * 将JSONObject的内容解析到HashMap容器中，只适合Value是String类型的JSONObject。
	 * @param jsonObject 解析的对象
	 * @return HashMap<String, String>解析后的内容
	 * @throws Exception 抛出的异常
	 */
	@SuppressWarnings({"rawtypes" })
	public static HashMap<String, String> parseJsonObjectOnlyString(JSONObject jsonObject) throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		Iterator iterator = jsonObject.keys();
		while (iterator.hasNext()) {
			//key---类型ID
			String key = iterator.next().toString();
			String value = jsonObject.getString(key);
			if (TextUtils.isEmpty(value)) {
				value = "";
			}
			map.put(key, value);
		}
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public static JSONObject map2JsonObject(HashMap<String, Object> map){
		JSONObject jsonObject = new JSONObject();
		try {
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = entry.getKey().toString();
				Object value = entry.getValue();
				if (null == value) {
					value = "";
				}
				jsonObject.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}

package com.hawk.gshassist.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.hawk.gshassist.application.AppApplication;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SharedPreUtil {
	
	private static final String TAG = "SharedPreUtil";
	
	private SharedPreferences sp;
	private Editor editor;
	private final static String SP_NAME = "HawkGSHAssist";
	private final static int MODE = Context.MODE_PRIVATE;

	public static final String EASCOOKIE="EASCookie";
	public static final String LIBCOOKIE="LibCookie";

	private static SharedPreUtil sharePreUtil;
	
	public static SharedPreUtil getInstance(){
		if(sharePreUtil == null){
			sharePreUtil = new SharedPreUtil(AppApplication.getInstance());
		}
		return sharePreUtil;
	}
	
	public SharedPreUtil(Context context) {
		sp = context.getSharedPreferences(SP_NAME, MODE|Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	public void clear(){
		editor.clear().commit();
	}

	public boolean save(String key, String value) {
		editor.putString(key, value);
		return editor.commit();
	}
	
	public boolean save(String key, boolean b) {
		editor.putBoolean(key, b);
		return editor.commit();
	}
	
	public boolean save(String key, int num) {
		editor.putInt(key, num);
		return editor.commit();
	}
	
	public <T> boolean saveList(String key, List<T> list) {
		JSONArray array = new JSONArray(list);
		LogUtil.i(TAG, "saveList " + array.toString());
		editor.putString(key, array.toString());
		return editor.commit();
	}
	
	
	public String read(String key) {
		String str = null;
		str = sp.getString(key, null);
		return str;
	}
	
	public String read(String key,String str) {
		str = sp.getString(key, str);
		return str;
	}
	
	public boolean read(String key,boolean b) {
		boolean get_b = false;
		get_b = sp.getBoolean(key, b);
		return get_b;
	}
	
	public int read(String key,int num) {
		int n = 0;
		n = sp.getInt(key, num);
		return n;
	}
	
	public JSONArray readArray(String key) {
		String str = null;
		str = sp.getString(key, null);
		LogUtil.i(TAG, "readArray:" + str);
		JSONArray array = new JSONArray();
		if(!TextUtils.isEmpty(str)){
			try {
				array = new JSONArray(str);
			} catch (JSONException e) {
				e.printStackTrace();
				return array;
			}
		}
		return array;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> List<T> readList(String key){
		
		List<T> list = new ArrayList<T>();
		
		JSONArray array = readArray(key);
		
		for(int i = 0; i < array.length(); i++){
			try {
				list.add((T) array.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
				return new ArrayList<T>();
			}
		}
		return list;
		
	}
}
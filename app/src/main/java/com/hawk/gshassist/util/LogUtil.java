package com.hawk.gshassist.util;

import android.util.Log;

/**
 * 日志打印类 
 */
public class LogUtil {

	private static final int VERBOSE = 1;

	private static final int DEBUG = 2;

	private static final int INFO = 3;

	private static final int WARN = 4;

	private static final int ERROR = 5;

	private static final int NOTHING = 6;

	//开发期间：LEVEL为VERBOSE, 输出全部调试日志
	//开发结束：LEVEL为NOTHINT, 关闭全部调试日志
	private static final int LEVEL = 1;

	public static void v(String TAG, String msg) {
		if (LEVEL <= VERBOSE) {
			Log.v(TAG, msg);
		}
	}

	public static void d(String TAG, String msg) {
		if (LEVEL <= DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void i(String TAG, String msg) {
		if (LEVEL <= INFO) {
			Log.i(TAG, msg);
		}
	}

	public static void w(String TAG, String msg) {
		if (LEVEL <= WARN) {
			Log.w(TAG, msg);
		}
	}

	public static void e(String TAG, String msg) {
		if (LEVEL <= ERROR) {
			Log.e(TAG, msg);
		}
	}


}

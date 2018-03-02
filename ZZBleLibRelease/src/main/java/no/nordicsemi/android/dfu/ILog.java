package no.nordicsemi.android.dfu;

import android.util.Log;
/**
 * Log类封装。
 * @author E
 */
public class ILog {

	private static final boolean DEBUG = true;
	private static final String IWY_TAG = "DFU";
	
	public static void e(String tag, String msg){
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String msg){
		if (DEBUG) {
			Log.e(IWY_TAG, msg);
		}
	}

	public static void i(String tag, String msg){
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}
	
	public static void i(String msg){
		if (DEBUG) {
			Log.i(IWY_TAG, msg);
		}
	}
	
	public static void d(String tag, String msg){
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}
	
	public static void d(String msg){
		if (DEBUG) {
			Log.d(IWY_TAG, msg);
		}
	}
	
	public static void w(String tag, String msg){
		if (DEBUG) {
			Log.w(tag, msg);
		}
	}
	
	public static void w(String msg){
		if (DEBUG) {
			Log.w(IWY_TAG, msg);
		}
	}
	
	public static void v(String tag, String msg){
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}
	
	public static void v(String msg){
		if (DEBUG) {
			Log.v(IWY_TAG, msg);
		}
	}

}

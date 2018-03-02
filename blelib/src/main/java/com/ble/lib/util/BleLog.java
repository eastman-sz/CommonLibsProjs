package com.ble.lib.util;

import android.util.Log;
/**
 * Created by E on 2017/12/7.
 */
public class BleLog {

    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "blelog";

    public static void e(String tag, String msg){
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg){
        if (DEBUG) {
            Log.e(LOG_TAG, msg);
        }
    }

    public static void i(String tag, String msg){
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String msg){
        if (DEBUG) {
            Log.i(LOG_TAG, msg);
        }
    }

    public static void d(String tag, String msg){
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg){
        if (DEBUG) {
            Log.d(LOG_TAG, msg);
        }
    }

    public static void w(String tag, String msg){
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void w(String msg){
        if (DEBUG) {
            Log.w(LOG_TAG, msg);
        }
    }

    public static void v(String tag, String msg){
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void v(String msg){
        if (DEBUG) {
            Log.v(LOG_TAG, msg);
        }
    }

}

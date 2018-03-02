package com.ble.lib.application;

import android.content.Context;
import com.ble.lib.dev.BleDevDbHepler;
/**
 * This method init(context) should invoked at app start (better in app application ).
 * Created by E on 2017/12/7.
 */
public class BleApplication {

    private static Context mContext = null;

    public static void init(Context context){
        mContext = context;
        BleDevDbHepler.initState();
    }

    public static Context getContext(){
        return mContext;
    }

}

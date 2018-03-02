package com.common.libs;

import android.app.Application;

import com.ble.lib.application.BleApplication;

/**
 * Created by E on 2017/12/7.
 */
public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BleApplication.init(this);
    }
}

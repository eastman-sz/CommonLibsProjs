package com.ble.lib.dfu;

import android.app.Activity;
import android.os.Bundle;

public class DfuNotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isTaskRoot()){

        }
        finish();
    }
}

package com.libs.module.ble.dfu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.ble.lib.dfu.DfuNotificationActivity;
import no.nordicsemi.android.dfu.DfuBaseService;

public class DfuService extends DfuBaseService {

	public static void actionStop(Context context){
		Intent intent = new Intent(context , DfuService.class);
		context.stopService(intent);
	}
	
	@Override
	protected Class<? extends Activity> getNotificationTarget() {
		
		return DfuNotificationActivity.class;
	}

}

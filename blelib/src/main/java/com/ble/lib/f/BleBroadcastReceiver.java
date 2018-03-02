package com.ble.lib.f;

import java.util.ArrayList;
import com.ble.lib.application.BleApplication;
import com.ble.lib.util.BleLog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
/**
 * Broadcast receiver for notifying system level status change.
 * @author E
 */
public class BleBroadcastReceiver extends BroadcastReceiver {

	private BleSearchHelper bleSearchHelper = null;

	@Override
	public void onReceive(Context context, Intent intent) {
	    if (null == bleSearchHelper){
	        bleSearchHelper = new BleSearchHelper(context , true , null);
        }

		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
		    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
		    switch (state) {
			case BluetoothAdapter.STATE_OFF:
				BleLog.e("--------bluetooth-is-off------------");
				
				onBleStateChanged(BleState.STATE_DISENABLED);
				break;
			case BluetoothAdapter.STATE_TURNING_OFF:
				BleLog.e("--------bluetooth-is-turning-off-----------");
				break;
			case BluetoothAdapter.STATE_ON:
				BleLog.e("--------bluetooth-is-turn-on-----------");
				
				onBleStateChanged(BleState.STATE_ENABLED);
				break;
			case BluetoothAdapter.STATE_TURNING_ON:
				BleLog.e("--------bluetooth-is-turning-on--------------");
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 状态变化。
	 * @param bleState
	 */
	private void onBleStateChanged(BleState bleState){
		ArrayList<BleStateListener> stateListeners = BleCallBackControl.getInstance().getStateListeners();
		synchronized (stateListeners) {
			for (BleStateListener stateListener : stateListeners) {
				stateListener.onStateChange(bleState, null);
			}
		}

		//blow code for p9 
		if (bleState == BleState.STATE_ENABLED) {
			if (null != bleSearchHelper){
                bleSearchHelper.startSearch();
            }
		}
	}

}

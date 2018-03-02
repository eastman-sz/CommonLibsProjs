package com.ble.lib.f;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * 蓝牙状态类。
 * @author E
 */
public class BleStatusHelper {

	public final static int REQUEST_ENABLE_BT = 0X12048;
	
	public static BleStatusHelper statusHelper = null;
	
	public static BleStatusHelper getInstance(){
		if (null == statusHelper) {
			statusHelper = new BleStatusHelper();
		}
		return statusHelper;
	}
	
	/**
	 * 判断蓝牙是否可用。
	 * @param context 上下文环境
	 * @return boolean
	 */
	public boolean isBleEnabled(Context context){
		boolean hasBleFeatures = hasBleFeatures(context);
		if (!hasBleFeatures) {
			return false;
		}
		boolean isBleOpened = isBleOpened(context);
		return isBleOpened;
	}
	
	/**
	 * 设备是否有BLE4.0。
	 * @param context 上下文环境
	 * @return True or False
	 */
	public boolean hasBleFeatures(Context context){
		if (null == context) {
			return false;
		}
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
	}
	
	/**
	 * 蓝牙是否打开。
	 * @param context 上下文环境
	 */
	public boolean isBleOpened(Context context){
		BluetoothAdapter bluetoothAdapter = getBlueToothAdapter(context);
		return null != bluetoothAdapter && bluetoothAdapter.isEnabled();
	}
	
	/**
	 * 打开蓝牙。
	 * @param context 上下文环境
	 */
	public void openBle(Context context){
	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    if (context instanceof Activity) {
	    	((Activity)context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}
	
	public void enableBle(Context context){
		BluetoothAdapter bluetoothAdapter = getBlueToothAdapter(context);
		if (null == bluetoothAdapter) {
			return;
		}
		bluetoothAdapter.enable();
	}
	
	public void disEnableBle(Context context){
		BluetoothAdapter bluetoothAdapter = getBlueToothAdapter(context);
		if (null == bluetoothAdapter) {
			return;
		}
		bluetoothAdapter.disable();
	}
	
	/**
	 * 蓝牙适配器。
	 * @param context
	 * @return
	 */
	public BluetoothAdapter getBlueToothAdapter(Context context){
		BluetoothManager bluetoothManager =(BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
		return bluetoothAdapter;
	}
	
}

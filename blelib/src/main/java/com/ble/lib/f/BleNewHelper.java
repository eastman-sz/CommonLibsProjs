package com.ble.lib.f;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;
import com.ble.lib.application.BleApplication;
import com.ble.lib.dev.BleDevInfo;
import com.ble.lib.util.BleLog;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Helper class for bluetooth.
 * @author E
 */
public class BleNewHelper {

	private static BleNewHelper bleHelper = null;
	//保存不同设备的BluetoothGatt
	private HashMap<String, BluetoothGatt> map = new HashMap<>();
	//保存Address-Name的映射
	private HashMap<String, String> addressNameMap = new HashMap<>();
	
	private Handler handler = new Handler(Looper.getMainLooper());
	
	public static BleNewHelper getInstance(){
		if (null == bleHelper) {
			bleHelper = new BleNewHelper();
		}
		return bleHelper;
	}
	
	
	public void startConnect(final Context context , String address , boolean autoConnect){
	    try {
            if (TextUtils.isEmpty(address)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "address can't be null , please check address again", Toast.LENGTH_SHORT).show();;
                    }
                });
                return;
            }

            boolean isBleEnable = BleStatusHelper.getInstance().isBleEnabled(context);
            if (!isBleEnable) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "The bluetooth of your device is closed or disabled , please check it", Toast.LENGTH_SHORT).show();;
                    }
                });
                return;
            }
            BluetoothAdapter adapter = BleStatusHelper.getInstance().getBlueToothAdapter(context);
            BluetoothDevice bluetoothDevice = null;
            try {
                bluetoothDevice = adapter.getRemoteDevice(address);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null == bluetoothDevice) {
                throw new NullPointerException("The remote device can't be found");
            }

            //更新库
            String name = bluetoothDevice.getName();
            BleDevInfo.updateDevState(address, name , BleState.STATE_CONNECTING.getState());
            //保存映射
            addAddressNameMapping(address, name);
            //回调
            onBleStateChanged(BleState.STATE_CONNECTING, address);

            BluetoothGatt bluetoothGatt = getExistBluetoothGatt(address);
            if (null == bluetoothGatt) {
                bluetoothGatt = bluetoothDevice.connectGatt(context, autoConnect, bluetoothGattCallback);
                //强制刷新
                refreshDeviceCache(bluetoothGatt);

                boolean readRemoteRssi =  bluetoothGatt.readRemoteRssi();

                BleLog.e("---startConnect-readRemoteRssi-0-: " + readRemoteRssi);

                addBluetoothGatt(address, bluetoothGatt);
                return;
            }
            //强制刷新
            refreshDeviceCache(bluetoothGatt);

            boolean readRemoteRssi =  bluetoothGatt.readRemoteRssi();
            if (!readRemoteRssi) {
                bluetoothGatt.connect();
            }
            BleLog.e("---startConnect-readRemoteRssi--: " + readRemoteRssi);
        }catch (Exception e){
	        e.printStackTrace();
        }
	}
	
	private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {
			BluetoothGattCallbackHelper.onCharacteristicChanged(gatt , characteristic);
		}
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
			BluetoothGattCallbackHelper.onCharacteristicRead(gatt , characteristic , status);
		}
		
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
			BluetoothGattCallbackHelper.onCharacteristicWrite(gatt , characteristic , status);
		}
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			onDevConnectionStateChange(gatt, status, newState);
		}
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			BluetoothGattCallbackHelper.onServicesDiscovered(gatt , status);
		}
	};

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onDevConnectionStateChange(final BluetoothGatt gatt, int status, final int newState){
	    final String address = gatt.getDevice().getAddress();
        final String name = gatt.getDevice().getName();
        if (status == BluetoothGatt.GATT_SUCCESS){
            if (newState == BluetoothProfile.STATE_CONNECTED){
                BleLog.e("-------连状态变化: 连接成功--------------------------");

                addBluetoothGatt(address, gatt);
                refreshDeviceCache(gatt);
                //Start to discover services when connected success, do it delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gatt.discoverServices();
                    }
                }, 600);


            }else if (newState == BluetoothProfile.STATE_DISCONNECTED){
                BleLog.e("-------状态变化: 断开成功--------------------------");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gatt.close();

                    }
                });

            }

        }else {
            //异常断开，准备重连....
            try {
                refreshDeviceCache(gatt);
                gatt.disconnect();
                final BluetoothGatt gatt1 = getExistBluetoothGatt(address);
                if (null != gatt1){
                    gatt1.disconnect();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gatt.close();
                        if (null != gatt1){
                            gatt1.close();
                        }
                        removeBluetoothGatt(address);
                    }
                } , 400);
            }catch (Exception e){
                e.printStackTrace();
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startConnect(BleApplication.getContext(), address , true);
                    BleLog.e("-----------异常断开，准备重连--------------");

                    onBleStateChange(BluetoothProfile.STATE_CONNECTING , name , address);
                }
            } , 1000);

        }

        BleLog.e("-------状态变化-------------status: " + status + "    newState: " + newState + "    " + gatt.getDevice().getName());

        handler.post(new Runnable() {
            @Override
            public void run() {
                onBleStateChange(newState , name , address);
            }
        });

	};

	private void onBleStateChange(int newState , String name , String address){
        BleState bleState = getBleState(newState);
        onBleStateChanged(bleState, address);
        BleDevInfo.updateDevState(address, name , bleState.getState());
    }

	private BleState getBleState(int newState){
	    if (newState == BluetoothProfile.STATE_CONNECTED){
	        return BleState.STATE_CONNECTED;
        }else if (newState == BluetoothProfile.STATE_CONNECTING){
	        return BleState.STATE_CONNECTING;
        }else if (newState == BluetoothProfile.STATE_DISCONNECTED){
	        return BleState.STATE_DISCONNECTED;
        }else {
            return BleState.STATE_DISCONNECTED;
        }
    }

	/**
	 * 断开某个设备。
	 * @param address 设备对应的地址
	 */
	public void disconnect(String address){
		try {
			BleLog.e("--------主动断开连接------------");
			//更新库
			BleDevInfo.updateDevState(address, BleState.STATE_DISCONNECTED.getState());
			//回调
			onBleStateChanged(BleState.STATE_DISCONNECTED, address);

			BluetoothGatt gatt = getExistBluetoothGatt(address);
			if (null == gatt) {
				return;
			}
			gatt.disconnect();
			refreshDeviceCache(gatt);
			//gatt.close();//在释放资源后，就没有系统回调了.如果长时间不释放资源，就会报status=133异常，会导致如果不重启蓝牙(甚至需要重启设备)就不能用了。
			gatt = null;

			removeBluetoothGatt(address);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//Fresh exist BluetoothGatt
	private boolean refreshDeviceCache(BluetoothGatt gatt){
	    try {
	    	if (null == gatt) {
				return false;
			}
	        BluetoothGatt localBluetoothGatt = gatt;
	        Method localMethod = localBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
	        if (localMethod != null) {
	           boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt, new Object[0])).booleanValue();
	            return bool;
	         }
	    } 
	    catch (Exception localException) {
			BleLog.e("An exception occured while refreshing device");
	    }
	    return false;
	}	
	
	private boolean isGattExist(String address){
		return map.containsKey(address);
	}
	
	private BluetoothGatt getExistBluetoothGatt(String address){
		if (isGattExist(address)) {
			return map.get(address);
		}
		return null;
	}
	
	private void removeBluetoothGatt(String address){
		if (isGattExist(address)) {
			map.remove(address);
		}
	}
	
	private void addBluetoothGatt(String address , BluetoothGatt gatt){
		map.put(address, gatt);
	}

	//保存地址和名字的映射
	private void addAddressNameMapping(String address , String name){
		addressNameMap.put(address, name);
	}
	
	/**
	 * 通过地址取得相应的名字。
	 * @param address 设备地址。
	 * @return 设备名字
	 */
	public String getName(String address){
		if (addressNameMap.containsKey(address)) {
			return addressNameMap.get(address);
		}
		return null;
	}
	
	/**
	 * 通过连接的地址，取得相应的BluetoothGatt。
	 * @param address 设备地址
	 * @return BluetoothGatt
	 */
	public BluetoothGatt getBluetoothGatt(String address){
		if (map.containsKey(address)) {
			return map.get(address);
		}
		return null;
	}

	private final static Object o = new Object();

	/**
	 * 在设备连接状态变化时调用。
	 * @param newState 新的状态
	 * @param address 设备地址
	 */
	private void onBleStateChanged(BleState newState , String address){
	    synchronized (o.getClass()){
            ArrayList<BleStateListener> stateListeners = BleCallBackControl.getInstance().getStateListeners();
            synchronized (stateListeners){
                for (BleStateListener listener : stateListeners){
                    listener.onStateChange(newState , address);
                }
            }
        }
	}

}

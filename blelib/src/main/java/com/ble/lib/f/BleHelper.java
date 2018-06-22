package com.ble.lib.f;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import com.ble.lib.application.BleApplication;
import com.ble.lib.dev.BleDevInfo;
import com.ble.lib.util.BleLog;
/**
 * Helper class for bluetooth.
 * @author E
 */
public class BleHelper {

	private static BleHelper bleHelper = null;
	//保存不同设备的BluetoothGatt
	private HashMap<String, BluetoothGatt> map = new HashMap<String, BluetoothGatt>();
	private List<String> list = new LinkedList<String>();
	//手动断开列表，手动断开后，不再自动连接
	private List<String> hand_disconnect_list = new ArrayList<String>();
	//保存Address-Name的映射
	private HashMap<String, String> addressNameMap = new HashMap<String, String>();
	
	private Handler handler = new Handler(Looper.getMainLooper());
	
	public static BleHelper getInstance(){
		if (null == bleHelper) {
			bleHelper = new BleHelper();
		}
		return bleHelper;
	}
	
	
	public void startConnect(final Context context , String address){
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

            //从手动断开里云除
            removeHandDisconnectList(address);


            BluetoothGatt bluetoothGatt = getExistBluetoothGatt(address);
            if (null == bluetoothGatt) {
                bluetoothGatt = bluetoothDevice.connectGatt(context, false, bluetoothGattCallback);
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
    private void onDevConnectionStateChange(final BluetoothGatt gatt, int status, int newState){
		//new add
		if (status == 133) {
			newState = BluetoothProfile.STATE_DISCONNECTED;
		}
		final String address = gatt.getDevice().getAddress();
		switch (newState) {
		case BluetoothProfile.STATE_CONNECTED:
			//连接成功
			addBluetoothGatt(address, gatt);
			removeHandDisconnectList(address);
			refreshDeviceCache(gatt);
			//Start to discover services when connected success, do it delay
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					gatt.discoverServices();
				}
			}, 600);
			break;
		case BluetoothProfile.STATE_CONNECTING:
			//正在连接
			break;
		case BluetoothProfile.STATE_DISCONNECTING:
			//正在断开
			break;
		case BluetoothProfile.STATE_DISCONNECTED:
			//已断开
			removeBluetoothGatt(address);
			
			//如果是手动断开，则不再自动连接。
			if (isDisconnectedByHand(address)) {
				gatt.close();
				
				BleLog.e("-------释放资源--------------------------");
				return;
			}

			//设备自动连接 //!hasRegisteredAutoConnect(address) &&
			if (BleStatusHelper.getInstance().isBleEnabled(BleApplication.getContext())) {
				BleLog.e("连接异常，自动断开重连");
                //add new
                Handler handler = new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 10101:
                                startConnect(BleApplication.getContext(), address);
                                break;
                            default:
                                break;
                        }
                    }
                };
                disconnect(address);
                registerAutoConnect(address);
                handler.sendEmptyMessageDelayed(10101, 600);
			}

			if (!BleStatusHelper.getInstance().isBleEnabled(BleApplication.getContext())) {
				disconnect(address);

				BleLog.e("--------蓝牙关闭后断开------------");
			}
			break;
		default:
			break;
		}
		BleLog.e("---------onDevConnectionStateChange---------:: " + status + "   : " + newState + " ::  " +
				(newState == BluetoothProfile.STATE_CONNECTED ?  "连接成功" : newState == BluetoothProfile.STATE_DISCONNECTED ? "已断开"
			    : newState == BluetoothProfile.STATE_CONNECTING ? "正在连接" : "未知 "));

		//更新库
		String name = gatt.getDevice().getName();
		//保存映射
		addAddressNameMapping(address, name);
		//回调
		switch (newState) {
		case BluetoothProfile.STATE_CONNECTED:
			onBleStateChanged(BleState.STATE_CONNECTED, address);
			
			BleDevInfo.updateDevState(address, name , BleState.STATE_CONNECTED.getState());
			break;
		case BluetoothProfile.STATE_CONNECTING:
			onBleStateChanged(BleState.STATE_CONNECTING, address);

			BleDevInfo.updateDevState(address, name , BleState.STATE_CONNECTING.getState());
			break;
		case BluetoothProfile.STATE_DISCONNECTING:
			break;
		case BluetoothProfile.STATE_DISCONNECTED:
			onBleStateChanged(BleState.STATE_DISCONNECTED, address);

			BleDevInfo.updateDevState(address, name , BleState.STATE_DISCONNECTED.getState());
			break;
		default:
			break;
		}
	};

	/**
	 * 断开某个设备。
	 * @param address 设备对应的地址
	 */
	public void disconnect(String address){
		try {
			BleLog.e("--------主动断开连接------------");

			addHandDisconnectList(address);
			//更新库
			BleDevInfo.updateDevState(address, BleState.STATE_DISCONNECTED.getState());
			//回调
			onBleStateChanged(BleState.STATE_DISCONNECTED, address);
			//fresh temp
			unRegisterAutoConnect(address);
//		addHandDisconnectList(address);

			BluetoothGatt gatt = getExistBluetoothGatt(address);
			if (null == gatt) {
				return;
			}
			gatt.disconnect();
			refreshDeviceCache(gatt);
			gatt.close();//在释放资源后，就没有系统回调了.如果长时间不释放资源，就会报status=133异常，会导致如果不重启蓝牙(甚至需要重启设备)就不能用了。
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
	
	private void registerAutoConnect(String address){
		if (hasRegisteredAutoConnect(address)) {
			return;
		}
	    list.add(address); 
	}
	
	private boolean hasRegisteredAutoConnect(String address){
		return list.contains(address);
	}
	
	private void unRegisterAutoConnect(String address){
		if (hasRegisteredAutoConnect(address)) {
			list.remove(address);
		}
	}
	
	/**
	 * 加入到手动断开列表。
	 * @param address 加入手动断开列表的设备地址
	 */
	private void addHandDisconnectList(String address){ 
		if (hand_disconnect_list.contains(address)) {
			return;
		}
		hand_disconnect_list.add(address);
	}
	/**
	 * 从手动断开列表移除。
	 * @param address 加入手动断开列表的设备地址
	 */
	private void removeHandDisconnectList(String address){
		if (hand_disconnect_list.contains(address)) {
			hand_disconnect_list.remove(address);
		}
	}
	/**
	 * 是否是手动断开。
	 * @param address 手动断开列表的设备地址
	 * @return True or False. 
	 */
	public boolean isDisconnectedByHand(String address){
		return hand_disconnect_list.contains(address);
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
	
	/**
	 * 取得最后一个连接设备的地址。
	 * @return 设备的地址
	 */
	public String getLastConnectedAddress(){
		int size = list.size();
		if (0 == size) {
			return null;
		}
		return list.get(size - 1);
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

package com.ble.lib.f;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.ble.lib.util.BleLog;

import java.util.ArrayList;
/**
 * Created by E on 2017/12/8.
 */
public class BluetoothGattCallbackHelper {

    public final static void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
        ArrayList<BleServiceListener> listeners = BleCallBackControl.getInstance().getServiceListeners();
        synchronized (listeners){
            for (BleServiceListener listener : listeners){
                listener.onCharacteristicChanged(gatt , characteristic);
            }
        }
    }

    public final static void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status){
        ArrayList<BleServiceListener> listeners = BleCallBackControl.getInstance().getServiceListeners();
        synchronized (listeners){
            for (BleServiceListener listener : listeners){
                listener.onCharacteristicRead(gatt , characteristic , status);
            }
        }
    }

    public final static void onCharacteristicWrite(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status){
        ArrayList<BleServiceListener> listeners = BleCallBackControl.getInstance().getServiceListeners();
        synchronized (listeners){
            for (BleServiceListener listener : listeners){
                listener.onCharacteristicWrite(gatt , characteristic , status);
            }
        }
    }

    public final static void onConnectionStateChange(BluetoothGatt gatt, int status, int newState){
        ArrayList<BleServiceListener> listeners = BleCallBackControl.getInstance().getServiceListeners();
        synchronized (listeners){
            for (BleServiceListener listener : listeners){
                listener.onConnectionStateChange(gatt , status , newState);
            }
        }
    }

    public final static void onServicesDiscovered(BluetoothGatt gatt, int status){
        ArrayList<BleServiceListener> listeners = BleCallBackControl.getInstance().getServiceListeners();
        synchronized (listeners){
            for (BleServiceListener listener : listeners){
                listener.onServicesDiscovered(gatt , status);

                BleLog.i("---服务回调---: " + listener.getClass().toString());
            }
        }
    }

    public final static void onDataReceived(int data_type , String address , String value){
        ArrayList<BleDataReceiveListener> dataReceiveListeners = BleCallBackControl.getInstance().getDataReceiveListeners();
        int dev_type = BleTypeControl.getInstance().getDevType(address);
        synchronized (dataReceiveListeners){
            for (BleDataReceiveListener listener : dataReceiveListeners){
                listener.onDataReceived(dev_type , data_type ,address , value);
            }
        }
    }

}

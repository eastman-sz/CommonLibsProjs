package com.ble.lib.f;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
/**
 * Created by E on 2017/12/07.
 */
public class BleDevUtils {

    /**
     * Get system bounded and connected BluetoothDevice.
     * @return BluetoothDevice list
     */
    public static ArrayList<BluetoothDevice> getSystemConnectedDevs(){
        ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try{
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = Integer.valueOf(String.valueOf(method.invoke(adapter, (Object[]) null)));
            if(state == BluetoothAdapter.STATE_CONNECTED){
                Set<BluetoothDevice> sets = adapter.getBondedDevices();

                for (BluetoothDevice bluetoothDevice : sets){
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = Boolean.valueOf(String.valueOf(isConnectedMethod.invoke(bluetoothDevice, (Object[]) null)));
                    if (isConnected){
                        list.add(bluetoothDevice);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

}

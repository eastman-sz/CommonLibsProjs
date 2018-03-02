package com.ble.lib.impl;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Looper;

import com.ble.lib.f.BleDataType;
import com.ble.lib.f.BleServiceListener;
import com.ble.lib.f.BluetoothGattCallbackHelper;
import com.ble.lib.f.BleCharacteristicHelper;
import com.ble.lib.f.CommonUuids;
import com.ble.lib.util.BleLog;
import com.ble.lib.util.JsonTool;
import java.util.HashMap;
import java.util.UUID;

/**
 * LD 2 Device.
 * Created by E on 2017/12/11.
 */
public class Ld2BleServiceListener implements BleServiceListener {

    //for saving LD 2 data
    private HashMap<String, Object> ld2DataMap = new HashMap<String, Object>();

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        try{
            String address = gatt.getDevice().getAddress();

            UUID CHARACTERISTIC_UUID = characteristic.getUuid();
            byte[] data = characteristic.getValue();

            if (CHARACTERISTIC_UUID.equals(CommonUuids.SP102_CHARACTER_4_UUID)){
                if (null == data || data.length < 4) {
                    return;
                }

                int hr = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1);
                int step = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 2);
                int activitys = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 3);

                //以JSON形式返回数据
                ld2DataMap.put("hr", hr);
                ld2DataMap.put("steps", step);
                ld2DataMap.put("activitys", activitys);
                //回调数据
                String json_data = JsonTool.map2JsonObject(ld2DataMap).toString();

                BleLog.e("---律动二代new---: " + json_data);

                BluetoothGattCallbackHelper.onDataReceived(BleDataType.LD_DATA, address, json_data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

    }

    @Override
    public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BleCharacteristicHelper.notifyCharacteristic(gatt, CommonUuids.SP102_SERVICE_UUID, CommonUuids.SP102_CHARACTER_4_UUID);
            }
        } , 1500);

    }

    private Handler handler = new Handler(Looper.myLooper());
}

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
import com.ble.lib.util.CommonBleUtils;
import com.ble.lib.util.JsonTool;
import java.util.HashMap;
import java.util.UUID;
/**
 * Created by E on 2017/12/11.
 */
public class Ld1BleServiceListener implements BleServiceListener {

    //For saving LD 1 data
    private HashMap<String, Object> ld1Map = new HashMap<String, Object>();

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        try {
            String address = gatt.getDevice().getAddress();

            UUID CHARACTERISTIC_UUID = characteristic.getUuid();
            byte[] data = characteristic.getValue();

            if (CHARACTERISTIC_UUID.equals(CommonUuids.STEAMING_DATA_UUID)){
                if (data.length != 4) {
                    return;
                }
                int hr = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1);
                int step = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 2);
                int activitys = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 3);

                //以JSON形式返回数据
                ld1Map.put("hr", hr);
                ld1Map.put("steps", step);
                ld1Map.put("activitys", activitys);
                //回调数据
                String json_data = JsonTool.map2JsonObject(ld1Map).toString();

                BleLog.e("---律动一代new---: " + json_data);

                BluetoothGattCallbackHelper.onDataReceived(BleDataType.LD_DATA, address, json_data);

            }else if (CHARACTERISTIC_UUID.equals(CommonUuids.BURST_DATA_UUID)){
                //一代设备离线数据
                String hexString = CommonBleUtils.byteArray2Hex(data);

                BleLog.e("---律动一代_离线new---: " + hexString);
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
        //实时数据
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BleCharacteristicHelper.notifyCharacteristic(gatt, CommonUuids.DATA_UUID, CommonUuids.STEAMING_DATA_UUID);
            }
        } , 1500);

        //取离线
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BleCharacteristicHelper.notifyCharacteristic(gatt, CommonUuids.DATA_UUID, CommonUuids.BURST_DATA_UUID);
            }
        } , 1900);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String hexCommand = "0403A27469FE";
                byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
                BleCharacteristicHelper.writeCommandToCharacteristic(gatt, CommonUuids.DATA_UUID, CommonUuids.MESSAGE_UUID , command);
            }
        } , 2500);
    }

    private Handler handler = new Handler(Looper.myLooper());

}

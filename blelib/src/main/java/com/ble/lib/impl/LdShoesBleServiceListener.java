package com.ble.lib.impl;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

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
 * Ld shoes.
 * Created by E on 2017/12/11.
 */
public class LdShoesBleServiceListener implements BleServiceListener {
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
       try {
           String address = gatt.getDevice().getAddress();

           UUID CHARACTERISTIC_UUID = characteristic.getUuid();

           if (CHARACTERISTIC_UUID.equals(CommonUuids.LD_SHOES_CHARACTER_1_UUID)){
               //shoe's steps
               byte[] data = characteristic.getValue();
               String result = CommonBleUtils.byteArray2Hex(data);

               int steps = Integer.valueOf(result , 16);

               BleLog.d("-----LD_Shoes-Steps------:: "  + result + "  : " + steps);

               BluetoothGattCallbackHelper.onDataReceived(BleDataType.LDShoesSteps, address, String.valueOf(steps));

           }else if (CHARACTERISTIC_UUID.equals(CommonUuids.LD_SHOES_CHARACTER_2_UUID)){
               //shoe's action
               byte[] data = characteristic.getValue();
               if (data.length != 2) {
                   return;
               }
               String result = CommonBleUtils.byteArray2Hex(data);

               if (TextUtils.isEmpty(result) || result.length() != 4) {
                   return;
               }
               String type = result.substring(0, 2);
               String id = result.substring(2, 4);
               int actionType = Integer.valueOf(type , 16);
               int actionId = Integer.valueOf(id , 16);

               //以JSON形式返回数据
               HashMap<String, Object> map = new HashMap<String, Object>();
               map.put("actionType", actionType);
               map.put("actionId", actionId);
               //回调数据
               String json_data = JsonTool.map2JsonObject(map).toString();

               BleLog.d("-----LD_Shoes-Actions------:: " + result + " Json: " + json_data);

               BluetoothGattCallbackHelper.onDataReceived(BleDataType.LDShoesAction, address, json_data);
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
                //shoe's steps
                BleCharacteristicHelper.notifyCharacteristic(gatt, CommonUuids.LD_SHOES_SERVICE_UUID, CommonUuids.LD_SHOES_CHARACTER_1_UUID);
            }
        } , 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //shoe's action
                BleCharacteristicHelper.notifyCharacteristic(gatt, CommonUuids.LD_SHOES_SERVICE_UUID, CommonUuids.LD_SHOES_CHARACTER_2_UUID);
            }
        } , 1500);
    }

    private Handler handler = new Handler(Looper.myLooper());

}

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
 * Shuhua treadmill.
 * Created by E on 2017/12/11.
 */
public class ShuHuaBleServiceListener implements BleServiceListener {
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        try {
            String address = gatt.getDevice().getAddress();

            UUID CHARACTERISTIC_UUID = characteristic.getUuid();
            if (CHARACTERISTIC_UUID.equals(CommonUuids.SHU_HUA_CHARACTER_1_UUID)){
                //主动读数据
                notifyAndWrite(false ,gatt , 10);

                byte[] data = characteristic.getValue();
                String result = CommonBleUtils.byteArray2Hex(data);

                if (TextUtils.isEmpty(result)){
                    return;
                }
                int length = result.length();
                if (length < 28){
                    return;
                }

                /**
                 * 0251035a 00a00001 00050000 00007ed2 03
                 * 02 起始码
                 * 51 指令码
                 * 03 运行中状态
                 * 5a 当前速度
                 * 00 当前坡度
                 * a000 正计时间
                 * 0100 正计距离
                 * 0500 正计热量
                 * 0000 正计步数
                 * 00 当前心率
                 * 7e 当前段数
                 * d2  校验码
                 * 03 终止码
                 */
                String speed_s = result.substring(6, 8);
                String slope_s = result.substring(8, 10);
                String step_s_1 = result.substring(22, 24);
                String step_s_2 = result.substring(24, 26);
                String step_s = step_s_2 + step_s_1;//采用倒序
                String hr_s = result.substring(26, 28);

                int speed = Integer.valueOf(speed_s, 16);
                int slope = Integer.valueOf(slope_s, 16);
                int step = Integer.valueOf(step_s, 16);
                int hr = Integer.valueOf(hr_s, 16);

                //以ＪＳＯＮ形式返回数据
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("speed", speed*0.1);
                map.put("slope", slope);
                map.put("steps", step);
                map.put("hr", hr);
                //回调数据
                String json_data = JsonTool.map2JsonObject(map).toString();

                BleLog.e("---舒华new---: " + json_data);

                BluetoothGattCallbackHelper.onDataReceived(BleDataType.SHUHUA_DATA, address, json_data);

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
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        notifyAndWrite(true ,gatt ,1500);
    }

    private Handler handler = new Handler(Looper.myLooper());

    private void notifyAndWrite(boolean notify , final BluetoothGatt gatt , int delayMillis){
        if (notify){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BleCharacteristicHelper.notifyCharacteristic(gatt, CommonUuids.SHU_HUA_SERVICE_UUID, CommonUuids.SHU_HUA_CHARACTER_1_UUID);
                }
            } , delayMillis);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String hexCommand = "02515103";
                byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);

                BleCharacteristicHelper.writeCommandToCharacteristic(gatt, CommonUuids.SHU_HUA_SERVICE_UUID, CommonUuids.SHU_HUA_CHARACTER_2_UUID , command);
            }
        } , delayMillis + 1000);
    }
}

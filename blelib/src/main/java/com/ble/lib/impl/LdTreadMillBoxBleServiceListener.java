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
import com.ble.lib.util.MathHelper;
import java.util.UUID;
/**
 * 律动跑步机盒子。
 * Created by E on 2017/12/11.
 */
public class LdTreadMillBoxBleServiceListener implements BleServiceListener {

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        try {
            String address = gatt.getDevice().getAddress();

            UUID CHARACTERISTIC_UUID = characteristic.getUuid();
            byte[] data = characteristic.getValue();
            if (CHARACTERISTIC_UUID.equals(CommonUuids.LD_TREADMILL_BOX_CHARACTER_1_UUID)){
                String result = CommonBleUtils.byteArray2Hex(data);

                String speed_1 = result.substring(0, 2);
                String speed_2 = result.substring(2, 4);
                int actionSpeed = Integer.valueOf(speed_2+speed_1 , 16);

                float speed = MathHelper.divideF(MathHelper.multiplyF(actionSpeed, 3.6f), 1000);

                BleLog.e("----律动跑步机盒子-----:  " + actionSpeed + "        -------->    " + speed);

                //回调数据
                BluetoothGattCallbackHelper.onDataReceived(BleDataType.LD_TREADMILL, address, String.valueOf(speed));
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
                BleCharacteristicHelper.notifyCharacteristic(gatt, CommonUuids.LD_TREADMILL_BOX_SERVICE_UUID, CommonUuids.LD_TREADMILL_BOX_CHARACTER_1_UUID);
            }
        } , 1500);
    }

    private Handler handler = new Handler(Looper.myLooper());

}

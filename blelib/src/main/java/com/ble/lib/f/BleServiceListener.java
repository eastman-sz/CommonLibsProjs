package com.ble.lib.f;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
/**
 * Created by E on 2017/12/7.
 */
public interface BleServiceListener {

    void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic);

    void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status);

    void onCharacteristicWrite(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status);

    void onConnectionStateChange(BluetoothGatt gatt, int status, int newState);

    void onServicesDiscovered(BluetoothGatt gatt, int status);

}

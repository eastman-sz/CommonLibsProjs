package com.ble.lib.f;

import android.bluetooth.BluetoothGatt;
import android.os.Handler;
import android.os.Looper;
/**
 * Created by E on 2017/12/8.
 */
public class BleCommonServicesDiscoveredHelper {

    public static void onServicesDiscovered(final BluetoothGatt gatt, int status){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readBatteryLevel(gatt);
            }
        } , 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyHRateData(gatt);
            }
        } , 900);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readSystemID(gatt);
            }
        } , 4500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readSoftwareRevision(gatt);
            }
        } , 4900);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readHardwareRevision(gatt);
            }
        } , 5400);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readSerialNumber(gatt);
            }
        } , 6000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readModelNumber(gatt);
            }
        } , 6500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readManufactureName(gatt);
            }
        } , 7000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readFirmwareVersion(gatt);
            }
        } , 7500);
    }

    static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Read battery level
     * @param gatt BluetoothGatt
     */
    public static boolean readBatteryLevel(BluetoothGatt gatt){
        return BleCharacteristicHelper.readCharacteristic(gatt, CommonUuids.BATTERY_SERVICE_UUID, CommonUuids.Battery_LEVEL_UUID);
    }

    /**
     * SoftwareRevision
     * @param gatt BluetoothGatt
     */
    public static void readSystemID(BluetoothGatt gatt){
        BleCharacteristicHelper.readCharacteristic(gatt, CommonUuids.SYSTEM_INFO_SERVICE_UUID, CommonUuids.SYSTEM_ID_CHARACTER_UUID);
    }

    /**
     * SoftwareRevision
     * @param gatt BluetoothGatt
     */
    public static void readSoftwareRevision(BluetoothGatt gatt){
        BleCharacteristicHelper.readCharacteristic(gatt, CommonUuids.SYSTEM_INFO_SERVICE_UUID, CommonUuids.SOFTWARE_VERSION_CHARACTER_UUID);
    }

    /**
     * readSerialNumber
     * @param gatt BluetoothGatt
     */
    public static void readHardwareRevision(BluetoothGatt gatt){
        BleCharacteristicHelper.readCharacteristic(gatt, CommonUuids.SYSTEM_INFO_SERVICE_UUID, CommonUuids.HARDWARE_REVISION_CHARACTER_UUID);
    }

    /**
     * readSerialNumber
     * @param gatt BluetoothGatt
     */
    public static void readSerialNumber(BluetoothGatt gatt){
        BleCharacteristicHelper.readCharacteristic(gatt, CommonUuids.SYSTEM_INFO_SERVICE_UUID, CommonUuids.SERIAL_NUMBER_CHARACTER_UUID);
    }

    /**
     * ModelNumber
     * @param gatt BluetoothGatt
     */
    public static void readModelNumber(BluetoothGatt gatt){
        BleCharacteristicHelper.readCharacteristic(gatt, CommonUuids.SYSTEM_INFO_SERVICE_UUID, CommonUuids.MODEL_NUMBER_CHARACTER_UUID);
    }

    /**
     * 读取工厂名
     * @param gatt BluetoothGatt
     */
    public static void readManufactureName(BluetoothGatt gatt){
        BleCharacteristicHelper.readCharacteristic(gatt, CommonUuids.SYSTEM_INFO_SERVICE_UUID, CommonUuids.MANUFACTURE_NAME_CHARACTER_UUID);
    }

    /**
     * 读取固件版本号
     * @param gatt BluetoothGatt
     */
    public static void readFirmwareVersion(BluetoothGatt gatt){
        BleCharacteristicHelper.readCharacteristic(gatt, CommonUuids.SYSTEM_INFO_SERVICE_UUID, CommonUuids.FIRMWARE_VERSION_CHARACTER_UUID);
    }

    /**
     * 公共心率
     * @param gatt BluetoothGatt
     */
    public static void notifyHRateData(BluetoothGatt gatt){
        BleCharacteristicHelper.notifyCharacteristic(gatt, CommonUuids.HEART_RATE_SERVICE_UUID, CommonUuids.HEART_RATE_CHARACT_UUID);
    }

}

package com.ble.lib.impl;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.ble.lib.dev.BleDevInfo;
import com.ble.lib.f.BleDataType;
import com.ble.lib.f.BleServiceListener;
import com.ble.lib.f.BleTempDataHelper;
import com.ble.lib.f.BluetoothGattCallbackHelper;
import com.ble.lib.f.CommonUuids;
import com.ble.lib.f.BleCommonServicesDiscoveredHelper;
import com.ble.lib.util.BleLog;
import com.ble.lib.util.CommonBleUtils;

import java.util.List;
import java.util.UUID;
/**
 * Basic service listener for common data.
 * Created by E on 2017/12/8.
 */
public class CommonBleServiceListener implements BleServiceListener {

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        try {
            String address = gatt.getDevice().getAddress();
            UUID CHARACTERISTIC_UUID = characteristic.getUuid();

            if (CommonUuids.HEART_RATE_CHARACT_UUID.equals(CHARACTERISTIC_UUID)){
                //standard heart rate
                int flag = characteristic.getProperties();
                int format = -1;
                if ((flag & 0x01) != 0) {
                    format = BluetoothGattCharacteristic.FORMAT_UINT16;
                    BleLog.d( "Heart rate format UINT16.");
                } else {
                    format = BluetoothGattCharacteristic.FORMAT_UINT8;
                    BleLog.d("Heart rate format UINT8.");
                }
                final int heartRate = characteristic.getIntValue(format, 1);

                BleLog.d("-------Standard-heart-rate----------:: " + address + "  :  " + heartRate);

                //回调数据
                BluetoothGattCallbackHelper.onDataReceived(BleDataType.STANDARD_HR, address, String.valueOf(heartRate));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        try {
            String address = gatt.getDevice().getAddress();
            UUID CHARACTERISTIC_UUID = characteristic.getUuid();
            byte[] data = characteristic.getValue();

            String hexString = CommonBleUtils.byteArray2Hex(data);

            if (CommonUuids.Battery_LEVEL_UUID.equals(CHARACTERISTIC_UUID)){
                //battery level
                int batteryLevel = Integer.valueOf(hexString, 16);

                BleLog.e("-----battery-level----: address: "  + address  + "   batterylevel: " + batteryLevel);

                //回调数据
                BluetoothGattCallbackHelper.onDataReceived(BleDataType.BATTERY_LEVEL, address, String.valueOf(batteryLevel));

                //temp
                BleTempDataHelper.getInstance().putData(address , CHARACTERISTIC_UUID.toString() , String.valueOf(batteryLevel));

            }else if (CommonUuids.FIRMWARE_VERSION_CHARACTER_UUID.equals(CHARACTERISTIC_UUID)){
                //Firmware version
                String msg = new String(hexString.getBytes());
                String firmwareVersion = CommonBleUtils.hexStr2Str(msg);

                BleLog.e("-----firmwareVersion----: address: "  + address  + "   firmwareVersion: " + firmwareVersion);

                //回调数据
                BluetoothGattCallbackHelper.onDataReceived(BleDataType.FIRMWARE_REVISION, address, String.valueOf(firmwareVersion));
                //更新库
                BleDevInfo.updateVersion(address , firmwareVersion);

                //temp
                BleTempDataHelper.getInstance().putData(address , CHARACTERISTIC_UUID.toString() , String.valueOf(firmwareVersion));

            }else if (CommonUuids.MANUFACTURE_NAME_CHARACTER_UUID.equals(CHARACTERISTIC_UUID)){
                //Manufacture Name
                String msg = new String(hexString.getBytes());
                String manufactureName = CommonBleUtils.hexStr2Str(msg);

                BleLog.e("-----manufactureName----: address: "  + address  + "   manufactureName: " + manufactureName);

                //temp
                BleTempDataHelper.getInstance().putData(address , CHARACTERISTIC_UUID.toString() , String.valueOf(manufactureName));

            }else if (CommonUuids.MODEL_NUMBER_CHARACTER_UUID.equals(CHARACTERISTIC_UUID)){
                //Model Number
                String msg = new String(hexString.getBytes());
                String modelNumber = CommonBleUtils.hexStr2Str(msg);

                BleLog.e("-----modelNumber----: address: "  + address  + "   modelNumber: " + modelNumber);

                //temp
                BleTempDataHelper.getInstance().putData(address , CHARACTERISTIC_UUID.toString() , String.valueOf(modelNumber));

            }else if (CommonUuids.SERIAL_NUMBER_CHARACTER_UUID.equals(CHARACTERISTIC_UUID)){
                //Serial Number
                String msg = new String(hexString.getBytes());
                String serialNumber = CommonBleUtils.hexStr2Str(msg);

                BleLog.e("-----serialNumber----: address: "  + address  + "   serialNumber: " + serialNumber);

                //回调数据
                BluetoothGattCallbackHelper.onDataReceived(BleDataType.SERIAL_NUMBER, address, String.valueOf(serialNumber));

                //更新库
                BleDevInfo.updateSn(address , serialNumber);

                //temp
                BleTempDataHelper.getInstance().putData(address , CHARACTERISTIC_UUID.toString() , String.valueOf(serialNumber));

            }else if (CommonUuids.HARDWARE_REVISION_CHARACTER_UUID.equals(CHARACTERISTIC_UUID)){
                //Hardware Revision
                String msg = new String(hexString.getBytes());
                String hardwareRevision = CommonBleUtils.hexStr2Str(msg);

                BleLog.e("-----hardwareRevision----: address: "  + address  + "   hardwareRevision: " + hardwareRevision);

                //temp
                BleTempDataHelper.getInstance().putData(address , CHARACTERISTIC_UUID.toString() , String.valueOf(hardwareRevision));

            }else if (CommonUuids.SOFTWARE_VERSION_CHARACTER_UUID.equals(CHARACTERISTIC_UUID)){
                //SOFTWARE version
                String msg = new String(hexString.getBytes());
                String softwareVersion = CommonBleUtils.hexStr2Str(msg);

                BleLog.e("-----softwareVersion----: address: "  + address  + "   softwareVersion: " + softwareVersion);

                //temp
                BleTempDataHelper.getInstance().putData(address , CHARACTERISTIC_UUID.toString() , String.valueOf(softwareVersion));

            }else if (CommonUuids.SYSTEM_ID_CHARACTER_UUID.equals(CHARACTERISTIC_UUID)){
                //SYSTEM ID

                BleLog.e("-----SYSTEM-ID----: address: "  + address  + "   SYSTEM-ID: " + hexString);

                //temp
                BleTempDataHelper.getInstance().putData(address , CHARACTERISTIC_UUID.toString() , String.valueOf(hexString));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        BleLog.e("---------onDevServicesDiscovered---------:: " + status);

        List<BluetoothGattService> services = gatt.getServices();
        for (BluetoothGattService service : services) {

            UUID uuid = service.getUuid();
            BleLog.e("----服务----: " + uuid.toString() + "  type:  " +  service.getType());
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                BleLog.e("---------------------特征-------： " + characteristic.getUuid());
            }
        }

        BleCommonServicesDiscoveredHelper.onServicesDiscovered(gatt ,status);

    }


}

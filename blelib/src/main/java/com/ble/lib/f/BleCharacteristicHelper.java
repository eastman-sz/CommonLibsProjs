package com.ble.lib.f;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import com.ble.lib.application.BleApplication;
import java.util.List;
import java.util.UUID;
/**
 * Created by E on 2017/12/8.
 */
public class BleCharacteristicHelper {
    /**
     * Read basic characteristic ,eg.battery,version ,model number
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     * @return
     */
    public static boolean readCharacteristic(BluetoothGatt gatt , UUID serviceUUID , UUID characteristicUUID){
        try {
            if (null == gatt) {
                return false;
            }
            if (!BleStatusHelper.getInstance().isBleEnabled(BleApplication.getContext())) {
                return false;
            }
            BluetoothGattService service = gatt.getService(serviceUUID);
            if (null == service) {
                return false;
            }
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            if (null == characteristic) {
                return false;
            }
            return gatt.readCharacteristic(characteristic);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Notify characteristics
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     * @return
     */
    public static boolean notifyCharacteristic(BluetoothGatt gatt , UUID serviceUUID , UUID characteristicUUID){
        try {
            if (null == gatt) {
                return false;
            }
            BluetoothGattService service = gatt.getService(serviceUUID);
            if (null == service) {
                return false;
            }
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            if (null == characteristic) {
                return false;
            }
            boolean isSet = gatt.setCharacteristicNotification(characteristic, true);
            if (!isSet) {
                return false;
            }
            List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
            if(descriptorList != null && descriptorList.size() > 0) {
                for(BluetoothGattDescriptor descriptor : descriptorList){
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(descriptor);
                }
            }
            return true;

//	        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUIDs.NOTIFY_UUID);
//	        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//	        return gatt.writeDescriptor(descriptor);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Write command to characteristic.
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     * @param hexCommand
     * @return
     */
    public static boolean writeCommandToCharacteristic(BluetoothGatt gatt , UUID serviceUUID , UUID characteristicUUID ,String hexCommand){
        try {
            if (null == gatt) {
                return false;
            }
            BluetoothGattService service = gatt.getService(serviceUUID);
            if (null == service) {
                return false;
            }
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            if (null == characteristic) {
                return false;
            }
            characteristic.setValue(hexCommand);
            return gatt.writeCharacteristic(characteristic);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Write command to characteristic.
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     * @param command
     * @return
     */
    public static boolean writeCommandToCharacteristic(BluetoothGatt gatt , UUID serviceUUID , UUID characteristicUUID ,byte[] command){
        try {
            if (null == gatt) {
                return false;
            }
            BluetoothGattService service = gatt.getService(serviceUUID);
            if (null == service) {
                return false;
            }
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            if (null == characteristic) {
                return false;
            }
            characteristic.setValue(command);
            return gatt.writeCharacteristic(characteristic);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

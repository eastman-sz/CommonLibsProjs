package com.ble.lib.impl;

import android.bluetooth.BluetoothGatt;
import com.ble.lib.f.BleCharacteristicHelper;
import com.ble.lib.f.CommonUuids;
import com.ble.lib.util.CommonBleUtils;
/**
 * Created by E on 2017/12/15.
 */
public class BleCommandHelper {

    public static void ld1WriteCommandForDfu(BluetoothGatt gatt){
        String hexCommand = "0402A27469FE";
        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , CommonUuids.DATA_UUID, CommonUuids.MESSAGE_UUID , command);
    }

    public static void ld2WriteCommandForDfu(BluetoothGatt gatt){
        String hexCommand = "04FE42AE7956";
        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , CommonUuids.SP102_SERVICE_UUID, CommonUuids.SP102_CHARACTER_1_UUID , command);
    }


}

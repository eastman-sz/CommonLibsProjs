package com.ble.lib.f;

/**
 * Ble data receive callback.
 * Created by E on 2017/12/11.
 */
public interface BleDataReceiveListener {
    /**
     * Invoked data received.
     * @param dev_type The type of device
     * @param data_type The type of data
     * @param address The address of device
     * @param value The value received
     */
    void onDataReceived(int dev_type , int data_type , String address , String value);

}

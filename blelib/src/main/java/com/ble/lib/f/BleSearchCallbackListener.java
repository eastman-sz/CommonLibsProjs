package com.ble.lib.f;

import android.bluetooth.BluetoothDevice;
/**
 * This callback will be invoked when ble device found.
 * Created by E on 2017/12/7.
 */
public abstract class BleSearchCallbackListener {

    /**
     * 开始搜索
     */
    public void onSearchStart(){};
    /**
     * 结束搜索
     */
    public void onSearchEnd(){};
    /**
     * 搜索结果
     * @param device 设备
     * @param rssi 信号
     * @param scanRecord 广播
     */
    public abstract void onSearchResult(BluetoothDevice device, int rssi, byte[] scanRecord);

}

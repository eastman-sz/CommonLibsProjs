package com.ble.lib.f;

/**
 * Created by E on 2017/12/7.
 */
public interface BleStateListener {

    void onStateChange(BleState bleState , String address);

}

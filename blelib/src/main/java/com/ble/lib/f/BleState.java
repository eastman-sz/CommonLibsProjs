package com.ble.lib.f;

/**
 * Created by E on 2017/12/7.
 */
public enum BleState {

    STATE_DISCONNECTED(1),
    STATE_CONNECTING(2),
    STATE_CONNECTED(3),
    STATE_SCAN_TIME_OUT(4),
    STATE_ENABLED(5),
    STATE_DISENABLED(6),
    STATE_SEARCHING(7),
    STATE_SEARCHING_END(8);

    int state = 0;

    BleState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

}

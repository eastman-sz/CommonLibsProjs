package com.ble.lib.f;

/**
 * Created by E on 2017/12/7.
 */
public class BleConfig {

    private static final BleConfig ourInstance = new BleConfig();

    //User id
    private int uid = 0;
    //是否是单个连接
    private boolean singleConnector = false;

    public static BleConfig getInstance() {
        return ourInstance;
    }

    private BleConfig() {
    }

    public BleConfig setUid(int uid) {
        this.uid = uid;
        return this;
    }

    public int getUid() {
        return uid;
    }

    public BleConfig setSingleConnector(boolean singleConnector) {
        this.singleConnector = singleConnector;
        return this;
    }

    public boolean isSingleConnector() {
        return singleConnector;
    }
}

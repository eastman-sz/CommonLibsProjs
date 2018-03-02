package com.libs.module.ble.tzcheng;

import java.util.UUID;

/**
 * 1代体脂秤。
 * Created by E on 2018/1/5.
 */
public class TzchengUUid {

    public final static UUID TZC_SERVICE_UUID = UUID.fromString("0000fc00-0000-1000-8000-00805f9b34fb");

    public final static UUID TZC_CHARACTOR_1_UUID = UUID.fromString("0000fc20-0000-1000-8000-00805f9b34fb");

    public final static UUID TZC_CHARACTOR_2_UUID = UUID.fromString("0000fc21-0000-1000-8000-00805f9b34fb");
    //notify
    public final static UUID TZC_CHARACTOR_3_UUID = UUID.fromString("0000fc22-0000-1000-8000-00805f9b34fb");
    //write
    public final static UUID TZC_CHARACTOR_4_UUID = UUID.fromString("0000fc23-0000-1000-8000-00805f9b34fb");

}

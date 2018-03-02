package com.libs.module.ble.bracelet;

import java.util.UUID;
/**
 * 手环UUID。
 * Created by E on 2018/1/15.
 */
public class BraceletUUid {

    public final static UUID BRACELET_SERVICE_UUID = UUID.fromString("0000fd00-0000-1000-8000-00805f9b34fb");

    public final static UUID BRACELET_CHARACTOR_1_UUID = UUID.fromString("0000fd09-0000-1000-8000-00805f9b34fb");

    public final static UUID BRACELET_CHARACTOR_2_UUID = UUID.fromString("0000fd0a-0000-1000-8000-00805f9b34fb");
    //notify
    public final static UUID BRACELET_CHARACTOR_3_UUID = UUID.fromString("0000fd19-0000-1000-8000-00805f9b34fb");
    //write
    public final static UUID BRACELET_CHARACTOR_4_UUID = UUID.fromString("0000fd1a-0000-1000-8000-00805f9b34fb");

}

package com.ble.lib.f;

import java.util.UUID;
/**
 * Created by E on 2017/12/7.
 */
public class CommonUuids {
    /**
     * 心率服务的UUID
     */
    public final static UUID HEART_RATE_SERVICE_UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    /**
     * 系统信息UUID
     */
    public final static UUID SYSTEM_INFO_SERVICE_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    /**
     * 心率CHARACTERISTICE的UUID
     */
    public final static UUID HEART_RATE_CHARACT_UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    /**
     * 电量服务的UUID
     */
    public final static UUID BATTERY_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    /**
     * 电量级别的UUID
     */
    public static final UUID Battery_LEVEL_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
    /**
     * 设置通知的UUID
     */
    public static final UUID NOTIFY_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    /**
     * 版本号
     */
    public static final UUID DFU_VERSION = new UUID(0x000015341212EFDEl, 0x1523785FEABCD123l);

    //-------------------------------系统信息下的_CHARACTER_UUID---------------------------------------------------------------
    /**
     * Manufacture Name UUID
     */
    public static final UUID MANUFACTURE_NAME_CHARACTER_UUID = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
    /**
     * Model Number UUID
     */
    public static final UUID MODEL_NUMBER_CHARACTER_UUID = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
    /**
     * Serial Number UUID
     */
    public static final UUID SERIAL_NUMBER_CHARACTER_UUID = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
    /**
     * Hardware Revision UUID
     */
    public static final UUID HARDWARE_REVISION_CHARACTER_UUID = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
    /**
     * Firmware version UUID
     */
    public static final UUID FIRMWARE_VERSION_CHARACTER_UUID = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    /**
     * SOFTWARE version UUID
     */
    public static final UUID SOFTWARE_VERSION_CHARACTER_UUID = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");
    /**
     * SYSTEM ID UUID
     */
    public static final UUID SYSTEM_ID_CHARACTER_UUID = UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb");

    //---------------------------律动一代设备----------------------------------------
    /**
     * 数据UUID,服务ID
     */
    public static final UUID DATA_UUID = UUID.fromString("2368ef00-8536-9909-8f61-428eda81288d");
    /**
     * Characteristic UUID
     */
    public static final UUID MESSAGE_UUID          = UUID.fromString("2368ef01-8536-9909-8f61-428eda81288d");
    public static final UUID MESSAGE_RESPONSE_UUID = UUID.fromString("2368ef02-8536-9909-8f61-428eda81288d");
    public static final UUID BURST_DATA_UUID       = UUID.fromString("2368ef03-8536-9909-8f61-428eda81288d");
    public static final UUID STEAMING_DATA_UUID    = UUID.fromString("2368ef04-8536-9909-8f61-428eda81288d");


    //---------------------------律动二代设备----------------------------------------
    /**
     * 服务ＩＤ。
     */
    public static final UUID SP102_SERVICE_UUID = UUID.fromString("585c7160-7910-4329-b01b-a37888cdee94");
    /**
     * 特征1。 读写特征
     */
    public static final UUID SP102_CHARACTER_1_UUID = UUID.fromString("585c7161-7910-4329-b01b-a37888cdee94");
    /**
     * 特征1。响应特征
     */
    public static final UUID SP102_CHARACTER_2_UUID = UUID.fromString("585c7162-7910-4329-b01b-a37888cdee94");
    /**
     * 特征1。存储数据特征
     */
    public static final UUID SP102_CHARACTER_3_UUID = UUID.fromString("585c7163-7910-4329-b01b-a37888cdee94");
    /**
     * 特征1。 时实数据特征
     */
    public static final UUID SP102_CHARACTER_4_UUID = UUID.fromString("585c7164-7910-4329-b01b-a37888cdee94");

    //---------------------------special-for-LD-跑步机盒子----------------------------------------
    /**
     * 服务ＩＤ。
     */
    public static final UUID LD_TREADMILL_BOX_SERVICE_UUID = UUID.fromString("00001814-0000-1000-8000-00805f9b34fb");
    /**
     * 特征1。监听.speed
     */
    public static final UUID LD_TREADMILL_BOX_CHARACTER_1_UUID = UUID.fromString("00002a55-0000-1000-8000-00805f9b34fb");

    //---------------------------special-for-shuhua----------------------------------------
    /**
     * 服务ＩＤ。
     */
    public static final UUID SHU_HUA_SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    /**
     * 特征1。监听
     */
    public static final UUID SHU_HUA_CHARACTER_1_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    /**
     * 特征2。写数据
     */
    public static final UUID SHU_HUA_CHARACTER_2_UUID = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");

    //---------------------------special-for-LD-SHOES----------------------------------------
    /**
     * 服务ＩＤ。
     */
    public static final UUID LD_SHOES_SERVICE_UUID = UUID.fromString("0000f000-0000-1000-8000-00805f9b34fb");
    /**
     * 特征1。监听.steps
     */
    public static final UUID LD_SHOES_CHARACTER_1_UUID = UUID.fromString("0000f112-0000-1000-8000-00805f9b34fb");
    /**
     * 特征2。监听.actions
     */
    public static final UUID LD_SHOES_CHARACTER_2_UUID = UUID.fromString("0000f116-0000-1000-8000-00805f9b34fb");

}

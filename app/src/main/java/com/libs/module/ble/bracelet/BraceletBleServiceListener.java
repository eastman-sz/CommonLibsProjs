package com.libs.module.ble.bracelet;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ble.lib.application.BleApplication;
import com.ble.lib.f.BleCharacteristicHelper;
import com.ble.lib.f.BleServiceListener;
import com.ble.lib.f.BluetoothGattCallbackHelper;
import com.ble.lib.f.DevType;
import com.ble.lib.util.CommonBleUtils;
import com.common.libs.util.ILog;
import com.libs.module.ble.tizhicheng.TzcMath;
import java.text.MessageFormat;
import java.util.UUID;
/**
 * Created by E on 2018/1/15.
 */
public class BraceletBleServiceListener implements BleServiceListener {

    private String address = null;
    //数据结构
    private BraceletData braceletData = new BraceletData();
    private BluetoothGatt gatt = null;

    public BraceletBleServiceListener(){
        registerBraceletBroadcastReceiver();
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        try{
            UUID CHARACTERISTIC_UUID = characteristic.getUuid();

            if (CHARACTERISTIC_UUID.equals(BraceletUUid.BRACELET_CHARACTOR_3_UUID)){
                String name = gatt.getDevice().getName();
                address = gatt.getDevice().getAddress();

                byte[] data = characteristic.getValue();
                String result = CommonBleUtils.byteArray2Hex(data);
                if (TextUtils.isEmpty(result)){
                    return;
                }
                ILog.e("----手环characteristicChanged---: " + name + " ----  " + result);
                int length = result.length();
                if (length < 40){
                    ack(gatt , result);

                }else {
                    //实时步数、卡路里、距离
                    parseData(result);

                }
            }

        }catch (Exception e){

        }
    }

    private void ack(BluetoothGatt gatt , String result){
        //取得应用层包序号(2个字节)
        String packSeq = result.substring(6 , 10);
        ILog.e("----手环_应用层包序号---: " + " ----  " + packSeq);
        //应答(FF2006 + 应用层包序号 + checksum)
        String hexCommandPre = "FF2006" + packSeq;
        String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);

        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);

        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , BraceletUUid.BRACELET_SERVICE_UUID , BraceletUUid.BRACELET_CHARACTOR_4_UUID , command);
    }

    /**
     * 同步时间.
     * @param gatt BluetoothGatt
     */
    private void updateUTC(BluetoothGatt gatt){
        long systime = System.currentTimeMillis()/1000;
        int offset =  java.util.TimeZone.getDefault().getRawOffset()/1000;//时区OffsetTIme
        long newSetTime = systime + offset;//转换为0时区时间

        String hexCommandPre = "FF000F00060110080104" + CommonBleUtils.integral2Hex((int)newSetTime);
        String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);
        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , BraceletUUid.BRACELET_SERVICE_UUID , BraceletUUid.BRACELET_CHARACTOR_4_UUID , command);
    }

    //打开实时数据通道(步数，卡路里，距离等)
    private void openRealData(BluetoothGatt gatt){
        String hexCommandPre = "FF000C0001011004010101";
        String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);
        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , BraceletUUid.BRACELET_SERVICE_UUID , BraceletUUid.BRACELET_CHARACTOR_4_UUID , command);
    }

    private void sendMsgToBracelet(String msg , int type , final BluetoothGatt gatt){
        if (TextUtils.isEmpty(msg)){
            return;
        }
/*        String msg = "您好";
        String hexMsg = CommonBleUtils.byteArray2Hex(msg.getBytes());
        String keyVal = "07030801E682A8";
        int msgByteLength = keyVal.length()/2;
        int totalByteLength = 11 + msgByteLength;
        String hexCommandPre0 = "FF00{0}000802100201{1}" + keyVal;
        String hexCommandPre = MessageFormat.format(hexCommandPre0 , CommonBleUtils.integral2Hex(totalByteLength),CommonBleUtils.integral2Hex(msgByteLength));
        String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);
        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , BraceletUUid.BRACELET_SERVICE_UUID , BraceletUUid.BRACELET_CHARACTOR_4_UUID , command);

        ILog.e("发送的消息命令: " + hexCommand);*/

//        String msg = "您";
        //最多显示60个字节
        int lenght = msg.length();
        if (lenght > 20){
            msg = msg.substring(0 , 20);
        }
        String hexMsgType = CommonBleUtils.integral2Hex(type);

        String hexMsg = CommonBleUtils.byteArray2Hex(msg.getBytes());
        int hexMsgByteLeng = hexMsg.length()/2;
        //第一条指令最多只能放入5个字节的内容.如果大于5个字节，则要分包发送
        if (hexMsgByteLeng > 5){
            //先发第一条
            String hexMsg0 = hexMsg.substring(0 , 10);
            String hexCommandPre0 = "FF8014000802100201{0}09{1}0801{2}";
            int totalKeyvalLeng = 4 + hexMsgByteLeng;
            String hexCommandPre = MessageFormat.format(hexCommandPre0 , CommonBleUtils.integral2Hex(totalKeyvalLeng), hexMsgType ,hexMsg0);

            writeCommand(gatt , hexCommandPre);

            //多包，第二条起，每条最多可发送16个字节的内容
            int hexMsgLeftLength = hexMsgByteLeng - 5;
            int num = hexMsgLeftLength/16;
            int mo = hexMsgLeftLength%16;
            int leftNum = 0 == mo ? num : (num + 1);

/*                  if (1 == leftNum){
                int totalLength = 4 + hexMsgLeftLength;
                String msgLeft = hexMsg.substring(10);
                String hexCommandPre00 = "FFC1{0}" + msgLeft;
                String hexCommandPreL = MessageFormat.format(hexCommandPre00 , CommonBleUtils.integral2Hex(totalLength));

                writeCommand(gatt , hexCommandPreL);
                return;
            }*/

            int startIndex = 10;
            int lastItem = leftNum - 1;//最后一条
            int startFlag = 128;//标识符
            for (int i = 0; i < leftNum ; i++){
                //如果不是最后一条，每条都是20个字节
                if (i < lastItem){
                    startFlag ++;
                    String hexFlag = CommonBleUtils.integral2Hex(startFlag);
                    //单条内容，每条16个字节
                    int newStartIndex = startIndex+i*32;
                    String hexMsgSingle = hexMsg.substring(newStartIndex , newStartIndex + 32);

                    String hexCommandPres = "FF" + hexFlag + "14" + hexMsgSingle;

                    writeCommand(gatt , hexCommandPres);

                }else {
                    //最后一条
                    String hexMsgLeft = hexMsg.substring(startIndex+i*32);
                    int msgLeftByteLength = hexMsgLeft.length()/2;
                    int leftTotalLenght = 4 + msgLeftByteLength;
                    String hexLeftTotalLenght = CommonBleUtils.integral2Hex(leftTotalLenght);

                    //一条c1,二条c2类推
                    String hexSeq = CommonBleUtils.integral2Hex(192 + leftNum);

                    String hexCommandPres = "FF" + hexSeq + hexLeftTotalLenght + hexMsgLeft;

                    writeCommand(gatt , hexCommandPres);
                }
            }
        }else {
            String keyValHexLength = CommonBleUtils.integral2Hex(4 + hexMsgByteLeng);
            String keyVal = keyValHexLength + hexMsgType + "0801" + hexMsg;//hexMsgType --- 03微信 ,04QQ ,00未接来电 ,02Sms

            int msgByteLength = keyVal.length()/2;
            int totalByteLength = 11 + msgByteLength;
            String hexCommandPre0 = "FF00{0}000802100201{1}" + keyVal;
            String hexCommandPre = MessageFormat.format(hexCommandPre0 , CommonBleUtils.integral2Hex(totalByteLength),CommonBleUtils.integral2Hex(msgByteLength));

            writeCommand(gatt , hexCommandPre);
        }
    }

    private void sendNewPushMsg2Bracelet(final String msg , final int type){
        if (TextUtils.isEmpty(address)){
            return;
        }
        ILog.e("收到QQ或微信消息0-------");

        if (null == gatt){
            return;
        }
        ILog.e("收到QQ或微信消息1-------");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openRealData(gatt);
            }
        } , 100);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMsgToBracelet(msg , type , gatt);
            }
        } , 600);
    }

    private void writeCommand(BluetoothGatt gatt , String hexCommandPre){
        try{
            Thread.sleep(800);
        }catch (Exception e){
            e.printStackTrace();
        }
        String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);
        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , BraceletUUid.BRACELET_SERVICE_UUID , BraceletUUid.BRACELET_CHARACTOR_4_UUID , command);

        ILog.e("发送的消息命令: " + hexCommand);
    }

    /**
     * 解析实时数据。实时步数、卡路里、距离
     * @param result
     */
    private void parseData(String result){
        try {
            String stepsHex = result.substring(20 , 26);
            String distanceHex = result.substring(26 , 32);
            String calorieHex = result.substring(32 , 38);

            ILog.e("stepsHex: " + stepsHex + "     calorieHex: " + calorieHex + "     distanceHex: " + distanceHex);

            int steps = CommonBleUtils.hex2Integral(stepsHex);
            int distance = CommonBleUtils.hex2Integral(distanceHex);
            int calorie = CommonBleUtils.hex2Integral(calorieHex);

            ILog.e("stepsHex: " + steps + "     calorieHex: " + calorie + "     distanceHex: " + distance);

            braceletData.setSteps(steps);
            braceletData.setDistance(distance);
            braceletData.setCalorie(calorie);

            String json = JSON.toJSONString(braceletData);
            //回调数据
            BluetoothGattCallbackHelper.onDataReceived(DevType.HW330A, address, json);

            ILog.e("手环Json: " + json);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

    }

    @Override
    public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
        this.gatt = gatt;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean success =  BleCharacteristicHelper.notifyCharacteristic(gatt, BraceletUUid.BRACELET_SERVICE_UUID, BraceletUUid.BRACELET_CHARACTOR_3_UUID);

                ILog.e("--手环Notify0---: " + success);

                if (success){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateUTC(gatt);
                        }
                    } , 1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            openRealData(gatt);
                        }
                    } , 1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendMsgToBracelet("", 3 ,gatt);
                        }
                    } , 2000);
                    return;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean success =  BleCharacteristicHelper.notifyCharacteristic(gatt, BraceletUUid.BRACELET_SERVICE_UUID, BraceletUUid.BRACELET_CHARACTOR_3_UUID);

                        ILog.e("--手环Notify1---: " + success);

                        if (success){
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateUTC(gatt);
                                }
                            } , 1000);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    openRealData(gatt);
                                }
                            } , 1000);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sendMsgToBracelet("" ,3, gatt);
                                }
                            } , 2000);
                        }
                    }
                } , 1000);
            }
        } , 1500);
    }

    private Handler handler = new Handler(Looper.myLooper());


    private class BraceletBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)){
                return;
            }
            if (action.equals("newBraceletPushMsgReceived")){
                String title = intent.getStringExtra("title");
                int type = intent.getIntExtra("type" , 0);

                sendNewPushMsg2Bracelet(title , type);
            }
        }
    }

    private BraceletBroadcastReceiver broadcastReceiver = null;

    private void registerBraceletBroadcastReceiver(){
        if (null == broadcastReceiver){
            broadcastReceiver = new BraceletBroadcastReceiver();

            IntentFilter filter = new IntentFilter();
            filter.addAction("newBraceletPushMsgReceived");

            BleApplication.getContext().registerReceiver(broadcastReceiver , filter);

        }
    }


}

package com.libs.module.ble.tzcheng;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ble.lib.f.BleServiceListener;
import com.ble.lib.f.BluetoothGattCallbackHelper;
import com.ble.lib.f.BleCharacteristicHelper;
import com.ble.lib.util.CommonBleUtils;
import com.common.libs.util.ILog;
import com.libs.module.ble.tizhicheng.TzcMath;
import com.utils.lib.ss.common.MathUtil;

import java.util.UUID;
/**
 * Created by E on 2018/1/5.
 */
public class TzchengBleServiceListener implements BleServiceListener {

    private String address = null;
    private boolean hasDataReceived = false;

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        try {
            String name = gatt.getDevice().getName();
            address = gatt.getDevice().getAddress();

            UUID CHARACTERISTIC_UUID = characteristic.getUuid();

            if (CHARACTERISTIC_UUID.equals(TzchengUUid.TZC_CHARACTOR_3_UUID)){
                byte[] data = characteristic.getValue();
                String result = CommonBleUtils.byteArray2Hex(data);
                if (TextUtils.isEmpty(result)){
                    return;
                }
                String start1Byte = result.substring(0 , 2);
                //FD-设备向APP发送指令
                if (start1Byte.equals("FD")){
                    parse(gatt , result);
                }


                ILog.e("----1代体脂秤characteristicChanged---: " + name + " ----  " + result);


            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 同步时间.
     * @param gatt BluetoothGatt
     */
    private void updateUTC(BluetoothGatt gatt){

        String hexCommandPre = "FE0801" + CommonBleUtils.integral2Hex((int)(System.currentTimeMillis()/1000));
        String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);
        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , TzchengUUid.TZC_SERVICE_UUID , TzchengUUid.TZC_CHARACTOR_4_UUID , command);
    }

    private TzchengData tzchengData = new TzchengData();

    /**
     * 设备命令列表
     * 0x 01------上传不稳定体重数据
     * 0x 02------上传稳定体重数据
     * 0x 03------上传体脂数据
     * 0x 04------上传设备基本信息
     * 0x 05------上传用户信息
     * 0x 06------上传设备时间
     * 0x 07------上传历史数据
     * 0x 63------发送错误号
     * 0x 64------应答命令
     * --------------------------------------------
     * APP 命令列表
     * 0x 01------时间设置
     * 0x 02------同步用户信息
     * 0x 03------设备参数设置
     * 0x 04------请求设备基本信息
     * 0x 05------添加、删除、指定用户信息
     * 0x 64------应答命令
     * @param result
     */
    private void parse(BluetoothGatt gatt , String result){
        //数据格式 包头1byte + 长度 + 命令(CmdId)1byte + Data(0~16 bytes) + 校验和(1byte)
        String cmdId = result.substring(4 , 6);
        if (cmdId.equals("64")){
            hasDataReceived = false;
            //应答
            String hexCommandPre = "FE056464";
            String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);
            byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
            BleCharacteristicHelper.writeCommandToCharacteristic(gatt , TzchengUUid.TZC_SERVICE_UUID , TzchengUUid.TZC_CHARACTOR_4_UUID , command);

        }else if (cmdId.equals("02")){
            //上传稳定体重数据
            String weightHex = result.substring(6 , 10);

            int weightHexValue = Integer.valueOf(weightHex, 16);
            float weight = MathUtil.divideF(weightHexValue , 10);

            ILog.e("体重: " + weight + "  公斤");

        }else if (cmdId.equals("03")){
            //上传体脂数据--分两条返回
            //第一条:FD14032101AF1C0103190000FE0201001F022161
            /**
             * 第一条解析格式
             * FD    //包头
             * 14    //长度
             * 03    //命令
             * 21    //第7：4bit   表示总包数 /第3：0bit   表示包序号
             * 01    //用户号
             * AF    //身高
             * 1C    //年龄
             * 01    //性别    0：女 1 ：男
             * 0319  //体重（0.1 单位）
             * 00    //单位00:kg ,01:lb ,02:ST, 03:斤
             * 00FE  //脂肪率（百分比，单位 0.1）
             * 0201  //水分（百分比，单位 0.1）
             * 001F  //骨头（单位 0.1KG）
             * 0221  //肌肉（百分比，单位 0.1）
             * 61    //校验和
             */
            //第二条:FD0B032205F40112000B44
            /**
             * 第二条解析格式
             * FD    //包头
             * 0B    //长度
             * 03    //命令
             * 22    //第7：4bit   表示总包数 /第3：0bit   表示包序号
             * 05F4  //卡路里（单位 kcal ）
             * 0112  //BMI （单位 0.1）
             * 000B  //内脏脂肪（单位 1）
             * 44    //校验和
             */
            int length = result.length();
            if (40 == length){
                String uidHex = result.substring(8 , 10);
                String heightHex = result.substring(10 , 12);
                String ageHex = result.substring(12 , 14);
                String sexHex = result.substring(14 , 16);
                String weightHex = result.substring(16 , 20);
                String weightUnitHex = result.substring(20 , 22);
                String zhifangLvHex = result.substring(22 , 26);
                String shuifenHex = result.substring(26 , 30);
                String gutouHex = result.substring(30 , 34);
                String jirouHex = result.substring(34 , 38);

                int tuid = CommonBleUtils.hex2Integral(uidHex);
                int height = CommonBleUtils.hex2Integral(heightHex);
                int age = CommonBleUtils.hex2Integral(ageHex);
                int sex = CommonBleUtils.hex2Integral(sexHex);
                int weightHexValue = Integer.valueOf(weightHex, 16);
                float weight = MathUtil.divideF(weightHexValue , 10);
                int zhifangVal = Integer.valueOf(zhifangLvHex, 16);
                float zhifangLv = MathUtil.divideF(zhifangVal , 10);
                int shuifenVal = Integer.valueOf(shuifenHex, 16);
                float shuifen = MathUtil.divideF(shuifenVal , 10);
                int gutouVal = Integer.valueOf(gutouHex, 16);
                float gutou = MathUtil.divideF(gutouVal , 10);
                int jirouVal = Integer.valueOf(jirouHex, 16);
                float jirou = MathUtil.divideF(jirouVal , 10);
                float zhifang = MathUtil.multiplyF(weight , zhifangLv , 2)*0.01f;//脂肪

                tzchengData.setTuid(tuid);
                tzchengData.setHeight(height);
                tzchengData.setAge(age);
                tzchengData.setSex(sex);
                tzchengData.setWeight(weight);
                tzchengData.setZhifang(zhifang);
                tzchengData.setShuifen(shuifen);
                tzchengData.setGutou(gutou);
                tzchengData.setJirou(jirou);

                System.err.println("用户号: " + uidHex);
                System.err.println("身高: " + heightHex);
                System.err.println("年龄: " + ageHex);
                System.err.println("性别: " + sexHex);
                System.err.println("体重: " + weightHex);
                System.err.println("单位: " + weightUnitHex);
                System.err.println("脂肪: " + zhifang);
                System.err.println("水分: " + shuifenHex);
                System.err.println("骨头: " + gutouHex);
                System.err.println("肌肉: " + jirouHex);


            }else if (22== length){
                if (hasDataReceived){
                    return;
                }
                String kaluliHex = result.substring(8 , 12);
                String bmiHex = result.substring(12 , 16);
                String neiZangZhiFangHex = result.substring(16 , 20);

                int kaluli = Integer.valueOf(kaluliHex, 16);
                int bmiVal = Integer.valueOf(bmiHex, 16);
                float bmi = MathUtil.divideF(bmiVal , 10);
                int neiZangZhiFangVal = Integer.valueOf(neiZangZhiFangHex, 16);
                float neiZangZhiFang = MathUtil.divideF(neiZangZhiFangVal , 10);

                tzchengData.setKaluli(kaluli);
                tzchengData.setBmi(bmi);
                tzchengData.setNeiZangZhiFang(neiZangZhiFang);

                System.err.println("卡路里: " + kaluliHex);
                System.err.println("BMI: " + bmiHex);
                System.err.println("内脏脂肪: " + neiZangZhiFangHex);

                String joson = JSON.toJSONString(tzchengData);
                //回调数据
                BluetoothGattCallbackHelper.onDataReceived(21, address, joson);

                hasDataReceived = true;
            }

        }


    }


    /**
     * 通过判断命令ID(commandIdHex)来响应设备发送的指令。
     * 0x 01, 上传不稳定体重数据
     * 0x 02, 上传稳定体重数据
     * 0x 03, 上传体脂数据
     * 0x 04, 上传设备基本信息
     * 0x 05, 上传用户信息
     * 0x 06, 上传设备时间
     * 0x 07, 上传历史数据
     * 0x 63, 发送错误号
     * 0x 64, 应答命令
     * @param gatt
     */
    private void ackDev(BluetoothGatt gatt){
        //上传用户信息
        String hexCommandPre = "FE110211FFAA1E010000000000000000";
        String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);
        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , TzchengUUid.TZC_SERVICE_UUID , TzchengUUid.TZC_CHARACTOR_4_UUID , command);
    }

    /**
     * APP同步用户信息给设备
     * @param gatt
     */
    private void synUserInfo(BluetoothGatt gatt){
        //上传用户信息
        String hexCommandPre = "FE09050201AF1C01";
        String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);
        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);
        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , TzchengUUid.TZC_SERVICE_UUID , TzchengUUid.TZC_CHARACTOR_4_UUID , command);

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
        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ILog.e("--1代体脂秤Notify0---");

                    boolean success =  BleCharacteristicHelper.notifyCharacteristic(gatt, TzchengUUid.TZC_SERVICE_UUID, TzchengUUid.TZC_CHARACTOR_3_UUID);

                    if (success){
                        ILog.e("--1代体脂秤Notify0---: 成功");
       /*                 handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateUTC(gatt);
                            }
                        } , 1000);*/
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                synUserInfo(gatt);
                            }
                        } , 1000);
                        return;
                    }

                    ILog.e("--1代体脂秤Notify1---: " + success);

                    success =  BleCharacteristicHelper.notifyCharacteristic(gatt, TzchengUUid.TZC_SERVICE_UUID, TzchengUUid.TZC_CHARACTOR_3_UUID);

                    ILog.e("--1代体脂秤Notify2---: " + success);
                }
            } , 1500);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler(Looper.myLooper());


}

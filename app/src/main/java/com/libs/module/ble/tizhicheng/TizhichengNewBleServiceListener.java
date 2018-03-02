package com.libs.module.ble.tizhicheng;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.ble.lib.f.BleServiceListener;
import com.ble.lib.f.BluetoothGattCallbackHelper;
import com.ble.lib.f.BleCharacteristicHelper;
import com.ble.lib.util.CommonBleUtils;
import com.common.libs.util.ILog;
import com.google.gson.Gson;
import com.utils.lib.ss.common.MathUtil;
import java.util.UUID;
/**
 * Created by E on 2017/12/21.
 */
public class TizhichengNewBleServiceListener implements BleServiceListener {

    private String address = null;

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        try {
            String name = gatt.getDevice().getName();
            address = gatt.getDevice().getAddress();

            UUID CHARACTERISTIC_UUID = characteristic.getUuid();

            if (CHARACTERISTIC_UUID.equals(TzcUUid.TZC_CHARACTOR_3_UUID)){
                byte[] data = characteristic.getValue();
                String result = CommonBleUtils.byteArray2Hex(data);
                if (TextUtils.isEmpty(result)){
                    return;
                }
                int resultLength = result.length();
                if (resultLength < 6){
                    return;
                }
                ILog.e("----新款体脂秤characteristicChanged---: " + name + " ----  " + result);

                //整个数据长度（字节）,包括最后的校验位
                String lengthHex = result.substring(4 , 6);
                int length = CommonBleUtils.hex2Integral(lengthHex);

                //通过标识符（第2个字节）来判断是否需要拆包00无需拆包
                /**
                 *  标示符(1byte)
                 *  位序  Bit7   Bit6      Bit5          bit4  Bit3-bit0
                 *  定义  0  单包  0  继续包  0  没有带应答  保留  传输层包序号(0-F)
                 *       1  多包  1  结束包  1  带传输层应答
                 */
                 String flag = result.substring(2 , 4);
                 if (flag.equals("00")){
                     //00无需拆包
                     checkAck(gatt , result);

                     //通过key来判断是否是实时数据:01实时 02历史
                     if (length == 20){
                         String key = result.substring(14 , 16);
                         if (key.equals("01")){
                             String dataLengthHex = result.substring(18 , 20);
                             int dataLength = Integer.valueOf(dataLengthHex, 16);
                             String dataHex = result.substring(20 ,20 + dataLength*2);

                             ILog.e("----新款体脂秤_实时数据---: " + dataHex);

                             calResult(dataHex);

                         }
                     }

                 }else {
                     //需要拆包，需要判断多包继续包和多包结束包,也需要判断是否需要应答
                     String binary = CommonBleUtils.hex2BinaryString(flag , 8);
                     String binaryFirst2 = binary.substring(0 , 2);
                     if (binaryFirst2.equals("10")){
                         //多包继续包
                         checkAck(gatt , result);

                     }else if (binaryFirst2.equals("11")){
                         //多包结束包

                     }

                 }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 解析
    // ff00140005061001010918d80015fe14252b00a0
    /**
     ff 包头
     00 标识符
     14 长度
     0005
     06 CmdId
     10
     01 key
     01 应答
     09 值长度
     18d8 体重 kg 精度0.1
     00 单位 00:KG 01:LB 02 ST 03斤
     15fe 第一电极阻抗
     14252b 加密电极阻抗
     00 设备类型 00:LF
     a0 校验码
     */
    /**
     * 处理实时数据。
     * 05AA00000000000000
     * 2bytes 体重(KG)精度为 0.01
     * 1bytes 体重单位--00：表示KG 01：表示LB 02：表示ST 03：表示斤
     * 2bytes 第一电极阻抗（0.1Ω）
     * 3bytes 加密电极阻抗
     * 1bytes 设备类型 0x00：LF
     * @param dataHex
     */
    private void calResult(String dataHex){
        if (TextUtils.isEmpty(dataHex)){
            return;
        }
        int lenght = dataHex.length();
        if (lenght != 18){
            return;
        }
        String weightHex = dataHex.substring(0 , 4);
        String weightUnitHex = dataHex.substring(4 , 6);
        String impedanceHex = dataHex.substring(6 , 10);//第一电极阻抗（0.1Ω）
        String impedanceEnHex = dataHex.substring(10 , 16);//加密电极阻抗

        ILog.e("weightHex: " + weightHex + "    weightUnitHex: " + weightUnitHex + "    impedanceHex: " + impedanceHex + "    impedanceEnHex: " + impedanceEnHex);

        int weightHexValue = Integer.valueOf(weightHex, 16);
        float weight = MathUtil.divideF(weightHexValue , 100);

        int impedanceEn = Integer.valueOf(impedanceEnHex, 16);

        ILog.e("weightHex: " + weight + "    weightUnitHex: " + weightUnitHex + "    impedanceHex: " + impedanceHex + "    impedanceEnHex: " + impedanceEn);

        TzcBean tzcBean = new TzcBean(weight , impedanceEn);

        Gson gson = new Gson();

        String joson = gson.toJson(tzcBean);
        //回调数据
        BluetoothGattCallbackHelper.onDataReceived(20, address, joson);

    }


    /**
     * 判断是否需要应答--取key-header的第一个字节的末两bit来判断00,No Ack 01,确认Ack 10,数据ACK 11,保留
     * @param gatt
     * @param result
     * @return
     */
    private boolean checkAck(BluetoothGatt gatt ,String result){
        //
        String keyHeaderFirstByte = result.substring(16 , 18);
        //转换成2进制
        String binary = CommonBleUtils.hex2BinaryString(keyHeaderFirstByte , 8);
        String binarylast2 = binary.substring(6);
        if (binarylast2.equals("01")){
            //需要应答
            ack(gatt , result);

        }
        return false;
    }

    private void ack(BluetoothGatt gatt , String result){
        //取得应用层包序号(2个字节)
        String packSeq = result.substring(6 , 10);
        ILog.e("----新款体脂秤_应用层包序号---: " + " ----  " + packSeq);
        //应答(FF2006 + 应用层包序号 + checksum)
        String hexCommandPre = "FF2006" + packSeq;
        String hexCommand = hexCommandPre + TzcMath.calChecksum(hexCommandPre);

        byte[] command = CommonBleUtils.hexTextToBytes(hexCommand);

        BleCharacteristicHelper.writeCommandToCharacteristic(gatt , TzcUUid.TZC_SERVICE_UUID , TzcUUid.TZC_CHARACTOR_4_UUID , command);

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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ILog.e("--新体脂秤Notify0---");

               boolean success =  BleCharacteristicHelper.notifyCharacteristic(gatt, TzcUUid.TZC_SERVICE_UUID, TzcUUid.TZC_CHARACTOR_3_UUID);

               if (success){
                   return;
               }

               ILog.e("--新体脂秤Notify1---: " + success);

               success =  BleCharacteristicHelper.notifyCharacteristic(gatt, TzcUUid.TZC_SERVICE_UUID, TzcUUid.TZC_CHARACTOR_3_UUID);

               ILog.e("--新体脂秤Notify2---: " + success);
            }
        } , 1500);
    }

    private Handler handler = new Handler(Looper.myLooper());
}

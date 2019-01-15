package com.ble.lib.f;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.ble.lib.dev.BleDevDbHepler;
import com.ble.lib.dev.BleDevInfo;
import java.util.ArrayList;
/**
 * Main for ble connect and disconnect.
 * Created by E on 2017/12/14.
 */
public class BleActionNewControl {

    private final static BleActionNewControl bleActionControl = new BleActionNewControl();

    private BleActionNewControl(){
    }

    public static BleActionNewControl getInstance(){
        return bleActionControl;
    }

    private void startConnect(Context context , String address , boolean autoConnect){
        startConnect(context , 0 , address , autoConnect);
    }

    public void startConnect(Context context , String address){
        startConnect(context , 0 , address , false);
    }

    public void startAutoConnect(Context context , String address){
        startConnect(context , 0 , address , true);
    }

    public void startConnect(final Context context , final int dev_type , final String address , boolean autoConnect){
        //更新库设备类型
        BleDevInfo.updateDevType(address, dev_type);

        //如果是只允许单个连接（只能有一个连接），断开其他的已连接设备
        if (BleConfig.getInstance().isSingleConnector()){
            ArrayList<BleDevInfo> list = BleDevDbHepler.getActivedBleDevs();
            for (BleDevInfo devInfo : list){
                BleNewHelper.getInstance().disconnect(devInfo.getAddress());
            }
        }

        //同一个类型，只能连接一个设备
        boolean devTypeExist = BleTypeControl.getInstance().isDevTypeExist(dev_type);
        if (devTypeExist) {
            final String pre_address = BleTypeControl.getInstance().getPreAddressByDevType(dev_type);
            if (!pre_address.equals(address)) {
                Handler handler = new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 0:
                            {
                                BleNewHelper.getInstance().disconnect(pre_address);
                                BleTypeControl.getInstance().clearAddressMapping(pre_address);
                            }
                                break;
                            default:
                                break;
                        }
                    }
                };
                handler.sendEmptyMessageDelayed(0,  600);
            }
        }

        BleTypeControl.getInstance().addDevice(dev_type, address);
        BleNewHelper.getInstance().startConnect(context, address , autoConnect);
    }

    public void disconnect(String address){
        BleNewHelper.getInstance().disconnect(address);
        BleTypeControl.getInstance().removePreRecord(address);
    }




}

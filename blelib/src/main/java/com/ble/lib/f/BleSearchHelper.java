package com.ble.lib.f;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.ble.lib.dev.BleDevInfo;
import com.ble.lib.util.BleLog;
import java.util.ArrayList;
/**
 * Created by E on 2017/12/7.
 */
public class BleSearchHelper {

    private Context context = null;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothAdapter.LeScanCallback leScanCallback = null;
    private int SCAN_PERIOD = 1000*15;
    //auto stop search
    private boolean autoStopSearch = true;
    //callback
    private BleSearchCallbackListener bleSearchCallbackListener = null;
    //是否正在扫描设备
    private boolean scanning = false;
    //是否过滤在同一次搜索过程的相同设备
    private boolean filterSameDevice = false;

    //为了去重
    private ArrayList<BluetoothDevice> devList = new ArrayList<>();


    public BleSearchHelper(Context context ,boolean autoStopSearch , BleSearchCallbackListener bleSearchCallbackListener) {
        this.context = context;
        this.autoStopSearch = autoStopSearch;
        this.bleSearchCallbackListener = bleSearchCallbackListener;
    }

    public void setFilterSameDevice(boolean filterSameDevice) {
        this.filterSameDevice = filterSameDevice;
    }

    //不能小于15秒
    public void setScanPeriodTimeout(int timeout){
        if (timeout < 15*1000){
            return;
        }
        this.SCAN_PERIOD = timeout;
    }

    public void startSearch(){
        if (null == bluetoothAdapter) {
            bluetoothAdapter = BleStatusHelper.getInstance().getBlueToothAdapter(context);
        }
        if (null == bluetoothAdapter) {
            return;
        }
        if (scanning) {
            stopBleSearch();
        }
        scanning = true;
        devList.clear();

        initLeScanCallback();
        bluetoothAdapter.startLeScan(leScanCallback);

        sendSystemConnectedDevs();

        //低功耗搜索，15秒后停止
        if (autoStopSearch){
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0 , SCAN_PERIOD);
        }

        onSearchStart();
    }

    public void startSearchByDefault(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        initLeScanCallback();
        devList.clear();

        registerDefaultBleScanResultBroadCastAction();

        bluetoothAdapter.startDiscovery();

        sendSystemConnectedDevs();

        if (autoStopSearch){
            //系统扫描，50秒后关闭
            handler.removeMessages(1);
            handler.sendEmptyMessageDelayed(1, 1000*50);
        }

        onSearchStart();
    }

    private void initLeScanCallback(){
        if (null != leScanCallback){
            return;
        }
        leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                String name = device.getName();
                String address = device.getAddress();

                if (TextUtils.isEmpty(name)){
                    return;
                }
                if (TextUtils.isEmpty(address)){
                    return;
                }
                if (filterSameDevice){
                    if (devList.contains(device)){
                        return;
                    }
                    devList.add(device);
                }

                BleLog.d("搜索设备: " + "   ---  " + name + "   address: " +  device.getAddress());


                BleDevInfo.updateDevSearchInfo(address , name);

                //invoke
                onDevFound(device , rssi , scanRecord);
            }
        };
    }

    private void onDevFound(BluetoothDevice device, int rssi, byte[] scanRecord){
        if (null == bleSearchCallbackListener){
            return;
        }
        bleSearchCallbackListener.onSearchResult(device , rssi , scanRecord);
    }

    private void onSearchStart(){
        if (null == bleSearchCallbackListener){
            return;
        }
        bleSearchCallbackListener.onSearchStart();
    }

    private void onSearchEnd(){
        if (null == bleSearchCallbackListener){
            return;
        }
        bleSearchCallbackListener.onSearchEnd();
    }

    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                {
                    //auto stop scan
                    stopBleSearch();
                }
                    break;
                case 1:
                {
                    //auto stop default scan
                    cancelDiscovery();
                }
                    break;
                default:
                    break;
            }
        }
    };

    public void stopBleSearch(){
        if (!scanning) {
            return;
        }
        if (null == bluetoothAdapter || null == leScanCallback){
            return;
        }
        scanning = false;

        handler.removeMessages(0);

        bluetoothAdapter.stopLeScan(leScanCallback);
        leScanCallback = null;

        onSearchEnd();
    }

    //关闭系统蓝牙扫描
    public void cancelDiscovery(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
        //已经关闭，就不用再次去关闭了
        handler.removeMessages(1);

        unRegisterDefaultBleScanResultBroadCastAction();

        onSearchEnd();
    }

    //在使用bluetoothAdapter.startDiscovery()这种方式搜索蓝牙设备时，需要注册广播
    public ArrayList<String> registerDefaultBleScanResultBroadCastAction(){
        ArrayList<String> list = new ArrayList<String>();
//        list.add(BluetoothDevice.ACTION_FOUND);

        if (null == defSearchBroadCastReceiver){
            defSearchBroadCastReceiver = new DefSearchBroadCastReceiver();

            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(defSearchBroadCastReceiver , filter);
        }

        return list;
    }

    //在停止搜索时，注销广播
    private void unRegisterDefaultBleScanResultBroadCastAction(){
        if (null == defSearchBroadCastReceiver){
            return;
        }
        context.unregisterReceiver(defSearchBroadCastReceiver);
        defSearchBroadCastReceiver = null;
    }

    //在使用bluetoothAdapter.startDiscovery()这种方式搜索蓝牙设备时，在广播中返回搜索结果
    public void onBroadCastReceive(Context context, String action, Intent intent){
        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (null == leScanCallback){
                return;
            }
            leScanCallback.onLeScan(device, 0, null);
        }
    }

    private class DefSearchBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)){
                return;
            }
            onBroadCastReceive(context , action , intent);
        }
    }

    //系统搜索的广播
    private DefSearchBroadCastReceiver defSearchBroadCastReceiver = null;

    /**
     * Send BluetoothDevices connected by system as searched one.
     */
    private void sendSystemConnectedDevs(){
        if (null == leScanCallback){
            return;
        }
        ArrayList<BluetoothDevice> list = BleDevUtils.getSystemConnectedDevs();
        for (BluetoothDevice device : list) {
            leScanCallback.onLeScan(device, -50, null);
        }
    }

}

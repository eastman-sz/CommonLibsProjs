package com.libs.module.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.libs.proj.R;
import com.common.libs.util.ILog;
import com.utils.lib.ss.common.ToastHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/**
 * http://blog.csdn.net/lincyang/article/details/50739342
 * http://blog.csdn.net/true100/article/details/51791929
 * https://www.jianshu.com/p/5d4f280db7ad
 */
public class UsbActivity extends BaseAppCompactActivitiy {

    private UsbManager usbManager = null;

    private ListView listView = null;
    private UsbDeviceAdapter adapter = null;
    private ArrayList<UsbBean> list = new ArrayList<>();

    public static final String ACTION_DEVICE_PERMISSION = "com.linc.USB_PERMISSION";
    private PendingIntent mPermissionIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb);

        initActivitys();

        usbManager = (UsbManager)getSystemService(Context.USB_SERVICE);
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("USB");
        commonTitleView.setOnTitleClickListener(new CommonTitleView.OnTitleClickListener() {
            @Override
            public void onLeftBtnClick() {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        adapter = new UsbDeviceAdapter(context , list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UsbBean usbBean = list.get(position);

                UsbDevice usbDevice = usbBean.getUsbDevice();

                if (usbManager.hasPermission(usbDevice)){
                    ToastHelper.makeText(context , "有权限");

                    startActivity(new Intent(context , UsbFileListActivity.class)
                        .putExtra("path" , usbBean.getPath())
                    );

                }else {
                    ToastHelper.makeText(context , "没有权限");

                    usbManager.requestPermission(usbDevice, mPermissionIntent);
                }

            }
        });
    }



    private void getUsbList(){
        list.clear();

        HashMap<String,UsbDevice> deviceHashMap = usbManager.getDeviceList();
        Iterator<UsbDevice> iterator = deviceHashMap.values().iterator();
        while (iterator.hasNext()){
            UsbDevice device = iterator.next();

            String info = "\ndevice name: "+device.getDeviceName()+"\ndevice product name:"
                    +device.getProductName()+"\nvendor id:"+device.getVendorId()+
                    "\ndevice serial: "+device.getSerialNumber();
            ILog.e("usbInfo: " + info);

            UsbBean usbBean = new UsbBean();
            usbBean.setName(device.getDeviceName());
            usbBean.setUsbDevice(device);

            list.add(usbBean);
        }

        adapter.notifyDataSetChanged();
    }


    @Override
    protected ArrayList<String> addBroadCastAction() {
        ArrayList<String> list = new ArrayList<>();
        list.add(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        list.add(UsbManager.ACTION_USB_DEVICE_DETACHED);

        register();
        return list;
    }

    private void register(){
        mPermissionIntent = PendingIntent.getBroadcast(this,0,new Intent(ACTION_DEVICE_PERMISSION),0);
        IntentFilter iFilter = new IntentFilter(ACTION_DEVICE_PERMISSION);

        iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        iFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        iFilter.addDataScheme("file");

        registerReceiver(mUsbReceiver,iFilter);
    }

    @Override
    protected void onBroadCastReceive(Context context, String action, Intent intent) {
        if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)){
            ToastHelper.makeText(context , "发现USB设备");

            getUsbList();

        }else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)){
            ToastHelper.makeText(context , "USB设备断开");
        }
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_DEVICE_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            ToastHelper.makeText(context , "usb EXTRA_PERMISSION_GRANTED");
                        }
                    } else {
                        ToastHelper.makeText(context , "usb EXTRA_PERMISSION_GRANTED null!!!");
                    }
                }
            }else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)){
                String usbPath = intent.getDataString();

                ToastHelper.makeText(context , "路Path: " + usbPath);
                ILog.e("路Path: " + usbPath);

                if (TextUtils.isEmpty(usbPath)){
                    ILog.e("路Path: " + "未能获取");
                    return;
                }
                File usbfile = new File(usbPath);
                String usbDirFileName = usbfile.getName();

                ILog.e("路Path_usbDirFileName: " + usbDirFileName);

                File dirFile = new File("/storage/" + usbDirFileName);
                if (!dirFile.exists()){
                    ILog.e("路Path: " + "文件不存在");
                    return;
                }
                if (!list.isEmpty()){
                    list.get(0).setPath(dirFile.getAbsolutePath());
                    adapter.notifyDataSetChanged(listView , 0);
                }

                dirFile.setReadable(true);
                dirFile.setExecutable(true);

                File[] files = dirFile.listFiles();
                if (null == files){
                    ILog.e("路Path: " + "----------");
                    return;
                }
                int length = files.length;
                for (int i = 0 ; i < length ; i++){
                    File file = files[i];

                    ILog.e("文件名Path: " + file.getAbsolutePath() + "   Name: " + file.getName());

                }


            }
        }
    };
}

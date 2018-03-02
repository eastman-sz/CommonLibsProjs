package com.libs.module.ble.bracelet;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ble.lib.dev.BleDevInfo;
import com.ble.lib.f.BleActionControl;
import com.ble.lib.f.BleCallBackControl;
import com.ble.lib.f.BleDataReceiveListener;
import com.ble.lib.f.BleSearchCallbackListener;
import com.ble.lib.f.BleSearchHelper;
import com.ble.lib.f.BleState;
import com.ble.lib.f.BleStateListener;
import com.ble.lib.f.DevType;
import com.ble.lib.util.CommonBleUtils;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.base.CustomFontTextView;
import com.common.libs.proj.R;
import com.common.libs.util.ILog;
import com.libs.module.ble.BleDevInfoAdapter;
import java.util.ArrayList;

public class BraceletActivity extends BaseAppCompactActivitiy {

    private BraceletBleServiceListener braceletBleServiceListener = null;

    private CustomFontTextView dataTextView = null;
    private ListView listView = null;
    private BleDevInfoAdapter adapter = null;
    private ArrayList<BleDevInfo> list = new ArrayList<>();

    private BleSearchHelper bleSearchHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bracelet);

        initActivitys();

        startSearch();

        String hex = "021002011701030800e6b283e4b8965fe79b9be5b0913a2068676867";

        ILog.e("文字转换: " + CommonBleUtils.hexStr2Str(hex));

    }

    private void startSearch(){
        if (null == bleSearchHelper){
            bleSearchHelper = new BleSearchHelper(context , true , bleSearchCallbackListener);
        }
        bleSearchHelper.startSearch();
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("体脂秤");
        commonTitleView.setRightBtnText("搜索");
        commonTitleView.setOnTitleClickListener(new CommonTitleView.OnTitleClickListener() {
            @Override
            public void onLeftBtnClick() {
                onBackPressed();
            }
            @Override
            public void onRightBtnClick() {
                list.clear();
                adapter.notifyDataSetChanged();
                startSearch();
            }
        });
    }

    @Override
    protected void initViews() {
        dataTextView = (CustomFontTextView) findViewById(R.id.dataTextView);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new BleDevInfoAdapter(context , list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BleDevInfo bleDevInfo = list.get(position);

                int state = bleDevInfo.getState();
                if (state == BleState.STATE_DISCONNECTED.getState()){

                    BleActionControl.getInstance().startConnect(context , bleDevInfo.getAddress());

                }else {
                    BleActionControl.getInstance().disconnect(bleDevInfo.getAddress());

                }
            }
        });
    }

    BleSearchCallbackListener bleSearchCallbackListener = new BleSearchCallbackListener() {
        @Override
        public void onSearchResult(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BleDevInfo bleDevInfo = BleDevInfo.fromBluetoothDevice(device , rssi);
                    String name = bleDevInfo.getName();
                    if (!name.startsWith("HW")){
                        return;
                    }
                    if (list.contains(bleDevInfo)){
                        return;
                    }
                    list.add(bleDevInfo);

                    adapter.notifyDataSetChanged();

                }
            });
        }
    };

    BleStateListener bleStateListener = new BleStateListener() {
        @Override
        public void onStateChange(final BleState bleState, final String address) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (BleDevInfo devInfo : list){
                        if (devInfo.getAddress().equals(address)){
                            devInfo.setState(bleState.getState());
                            adapter.notifyDataSetChanged(listView , list.indexOf(devInfo));
                            return;
                        }
                    }
                }
            });
        }
    };

    BleDataReceiveListener dataReceiveListener = new BleDataReceiveListener() {
        @Override
        public void onDataReceived(final int dev_type, final int data_type, final String address, final String value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (data_type == DevType.HW330A){
                        dataTextView.setText(value);
                    }
                }
            });
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerBleListeners();
    }

    @Override
    protected void onDestroy() {
        unRegisterBleListeners();
        super.onDestroy();
    }

    private void registerBleListeners(){
        braceletBleServiceListener = new BraceletBleServiceListener();
        BleCallBackControl.getInstance().registerBleStateListener(bleStateListener)
                .registerServiceListener(braceletBleServiceListener)
                .registerBleDataReceiveListener(dataReceiveListener)
        ;
        ;
    }

    private void unRegisterBleListeners(){
        BleCallBackControl.getInstance().unRegisterBleStateListener(bleStateListener)
                .unRegisterServiceListener(braceletBleServiceListener)
                .unRegisterBleDataReceiveListener(dataReceiveListener)
        ;
    }
}

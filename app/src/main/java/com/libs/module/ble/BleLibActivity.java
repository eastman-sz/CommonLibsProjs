package com.libs.module.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ble.lib.dev.BleDevInfo;
import com.ble.lib.f.BleActionControl;
import com.ble.lib.f.BleCallBackControl;
import com.ble.lib.f.BleDataReceiveListener;
import com.ble.lib.f.BleDataType;
import com.ble.lib.f.BleSearchCallbackListener;
import com.ble.lib.f.BleSearchHelper;
import com.ble.lib.f.BleState;
import com.ble.lib.f.BleStateListener;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.base.CustomFontTextView;
import com.common.libs.proj.R;
import com.common.libs.util.ILog;
import com.libs.module.ble.dfu.DFUActivity;
import com.utils.lib.ss.common.DateHepler;
import java.util.ArrayList;

public class BleLibActivity extends BaseAppCompactActivitiy {

    private CustomFontTextView dataTextView = null;

    private ListView listView = null;
    private BleDevInfoAdapter adapter = null;
    private ArrayList<BleDevInfo> list = new ArrayList<>();

    private BleSearchHelper bleSearchHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_lib);

        initActivitys();

        bleSearchHelper = new BleSearchHelper(context , true , bleSearchCallbackListener);

        bleSearchHelper.startSearch();
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("Ble");
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
                bleSearchHelper.startSearch();

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BleDevInfo bleDevInfo = list.get(position);

                ILog.e("-----长按-------:: " + bleDevInfo.getName());

                startActivity(new Intent(context , DFUActivity.class)
                        .putExtra("name" , bleDevInfo.getName())
                        .putExtra("address" , bleDevInfo.getAddress())
                );

                return true;
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
                    if (data_type == BleDataType.SHUHUA_DATA){
                        dataTextView.setText("舒华数据: " + DateHepler.timestampFormat(System.currentTimeMillis()/1000 , "HH:mm:ss")
                                + "\n" +  value);
                    }
                }
            });
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        BleCallBackControl.getInstance().registerBleStateListener(bleStateListener)
             .registerBleDataReceiveListener(dataReceiveListener)
        ;
    }

    @Override
    protected void onDestroy() {
        bleSearchHelper.cancelDiscovery();

        super.onDestroy();

        BleCallBackControl.getInstance().unRegisterBleStateListener(bleStateListener)
            .unRegisterBleDataReceiveListener(dataReceiveListener)
        ;
    }
}

package com.libs.module.ble.tzcheng;

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
import com.ble.lib.util.CommonBleUtils;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.base.CustomFontTextView;
import com.common.libs.proj.R;
import com.common.libs.util.ILog;
import com.libs.module.ble.BleDevInfoAdapter;
import com.libs.module.ble.tizhicheng.TzcMath;

import java.util.ArrayList;

public class TzchengActivity extends BaseAppCompactActivitiy {

    private CustomFontTextView dataTextView = null;
    private TzchengBleServiceListener bleServiceListener = null;

    private BleSearchHelper bleSearchHelper = null;

    private ListView listView = null;
    private BleDevInfoAdapter adapter = null;
    private ArrayList<BleDevInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzcheng);

        initActivitys();

        bleSearchHelper = new BleSearchHelper(context , true , bleSearchCallbackListener);

        bleSearchHelper.startSearch();
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("体脂秤一代");
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
    }

    public void onBtnClick(View v){
        switch (v.getId()){
            case R.id.calBtnTextView:
            {
                String hex = CommonBleUtils.integral2Hex((int)(System.currentTimeMillis()/1000));

                ILog.e("Hex: " + hex);

                String checkSum =  TzcMath.calChecksum("FD0602007C");

                ILog.e("checkSum: " + checkSum);
            }
                break;
            default:
                break;
        }
    }

    BleSearchCallbackListener bleSearchCallbackListener = new BleSearchCallbackListener() {
        @Override
        public void onSearchResult(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BleDevInfo bleDevInfo = BleDevInfo.fromBluetoothDevice(device , rssi);
                    String name = bleDevInfo.getName();
                    if (!name.startsWith("WS")){
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
        public void onDataReceived(int dev_type, final int data_type, String address, final String value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (data_type == 21){
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
        bleServiceListener = new TzchengBleServiceListener();
        BleCallBackControl.getInstance().registerBleStateListener(bleStateListener)
                .registerServiceListener(bleServiceListener)
                .registerBleDataReceiveListener(dataReceiveListener)
        ;
        ;
    }

    private void unRegisterBleListeners(){
        BleCallBackControl.getInstance().unRegisterBleStateListener(bleStateListener)
                .unRegisterServiceListener(bleServiceListener)
                .unRegisterBleDataReceiveListener(dataReceiveListener)
        ;
    }


}

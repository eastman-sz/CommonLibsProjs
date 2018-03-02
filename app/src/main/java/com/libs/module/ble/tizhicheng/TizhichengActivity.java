package com.libs.module.ble.tizhicheng;

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
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.base.CustomFontTextView;
import com.common.libs.proj.R;
import com.common.libs.util.ILog;
import com.libs.module.ble.BleDevInfoAdapter;
import java.util.ArrayList;
import cn.fitcare.fcbodyfatlibrary.FCDataType;
import cn.fitcare.fcbodyfatlibrary.FCPeopleGeneral;

public class TizhichengActivity extends BaseAppCompactActivitiy {

    private TizhichengNewBleServiceListener bleServiceListener = null;

    private CustomFontTextView dataTextView = null;
    private ListView listView = null;
    private BleDevInfoAdapter adapter = null;
    private ArrayList<BleDevInfo> list = new ArrayList<>();

    private BleSearchHelper bleSearchHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tizhicheng);

        initActivitys();

        bleSearchHelper = new BleSearchHelper(context , true , bleSearchCallbackListener);

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
                float weight = 79.2f;
                int height = 172;
                int sex = 1;
                int impedance = 13194334;
                int age = 30;

                cal(weight,height,sex, age, impedance);
            }
                break;
            default:
                break;
        }
    }

    private void cal(float weight , int height , int sex , int age , int impedance){
        FCPeopleGeneral bodyfat = new FCPeopleGeneral(weight,height,sex, age, impedance);
        int errorType = bodyfat.getBodyfatParameters();
        if (errorType == FCDataType.ErrorNone){
            String zukang = "阻抗:" + bodyfat.htZTwoLegs + " Ω";
            String bmi = "BMI:" + String.format("%.1f",bodyfat.htBMI);
            String BMR = "BMR:" + (int) bodyfat.htBMR;
            String nzzf = "内脏脂肪:" + (int) bodyfat.htVFAL; //内脏脂肪
            String gl = "骨量:" + String.format("%.1fkg",bodyfat.htBoneKg); //骨量
            String zfl = "脂肪率:" + String.format("%.1f%%",bodyfat.htBodyfatPercentage); //脂肪率
            String sf = "水分:" + String.format("%.1f%%",bodyfat.htWaterPercentage); //水分
            String jr = "肌肉:" + String.format("%.1fkg",bodyfat.htMuscleKg); //肌肉

            ILog.e("阻抗: " + zukang + "BMI: " + bmi + "BMR: " + BMR + "内脏脂肪: " + nzzf);
            ILog.e("骨量: " + gl + "脂肪率: " + zfl + "水分: " + sf + "肌肉: " + jr);
        }else {
            ILog.e("数据计算有误");
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
        public void onDataReceived(final int dev_type, final int data_type, final String address, final String value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (data_type == 20){
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
        bleServiceListener = new TizhichengNewBleServiceListener();
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

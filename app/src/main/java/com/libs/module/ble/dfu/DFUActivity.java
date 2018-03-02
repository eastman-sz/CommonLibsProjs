package com.libs.module.ble.dfu;

import android.os.Bundle;
import android.view.View;
import com.ble.lib.f.BleHelper;
import com.ble.lib.impl.BleCommandHelper;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.base.CustomFontTextView;
import com.common.libs.proj.R;
import com.ss.number.progress.bar.NumberProgressBar;

public class DFUActivity extends BaseAppCompactActivitiy implements View.OnClickListener{

    private CustomFontTextView devInfoTextView = null;
    private NumberProgressBar numberProgressBar = null;

    private DfuActivityHelper dfuActivityHelper = null;

    private String name = null;
    private String address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfu);

        initActivitys();

        dfuActivityHelper = new DfuActivityHelper(context);
        dfuActivityHelper.setDeviceName(name);
        dfuActivityHelper.setDeviceAddress(address);
        dfuActivityHelper.setDfuListener(new DfuStateListener());
    }

    @Override
    protected void getIntentData() {
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("DFU");
        commonTitleView.setOnTitleClickListener(new CommonTitleView.OnTitleClickListener() {
            @Override
            public void onLeftBtnClick() {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initViews() {
        devInfoTextView = (CustomFontTextView) findViewById(R.id.devInfo_textView);
        numberProgressBar = (NumberProgressBar) findViewById(R.id.numberProgressbar);

        devInfoTextView.setText(name + "\n" + address);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.prepareDfuBtnTextView).setOnClickListener(this);
        findViewById(R.id.startDfuBtnTextView).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prepareDfuBtnTextView:
            {
                BleCommandHelper.ld2WriteCommandForDfu(BleHelper.getInstance().getBluetoothGatt(address));
            }
            break;
            case R.id.startDfuBtnTextView:
            {
                dfuActivityHelper.startDfuSteps();
            }
                break;
            default:
                break;
        }
    }

    class DfuStateListener implements DfuActivityHelper.DFUListener{

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            numberProgressBar.setProgress(percent);
        }

        @Override
        public void onFirmwareValidating(String deviceAddress) {

        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {

        }

        @Override
        public void onEnablingDfuMode(String deviceAddress) {

        }

        @Override
        public void onDfuProcessStarting(String deviceAddress) {

        }

        @Override
        public void onDfuProcessStarted(String deviceAddress) {

        }

        @Override
        public void onDfuCompleted(String deviceAddress) {

        }

        @Override
        public void onDfuAborted(String deviceAddress) {

        }

        @Override
        public void onDeviceDisconnecting(String deviceAddress) {

        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {

        }

        @Override
        public void onDeviceConnecting(String deviceAddress) {

        }

        @Override
        public void onDeviceConnected(String deviceAddress) {

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        dfuActivityHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dfuActivityHelper.onPause();

    }
}

package com.common.libs.proj;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.libs.util.PermissionHelpler;
import com.libs.module.CommonviewLibActivity;
import com.libs.module.UtilsLibActivity;
import com.libs.module.ble.BleFuncActivity;
import com.libs.module.ble.BleLibActivity;
import com.libs.module.noti.NotifiActivity;
import com.libs.module.phone.TelActivity;
import com.libs.module.usb.UsbActivity;

import java.util.ArrayList;

public class MainActivity extends BaseAppCompactActivitiy {

    private ListView listView = null;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivitys();

        PermissionHelpler.requestPermissions(this);
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("Sample");
        commonTitleView.setOnTitleClickListener(new CommonTitleView.OnTitleClickListener() {
            @Override
            public void onLeftBtnClick() {
                moveTaskToBack(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                }
            }
        });
    }

    @Override
    protected void initViews() {
        list.add("UtilsLib");
        list.add("Numberprogressbar");
        list.add("CommonViewsLib");
        list.add("BLeLib");
        list.add("USB");
        list.add("Notification");
        list.add("Noti Tel");

        listView = (ListView) findViewById(R.id.listView);
        TextAdapter adapter = new TextAdapter(this , list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context , 0 == position ? UtilsLibActivity.class
                        : 1 == position ? com.ss.number.progress.bar.MainActivity.class
                        : 2 == position ? CommonviewLibActivity.class
                        : 3 == position ? BleFuncActivity.class
                        : 4 == position ? UsbActivity.class
                        : 5 == position ? NotifiActivity.class
                        : 6 == position ? TelActivity.class
                        : CommonviewLibActivity.class
                ));
            }
        });
    }
}

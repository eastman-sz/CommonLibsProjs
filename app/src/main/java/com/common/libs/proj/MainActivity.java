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
import com.libs.module.pullfreshview.PullFreshViewActivity;
import com.libs.module.usb.UsbActivity;
import com.libs.module.wheelview.DateTimeSelectDialog;

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
        list.add("Wheelview");
        list.add("pullFreshView");

        listView = (ListView) findViewById(R.id.listView);
        TextAdapter adapter = new TextAdapter(this , list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(context , UtilsLibActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(context , MainActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(context , CommonviewLibActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(context , BleFuncActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(context , UsbActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(context , NotifiActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(context , TelActivity.class));
                        break;
                    case 7:
                    {
                        DateTimeSelectDialog dialog = new DateTimeSelectDialog(context);
                        dialog.show();
                        dialog.setTimestamp(System.currentTimeMillis());
                    }
                        break;
                    case 8:
                        startActivity(new Intent(context , PullFreshViewActivity.class));
                        break;
                    default:
                        break;
                }

            }
        });
    }
}

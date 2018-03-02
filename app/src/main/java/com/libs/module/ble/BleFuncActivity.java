package com.libs.module.ble;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.libs.proj.R;
import com.common.libs.proj.TextAdapter;
import com.common.libs.util.ILog;
import com.libs.module.CommonviewLibActivity;
import com.libs.module.ble.bracelet.BraceletActivity;
import com.libs.module.ble.tizhicheng.TizhichengActivity;
import com.libs.module.ble.tzcheng.TzchengActivity;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * BLE Function entrance page.
 */
public class BleFuncActivity extends BaseAppCompactActivitiy {

    private ListView listView = null;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_func);

        initActivitys();
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("蓝牙");
        commonTitleView.setOnTitleClickListener(new CommonTitleView.OnTitleClickListener() {
            @Override
            public void onLeftBtnClick() {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initViews() {
        list.add("Common");
        list.add("体脂秤一代");
        list.add("体脂秤二代");
        list.add("手环");
        list.add("时区转换");

        listView = (ListView) findViewById(R.id.listView);
        TextAdapter adapter = new TextAdapter(this , list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (4 == position){
                    timeZone();
                    return;
                }
                startActivity(new Intent(context , 0 == position ? BleLibActivity.class
                        : 1 == position ? TzchengActivity.class
                        : 2 == position ? TizhichengActivity.class
                        : 3 == position ? BraceletActivity.class
                        : CommonviewLibActivity.class
                ));
            }
        });
    }

    private void timeZone(){
        String id =  java.util.TimeZone.getDefault().getID();
        int offset =  java.util.TimeZone.getDefault().getRawOffset();
        String displayName = TimeZone.getDefault().getDisplayName();

        ILog.e("id: " + id + "   offset: " + offset + "   displayName: " + displayName);

    }

}

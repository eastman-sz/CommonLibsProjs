package com.libs.module.noti;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.libs.proj.R;
import com.common.libs.proj.TextAdapter;
import com.common.libs.util.ILog;
import com.utils.lib.ss.common.ToastHelper;
import java.util.ArrayList;

public class NotifiActivity extends BaseAppCompactActivitiy {

    private ListView listView = null;
    private ArrayList<String> list = new ArrayList<>();

    private Intent notifiIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifi);

        initActivitys();
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("监听通知栏");
        commonTitleView.setOnTitleClickListener(new CommonTitleView.OnTitleClickListener() {
            @Override
            public void onLeftBtnClick() {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initViews() {
        list.add("开启服务");
        list.add("关闭服务");
        list.add("设置权限");
        list.add("检查访问通知栏权限");

        listView = (ListView) findViewById(R.id.listView);
        TextAdapter adapter = new TextAdapter(this , list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                    {
                        //开始监听通知栏服务
                        if (null == notifiIntent){
                            notifiIntent = new Intent(context , MyNotificationListenService.class);

                            startService(notifiIntent);
                        }
                    }
                        break;
                    case 1:
                    {
                        //关闭服务
                        if (null == notifiIntent){
                            return;
                        }
                        stopService(notifiIntent);
                        notifiIntent = null;
                    }
                        break;
                    case 2:
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            Intent intent = new Intent(
                                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                            startActivity(intent);
                        }
                    }
                        break;
                    case 3:
                    {
                        boolean enalbed = isEnabled();

                        ToastHelper.makeText(context , enalbed ? "已有权限" : "没有权限");
                    }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

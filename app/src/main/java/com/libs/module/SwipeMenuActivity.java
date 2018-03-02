package com.libs.module;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.libs.proj.R;
import com.common.libs.proj.TextAdapter;
import com.common.swipe.SwipeMenu;
import com.common.swipe.SwipeMenuCreator;
import com.common.swipe.SwipeMenuItem;
import com.common.swipe.SwipeMenuListView;
import com.utils.lib.ss.info.DeviceInfo;
import java.util.ArrayList;

public class SwipeMenuActivity extends BaseAppCompactActivitiy {

    private TextAdapter adapter = null;
    private SwipeMenuListView listView = null;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_menu);

        initActivitys();
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("SwipMenu");
        commonTitleView.setOnTitleClickListener(new CommonTitleView.OnTitleClickListener() {
            @Override
            public void onLeftBtnClick() {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initViews() {
        listView = (SwipeMenuListView) findViewById(R.id.listview);
        listView.setMenuCreator(menuCreator);

        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");

        adapter = new TextAdapter(context , list);
        listView.setAdapter(adapter);
        listView.setOnMenuItemClickListener(new MenuItemOnclickListener());
    }

    SwipeMenuCreator menuCreator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem item = new SwipeMenuItem(context);
            item.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
            item.setWidth(DeviceInfo.dip2px(context, 90));
            item.setIcon(R.drawable.sfs_ic_delete);
            menu.addMenuItem(item);
        }
    };

    class MenuItemOnclickListener implements SwipeMenuListView.OnMenuItemClickListener {
        @Override
        public void onMenuItemClick(final int position, SwipeMenu menu, final int index) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (index) {
                        case 0:

                            list.remove(position);

                            handler.sendEmptyMessageDelayed(0 , 300);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                {
                    adapter.notifyDataSetChanged();
                }
                    break;
            }
        }
    };

}

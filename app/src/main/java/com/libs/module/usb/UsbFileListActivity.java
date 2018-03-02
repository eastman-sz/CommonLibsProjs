package com.libs.module.usb;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.libs.proj.R;
import com.common.libs.util.ILog;

import java.io.File;
import java.util.ArrayList;

public class UsbFileListActivity extends BaseAppCompactActivitiy {

    private ListView listView = null;
    private UsbFileAdapter adapter = null;
    private ArrayList<UsbFile> list = new ArrayList<>();

    private String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_file_list);

        initActivitys();
    }

    @Override
    protected void getIntentData() {
        path = getIntent().getStringExtra("path");
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("USB Files");
        commonTitleView.setOnTitleClickListener(new CommonTitleView.OnTitleClickListener() {
            @Override
            public void onLeftBtnClick() {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        adapter = new UsbFileAdapter(context , list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        toListFiles();
    }

    private void toListFiles(){
        if (TextUtils.isEmpty(path)){
            ILog.e("路径不存在");
            return;
        }
        File dirFile = new File(path);
        dirFile.setReadable(true);
        dirFile.setExecutable(true);
        File[] files = dirFile.listFiles();
        if (null == files){
            ILog.e("路Path: " + "----------");
            return;
        }
        int length = files.length;
        for (int i = 0 ; i < length ; i++){
            File file = files[i];

            String filePath = file.getAbsolutePath();
            String fileName = file.getName();

            UsbFile usbFile = new UsbFile();
            usbFile.setName(fileName);
            usbFile.setPath(filePath);

            list.add(usbFile);
        }

        adapter.notifyDataSetChanged();

    }

}

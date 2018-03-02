package com.libs.module;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.common.base.BaseAppCompactActivitiy;
import com.common.base.CommonTitleView;
import com.common.dialog.CommonDialog;
import com.common.dialog.OnCommonDialogBtnClickListener;
import com.common.libs.proj.R;

public class CommonviewLibActivity extends BaseAppCompactActivitiy {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonview_lib);

        initActivitys();
    }

    @Override
    protected void initTitle() {
        CommonTitleView commonTitleView = (CommonTitleView)findViewById(R.id.commontitle_view);
        commonTitleView.setCenterTitleText("CommonviewLib");
        commonTitleView.setOnTitleClickListener(new CommonTitleView.OnTitleClickListener() {
            @Override
            public void onLeftBtnClick() {
                onBackPressed();
            }
        });
    }

    public void showCommonDialog(View view){
        CommonDialog dialog = new CommonDialog(context);
        dialog.show();
        dialog.setDialogText("" , "Content" , "取消" , "确定");
        dialog.setOnCommonDialogBtnClickListener(new OnCommonDialogBtnClickListener() {
            @Override
            public void onLeftBtnClik() {

            }
            @Override
            public void onRightBtnClik() {

            }
        });
    }

    public void showSwipeMenu(View view){
        startActivity(new Intent(context , SwipeMenuActivity.class));
    }


}

package com.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.base.CustomFontTextView;
import com.common.views.lib.ss.R;

/**
 * Created by E on 2017/9/25.
 */
public class CommonDialog extends BaseDialog {

    private CustomFontTextView titleTextView = null;
    private CustomFontTextView contentTextView = null;
    private CustomFontTextView leftBtnTextView = null;
    private CustomFontTextView rightBtnTextView = null;

    public CommonDialog(Context context) {
        super(context , R.style.commom_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int width = context.getResources().getDisplayMetrics().widthPixels;
        width = width > 850 ? 850 : width;
        params.width = width;
        View view = LayoutInflater.from(context).inflate(R.layout.sfs_common_dialog_view, null);
        setContentView(view , params);
        setCanceledOnTouchOutside(true);

        init();
    }

    @Override
    protected void initViews() {
        titleTextView = (CustomFontTextView) findViewById(R.id.title_textView);
        contentTextView = (CustomFontTextView) findViewById(R.id.content_textview);
        leftBtnTextView = (CustomFontTextView) findViewById(R.id.left_btn_textview);
        rightBtnTextView = (CustomFontTextView) findViewById(R.id.right_btn_textview);

        leftBtnTextView.clearFocus();
        rightBtnTextView.clearFocus();
    }

    @Override
    protected void initListeners() {
        leftBtnTextView.setOnClickListener(onClickListener);
        rightBtnTextView.setOnClickListener(onClickListener);
    }

    public void setDialogText(String title , String content , String leftBtnText , String rightBtnText){
        titleTextView.setText(title);
        contentTextView.setText(content);
        leftBtnTextView.setText(leftBtnText);
        rightBtnTextView.setText(rightBtnText);

        titleTextView.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
    }

    public void setDialogText(int resid_title , int resid_content , int resid_leftBtnText , int resid_rightBtnText){
        titleTextView.setText(resid_title);
        contentTextView.setText(resid_content);
        leftBtnTextView.setText(resid_leftBtnText);
        rightBtnTextView.setText(resid_rightBtnText);

        titleTextView.setVisibility(TextUtils.isEmpty(context.getResources().getString(resid_title)) ? View.GONE : View.VISIBLE);
    }

    public void setContentTextViewGravity(int gravity){
        contentTextView.setGravity(gravity);
    }

    public void leftBtnRequestFocus(int delayMillis){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rightBtnTextView.clearFocus();
                leftBtnTextView.requestFocus();
            }
        } , delayMillis);
    }

    public void rightBtnRequestFocus(int delayMillis){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                leftBtnTextView.clearFocus();
                rightBtnTextView.requestFocus();
            }
        } , delayMillis);
    }

    Handler handler = new Handler(Looper.myLooper()){};

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            if (vId == R.id.left_btn_textview){

                if (null != onCommonDialogBtnClickListener){
                    onCommonDialogBtnClickListener.onLeftBtnClik();
                }
            }else if (vId == R.id.right_btn_textview){

                if (null != onCommonDialogBtnClickListener){
                    onCommonDialogBtnClickListener.onRightBtnClik();
                }
            }

            dismiss();
        }
    };

    private OnCommonDialogBtnClickListener onCommonDialogBtnClickListener = null;

    public void setOnCommonDialogBtnClickListener(OnCommonDialogBtnClickListener onCommonDialogBtnClickListener) {
        this.onCommonDialogBtnClickListener = onCommonDialogBtnClickListener;
    }

}

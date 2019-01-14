package com.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.common.base.CustomFontTextView;
import com.common.views.lib.ss.R;
/**
 * Created by E on 2018/3/6.
 */
public class SingleBtnDialog extends BaseDialog implements View.OnClickListener{

    private CustomFontTextView titleTextView = null;
    private CustomFontTextView contentTextView = null;
    private CustomFontTextView singleBtnTextView = null;

    public SingleBtnDialog(Context context) {
        super(context , R.style.common_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int width = context.getResources().getDisplayMetrics().widthPixels;
        width = width > 850 ? 850 : width;
        params.width = width;
        View view = LayoutInflater.from(context).inflate(R.layout.sfs_single_btn_dialog_view, null);
        setContentView(view , params);
        setCanceledOnTouchOutside(true);

        init();
    }

    @Override
    protected void initViews() {
        titleTextView = (CustomFontTextView) findViewById(R.id.title_textView);
        contentTextView = (CustomFontTextView) findViewById(R.id.content_textview);
        singleBtnTextView = (CustomFontTextView) findViewById(R.id.single_btn_textview);
    }

    @Override
    protected void initListeners() {
        singleBtnTextView.setOnClickListener(this);
    }

    public void setDialogText(String title , String content , String singleBtnText){
        titleTextView.setText(title);
        contentTextView.setText(content);
        singleBtnTextView.setText(content);
    }

    public void setDialogText(int title , int content , int singleBtnText){
        titleTextView.setText(title);
        contentTextView.setText(content);
        singleBtnTextView.setText(content);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v == singleBtnTextView){
            if (null != onSingleBtnClickListener){
                onSingleBtnClickListener.onSingleBtnClick();
            }
        }
    }

    public interface OnSingleBtnClickListener{
        void onSingleBtnClick();
    }

    private OnSingleBtnClickListener onSingleBtnClickListener = null;

    public void setOnSingleBtnClickListener(OnSingleBtnClickListener onSingleBtnClickListener) {
        this.onSingleBtnClickListener = onSingleBtnClickListener;
    }
}

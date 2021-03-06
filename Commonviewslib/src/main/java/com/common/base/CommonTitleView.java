package com.common.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.common.views.lib.ss.R;

import java.lang.reflect.Field;
/**
 * Created by E on 2017/9/25.
 */
public class CommonTitleView extends BaseRelativeLayout {

    private CustomFontTextView topTitleView = null;
    private LinearLayout titleLayout = null;
    private CustomFontTextView leftBtnTextView = null;
    private CustomFontTextView centerTitleTextView = null;
    private CustomFontTextView rightBtnTextView = null;

    public CommonTitleView(Context context) {
        super(context);

        init();
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CommonTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void initViews() {
        LayoutInflater.from(context).inflate(R.layout.sfs_common_title_view, this);

        topTitleView = (CustomFontTextView) findViewById(R.id.top_title_view);
        titleLayout = (LinearLayout) findViewById(R.id.titleLayout);
        leftBtnTextView = (CustomFontTextView) findViewById(R.id.commom_left_textview);
        centerTitleTextView = (CustomFontTextView) findViewById(R.id.center_title_textview);
        rightBtnTextView = (CustomFontTextView) findViewById(R.id.commom_right_textview);

        int statusBarHeight = getStatusBarHeight(context);
        if (0 != statusBarHeight) {
            ViewGroup.LayoutParams params = topTitleView.getLayoutParams();
            params.height = statusBarHeight;
            topTitleView.setLayoutParams(params);
        }
    }

    @Override
    protected void initListener() {
        leftBtnTextView.setOnClickListener(listener);
        rightBtnTextView.setOnClickListener(listener);
    }

    public void setLeftBtnText(String leftBtnText){
        leftBtnTextView.setText(leftBtnText);
    }

    public void setLeftBtnTextColor(int textColor){
        leftBtnTextView.setTextColor(textColor);
    }

    public void setLeftBtnText(int leftBtnText){
        leftBtnTextView.setText(leftBtnText);
    }

    public void setLeftBtnBackgroud(int resid){
        Drawable drawable = getResources().getDrawable(resid);
        if (null == drawable) {
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        leftBtnTextView.setCompoundDrawables(drawable, null, null, null);
    }

    public void setRightBtnBackgroud(int resid){
        Drawable drawable = getResources().getDrawable(resid);
        if (null == drawable) {
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        rightBtnTextView.setCompoundDrawables(null, null, drawable, null);
    }

    public void setCenterTitleText(String centerTitleText){
        centerTitleTextView.setText(centerTitleText);
    }

    public void setCenterTitleText(int centerTitleText){
        centerTitleTextView.setText(centerTitleText);
    }

    public void setCenterTitleTextColor(int textColor){
        centerTitleTextView.setTextColor(textColor);
    }

    public void setLeftBtnVisibility(int visibility){
        leftBtnTextView.setVisibility(visibility);
    }

    public void setRightBtnVisibility(int visibility){
        rightBtnTextView.setVisibility(visibility);
    }

    public void setRightBtnText(String rightBtnText){
        rightBtnTextView.setText(rightBtnText);
    }

    public void setRightBtnText(int rightBtnText){
        rightBtnTextView.setText(rightBtnText);
    }

    public void setRightBtnTextColor(int textColor){
        rightBtnTextView.setTextColor(textColor);
    }

    public void setTitleBackgroundColor(int bgcolor){
        topTitleView.setBackgroundColor(bgcolor);
        titleLayout.setBackgroundColor(bgcolor);
    }

    public void setRightBtnEnabled(boolean enabled){
        rightBtnTextView.setEnabled(enabled);
    }

    public void setLeftBtnEnabled(boolean enabled){
        leftBtnTextView.setEnabled(enabled);
    }

    OnClickListener listener = new OnClickListener(){
        @Override
        public void onClick(View v) {
            int vId  = v.getId();
            if (vId == R.id.commom_left_textview){
                if (null != onTitleClickListener) {
                    onTitleClickListener.onLeftBtnClick();
                }
            }else if (vId == R.id.commom_right_textview){
                if (null != onTitleClickListener) {
                    onTitleClickListener.onRightBtnClick();
                }
            }
        }
    };

    public OnTitleClickListener onTitleClickListener = null;

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    public static abstract class OnTitleClickListener{
        public void onLeftBtnClick(){};
        public void onRightBtnClick(){};
    }

    /**
     * StatusBarHeight
     * @param context
     * @return StatusBarHeight
     */
    private static int getStatusBarHeight(Context context){
        Class<?> c =  null;
        Object obj =  null;
        Field field =  null;
        int  x = 0, sbar =  0;
        try  {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch(Exception e1) {
            e1.printStackTrace();
        }
        return 0;
    }

}

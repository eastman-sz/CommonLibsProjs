package com.common.base;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by E on 2017/4/26.
 */
public class ViewHelper {

    public static void setLeftCompoundDrawables(Context context , CustomFontTextView textView , int drawable_resid){
        if (0 == drawable_resid){
            textView.setCompoundDrawables(null , null , null ,null);

            return;
        }
        Drawable drawable = context.getResources().getDrawable(drawable_resid);
        drawable.setBounds(0,0,drawable.getMinimumWidth() , drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable , null , null ,null);
    }

    public static void setRightCompoundDrawables(Context context , CustomFontTextView textView , int drawable_resid){
        if (0 == drawable_resid){
            textView.setCompoundDrawables(null , null , null ,null);

            return;
        }
        Drawable drawable = context.getResources().getDrawable(drawable_resid);
        drawable.setBounds(0,0,drawable.getMinimumWidth() , drawable.getMinimumHeight());
        textView.setCompoundDrawables(null , null , drawable ,null);
    }

    public static void setTopCompoundDrawables(Context context , CustomFontTextView textView , int drawable_resid){
        if (0 == drawable_resid){
            textView.setCompoundDrawables(null , null , null ,null);

            return;
        }
        Drawable drawable = context.getResources().getDrawable(drawable_resid);
        drawable.setBounds(0,0,drawable.getMinimumWidth() , drawable.getMinimumHeight());
        textView.setCompoundDrawables(null , drawable , null ,null);
    }

    public static void setBottomCompoundDrawables(Context context , CustomFontTextView textView , int drawable_resid){
        if (0 == drawable_resid){
            textView.setCompoundDrawables(null , null , null ,null);

            return;
        }
        Drawable drawable = context.getResources().getDrawable(drawable_resid);
        drawable.setBounds(0,0,drawable.getMinimumWidth() , drawable.getMinimumHeight());
        textView.setCompoundDrawables(null , null , null ,drawable);
    }

}

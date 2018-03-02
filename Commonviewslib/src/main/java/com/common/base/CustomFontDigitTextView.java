package com.common.base;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.common.views.lib.ss.R;

/**
 * 自定义字体的TextView。
 * @author E
 */
public class CustomFontDigitTextView extends TextView {

	public CustomFontDigitTextView(Context context) {
		super(context);
		
		init();
	}

	public CustomFontDigitTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}

	public CustomFontDigitTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
	}
	
	private void init() {
		if (!isInEditMode()) {

//			final Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.font_fzlting));
			final Typeface typeface = TypefaceHelper.getInstance().getDigitTypeface(getContext());
			setTypeface(typeface);
		}
	}

}

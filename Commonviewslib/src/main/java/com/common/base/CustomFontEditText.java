package com.common.base;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 自定义带字体的ditText。 
 * @author E
 */
public class CustomFontEditText extends EditText {

	public CustomFontEditText(Context context) {
		super(context);
		
		init();
	}

	public CustomFontEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}

	public CustomFontEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
	}
	
	private void init() {
		if (!isInEditMode()) {
//			final Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.font_fzlting));
			final Typeface typeface = TypefaceHelper.getInstance().getTypeface(getContext());
			setTypeface(typeface);
		}
	}

}

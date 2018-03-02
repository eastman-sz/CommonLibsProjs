package com.common.base;

import android.content.Context;
import android.graphics.Typeface;

import com.common.views.lib.ss.R;

/**
 * 字体类。
 * @author E
 */
public class TypefaceHelper {

	private final static TypefaceHelper typefaceHelper = new TypefaceHelper();
	
	private static Typeface typeface = null;
	private static Typeface digitTypeface = null;
	
	public static TypefaceHelper getInstance(){
		return typefaceHelper;
	}

	private TypefaceHelper() {
	}

	/**
	 * 字体。
	 * @return 字体
	 */
	public Typeface getTypeface(Context context) {
		if (null == typeface) {
			typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.font_path));
		}
		return typeface;
	}
	
	/**
	 * 字体。
	 * @return 字体
	 */
	public Typeface getDigitTypeface(Context context) {
		if (null == digitTypeface) {
			digitTypeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.font_digit_path));
		}
		return digitTypeface;
	}	

}

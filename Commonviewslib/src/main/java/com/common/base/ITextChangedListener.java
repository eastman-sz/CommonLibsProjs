package com.common.base;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 监听文本输入变化的事件。
 * @author E
 */
public class ITextChangedListener implements TextWatcher {

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
	@Override
	public void afterTextChanged(Editable s) {
	}

}

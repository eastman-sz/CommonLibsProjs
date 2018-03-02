package com.common.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.common.base.SystemBarTintManager;
import com.common.views.lib.ss.R;

import java.util.ArrayList;
/**
 * 自定义Dialog的基类。
 * @author E
 */
public class BaseDialog extends Dialog {

	protected Context context = null;
	
	public BaseDialog(Context context) {
		super(context);
		this.context = context;
		initSystemBar();
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		initSystemBar();
	}

	public BaseDialog(Context context, boolean cancelable,
                      OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
		initSystemBar();
	}
	
	private void initSystemBar(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager((Activity)context);
		tintManager.setStatusBarTintEnabled(false);
		tintManager.setStatusBarTintResource(R.color.sfs_c1);
	}

	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		register();
	}
	
	protected void init(){
		initTitle();
		initViews();
		initListeners();
	}
	
	protected void initTitle(){
	}
	
	protected void initViews(){
	}
	
	protected void initListeners(){
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		unRegister();
	}
	
	@Override
	public void show() {
		super.show();
	}

	private class BaseReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			onBroadCastReceive(context, intent);
		}
	}
	
	private BaseReceiver receiver = null;
	
	/**
	 * 注册广播。
	 */
	private void register(){
		if (null == receiver && !addBroadCastAction().isEmpty()) {
			receiver = new BaseReceiver();
			IntentFilter filter = new IntentFilter();
			for (String action : addBroadCastAction()) {
				filter.addAction(action);
			}
			context.registerReceiver(receiver, filter);
		}
	}
	
	/**
	 * 注销广播。
	 */
	private void unRegister(){
		if (null != receiver && !addBroadCastAction().isEmpty()) {
			context.unregisterReceiver(receiver);
			receiver = null;
		}
	}
	
	/**
	 * 添加广播的Action。
	 * @return ArrayList<String> Action集合
	 */
	protected ArrayList<String> addBroadCastAction(){
		return new ArrayList<String>();
	}	
	
	/**
	 * 收到广播。
	 * @param context 广播上下文
	 * @param intent Intent
	 */
	protected void onBroadCastReceive(Context context, Intent intent){
		
	}
	
}

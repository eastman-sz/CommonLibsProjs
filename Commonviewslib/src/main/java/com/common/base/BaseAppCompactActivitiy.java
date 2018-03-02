package com.common.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.common.views.lib.ss.R;

import java.util.ArrayList;
/**
 * 定义Activity的基类。
 * @author E
 */
public class BaseAppCompactActivitiy extends Activity {

	protected Context context = null;
	protected long enterTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = BaseAppCompactActivitiy.this;
		enterTime = System.currentTimeMillis()/1000;
	}

	protected void initActivitys(){
		init();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
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
	
	private void init(){
		getIntentData();
		initTitle();
		initViews();
		initListener();		
	}

	protected void getIntentData(){
	}
	
	/**
	 * 初始化标题
	 */
	protected void initTitle(){
	}
	
	/**
	 * 初始化VIEW
	 */
	protected void initViews(){
	}
	
	/**
	 * 初始化Listener
	 */
	protected void initListener(){
	}	
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		register();
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		unRegister();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private class BaseReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
		    String action = intent.getAction();
		    if (TextUtils.isEmpty(action)){
		        return;
            }
			onBroadCastReceive(context, action ,intent);
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
			registerReceiver(receiver , filter);
		}
	}
	
	/**
	 * 注销广播。
	 */
	private void unRegister(){
		if (null != receiver && !addBroadCastAction().isEmpty()) {
		    unregisterReceiver(receiver);
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
	protected void onBroadCastReceive(Context context, String action, Intent intent){
		
	}

	/**
	 * 是否需要滑动关闭。
	 * @return
	 */
	protected boolean needScrollToFinish(){
		return true;
	}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

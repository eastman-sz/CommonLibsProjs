package com.common.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * 定义RelativeLayout的基类。
 * @author E
 */
public class BaseRelativeLayout extends RelativeLayout {

	protected Context context = null;
	
	public BaseRelativeLayout(Context context) {
		super(context);
		this.context = context;
	}

	public BaseRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public BaseRelativeLayout(Context context, AttributeSet attrs,
                              int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}
	
	protected void init(){
		initViews();
		initTitle();
		initListener();		
	}
	
	/**
	 * 初始化VIEW
	 */
	protected void initViews(){
	}
	
	/**
	 * 初始化标题
	 */
	protected void initTitle(){
	}
	
	/**
	 * 初始化Listener
	 */
	protected void initListener(){
	}	
	
	/**
	 * 刷新(仿手动)主要是再次进入页面的时候刷新数据
	 */
	public void freshByHand(boolean forceUpdate){
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		register();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		unRegister();
	}
	
	private class BaseReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			onBroadCastReceive(context, intent.getAction() ,intent);
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
		try {
			if (null != receiver && !addBroadCastAction().isEmpty()) {
				context.unregisterReceiver(receiver);
				receiver = null;
			}
		}catch (Exception e){
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
	protected void onBroadCastReceive(Context context, String action ,Intent intent){
		
	}	

	
}

package com.gameruinon.alipay;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;

public class PayDemoActivity extends Activity {

	private static final int SDK_CHECK_FLAG = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
	}

	
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};
	
	/**
	 * call alipay sdk pay. 调用SDK支付
	 */
	public void pay(View v) {
		AliPayEntrance.getInstance().addAliPayListener(new AliPayListener(){
			@Override
			public void onSuccess(String statusCode) {
				Toast.makeText(PayDemoActivity.this, "支付成功",
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onWaittingForSure(String statusCode) {
				Toast.makeText(PayDemoActivity.this, "支付结果确认中",
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onCancel(String statusCode) {
				Toast.makeText(PayDemoActivity.this, "支付取消",
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFailed(String statusCode) {
				Toast.makeText(PayDemoActivity.this, "支付失败",
						Toast.LENGTH_SHORT).show();
			}
		});
		AliPayEntrance.getInstance().pay(PayDemoActivity.this , "测试的商品" ,"该测试商品的详细描述" , "0.01" , null);
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(PayDemoActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}
}

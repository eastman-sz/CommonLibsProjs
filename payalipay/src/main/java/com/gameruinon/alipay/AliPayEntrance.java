package com.gameruinon.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
/**
 * 阿里支付（支付宝）的入口。
 * @author E
 */
public class AliPayEntrance {
	
	private static AliPayEntrance aliPayEntrance = null;
	//商户私钥，pkcs8格式
	private static final String RSA_PRIVATE = PayConstant.RSA_PRIVATE;
	//支付的WHATS
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	//回调
	private ArrayList<AliPayListener> listenerList = new ArrayList<AliPayListener>();
	//支付的公共参数
	private AlipayParmas alipayParmas = null;
	
	public static AliPayEntrance getInstance(){
		if (null == aliPayEntrance) {
			aliPayEntrance = new AliPayEntrance();
		}
		return aliPayEntrance;
	}

	Handler handler = new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SDK_PAY_FLAG:
				PayResult payResult = new PayResult((String) msg.obj);
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					//成功
					for (AliPayListener listener : listenerList) {
						listener.onSuccess(resultStatus);
					}
				}else {
					if (TextUtils.equals(resultStatus, "8000")) {
						//等待确认
						for (AliPayListener listener : listenerList) {
							listener.onWaittingForSure(resultStatus);
						}
					}else if (TextUtils.equals(resultStatus, "6001")) {
						//取消
						for (AliPayListener listener : listenerList) {
							listener.onCancel(resultStatus);
						}
					}else {
						//失败
						for (AliPayListener listener : listenerList) {
							listener.onFailed(resultStatus);
						}
					}
				}
				for (AliPayListener listener : listenerList) {
					listener.onResult(payResult);
				}
				break;
			case SDK_CHECK_FLAG:
				break;
			default:
				break;
			}
		}
	};
	
	public void addAliPayListener(AliPayListener aliPayListener){
		if (null != aliPayListener) {
			listenerList.add(aliPayListener);
		}
	}
	
	public void setAlipayParmas(AlipayParmas alipayParmas) {
		this.alipayParmas = alipayParmas;
		AliPayOrder.getInstance().setAlipayParmas(alipayParmas);
	}

	/**
	 * 阿里（支付宝支付接口）。
	 * @param context 上下文环境，必须是Activity.（不能为空）
	 * @param productName 商品名称 （不能为空）
	 * @param productDetail 商品的详细描述（128位以内）（不能为空）
	 * @param productPrice 商品价格(以元为单位，最多支持两位小数点)（不能为空）
	 * @param tradeNo 订单号 商品的订单号，方便对账用 （不能为空，若为空，则会采用系统生成的订单号）
	 */
	public void pay(final Activity context , String productName , String productDetail , String productPrice , String tradeNo){
		String orderInfo = AliPayOrder.getInstance().getOrderInfo(productName, productDetail, productPrice , tradeNo);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	/**
	 * 设置服务器回调地址。
	 * @param callBackUrl
	 */
	public void setCallBackUrl(String callBackUrl){
		AliPayOrder.getInstance().setCallBackUrl(callBackUrl);
	}
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * @param content 待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, null == alipayParmas ? RSA_PRIVATE : alipayParmas.getRsa_private());
	}
	
	/**
	 * get the sign type we use. 获取签名方式
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	

}

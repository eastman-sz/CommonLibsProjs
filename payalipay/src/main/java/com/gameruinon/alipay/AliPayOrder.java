package com.gameruinon.alipay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.text.TextUtils;
/**
 * 订单内容（包括商品名称，商品的详细描述 ， 价格等）
 * @author E
 */
public class AliPayOrder {

	//商户PID
	private static final String PARTNER = PayConstant.PARTNER;
	//商户收款账号
	private static final String SELLER = PayConstant.SELLER;
	
	private static AliPayOrder aliPayOrder = null;
	//"http://notify.msp.hk/notify.htm"
	private String CALL_BACK_URL = "http://sg.iwangyou.com/pcb/alipayCallback";
	
	//支付的公共参数
	private AlipayParmas alipayParmas = null;
	
	public static AliPayOrder getInstance(){
		if (null == aliPayOrder) {
			aliPayOrder = new AliPayOrder();
		}
		return aliPayOrder;
	}
	
	public void setAlipayParmas(AlipayParmas alipayParmas) {
		this.alipayParmas = alipayParmas;
	}

	/**
	 * 生成订单信息。
	 * @param subject 商品名称
	 * @param body 商品详情
	 * @param price 商品金额(以RMB元为单位)
	 * @return 订单信息
	 */
	public String getOrderInfo(String subject, String body, String price , String tradeNo) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + (null == alipayParmas ? PARTNER : alipayParmas.getPartner()) + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + (null == alipayParmas ? SELLER : alipayParmas.getSeller()) + "\"";

		String outTradeNo = TextUtils.isEmpty(tradeNo) ? getOutTradeNo() : tradeNo;
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + (null == alipayParmas ? CALL_BACK_URL : alipayParmas.getCallbackUrl())
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	/**
	 * 设置服务器端的回调地址
	 * @param callBackUrl 服务器端的回调地址
	 */
	public void setCallBackUrl(String callBackUrl){
		this.CALL_BACK_URL = callBackUrl;
		if (TextUtils.isEmpty(this.CALL_BACK_URL)) {
			this.CALL_BACK_URL = "http://notify.msp.hk/notify.htm";
		}
	}
	
	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
	

}

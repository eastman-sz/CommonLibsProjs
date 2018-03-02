package com.utils.lib.ss.info;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * SIM卡信息。
 * @author E
 */
public class SIMCardInfo {

	/**
	 * 获取当前用户手机号。
	 * @param context 上下文环境
	 * @return 当前用户手机号
	 */
	public static String getNativePhoneNumber(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String NativePhoneNumber=null;
		NativePhoneNumber=telephonyManager.getLine1Number();
		return NativePhoneNumber;
	}
	
	/**
	 * 判断用机是否有安装SIM卡。
	 * @param context 上下文环境
	 * @return boolean
	 */
	public static boolean hasSIMCard(Context context){
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = telephonyManager.getSimState();
		return TelephonyManager.SIM_STATE_ABSENT != simState && TelephonyManager.SIM_STATE_UNKNOWN != simState;
	}
	
	/**
	 * 获取服务商名称。
	 * @param context 上下文环境
	 * @return 服务商名称
	 */
	public String getProvidersName(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String ProvidersName = null;
		// 返回唯一的用户ID;就是这张卡的编号神马的
		String IMSI = telephonyManager.getSubscriberId();
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			ProvidersName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "中国电信";
		}
		return ProvidersName;
	}
	
}

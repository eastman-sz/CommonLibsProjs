package com.libs.module.ble.tizhicheng;

import android.text.TextUtils;
import com.ble.lib.util.CommonBleUtils;
/**
 * Created by E on 2018/1/4.
 */
public class TzcMath {

    /**
     * 新体脂秤生成校验码。取两位，不足两位，前面补0，大于两位，取后两位。
     * @param hexStr 16进制数据
     * @return 校验码
     */
    public static String calChecksum(String hexStr){
        if (TextUtils.isEmpty(hexStr)){
            return "00";
        }
        int length = hexStr.length();
        int num = length/2;
        int index = 0;
        int total = 0;
        for (int i = 0 ; i < num ; i++){
            String hex = hexStr.substring(index , index + 2);
            total += CommonBleUtils.hex2Integral(hex);
            index += 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        int len = hex.length();
        if (len < 2){
            hex = "0" + hex;
        }
        return hex;
    }


}

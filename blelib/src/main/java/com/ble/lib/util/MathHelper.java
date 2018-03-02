package com.ble.lib.util;

import java.math.BigDecimal;

/**
 * Created by E on 2017/12/11.
 */
public class MathHelper {

    /**
     * Returns a new BigDecimal whose value is this * multiplicand. The scale of the result is the sum of the scales of the two arguments.
     * @param a
     * @param b
     * @return this * multiplicand.
     */
    public static float multiplyF(double a , double b){
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * Returns a new BigDecimal whose value is this / divisor.
     * As scale of the result the parameter scale is used. If rounding is required to meet the specified scale,
     * then the specified rounding mode roundingMode is applied.
     * @param a 除数
     * @param divisor 被除数
     * @return  a new BigDecimal whose value is this / divisor.
     */
    public static float divideF(double a , double divisor){
        if (0 == divisor) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(divisor);
        float result =  b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).floatValue();
        return result;
    }


}

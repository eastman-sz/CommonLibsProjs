package com.common.util

import android.content.Context

class Utils {

    companion object {

        /**
         * 根据手机的分辨率从 DP 的单位 转成为 PX(像素)。
         * @param context 上下文环境
         * @param dpValue DP值
         * @return PX值
         */
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

    }

}
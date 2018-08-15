package com.sz.ss.gd.map

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

class GdLocationHelper {

    private var context: Activity ?= null

    private var aMapLocationClient: AMapLocationClient? = null

    private val gdLocationGpsHelper = GdLocationGpsHelper()

    constructor(context: Activity , onGdLocationChangedListener : OnGdLocationChangedListener){
        this.context = context
        gdLocationGpsHelper.onGdLocationChangedListener = onGdLocationChangedListener
    }

    //开始定位
    fun startLocation(){
        GdConfig.onLocationPermissionRequesterListener = object : OnLocationPermissionRequesterListener{
            override fun onDeny() {
            }
            override fun onGranted() {
                setLocationEnabled()
            }
        }
        context?.startActivity(Intent(context , LocationPermissionActivity::class.java))
    }

    private fun setLocationEnabled(){
        initLocation()
        aMapLocationClient?.startLocation()
    }

    //停止定位
    fun stopLocation() {
        aMapLocationClient?.let {
            if (it.isStarted){
                it.stopLocation()
            }
        }
    }

    //销毁定位服务
    fun destroyLocation(){
        aMapLocationClient?.let {
            if (it.isStarted){
                it.stopLocation()
                it.unRegisterLocationListener(aMapLocationListener)
                it.onDestroy()
            }
        }
    }

    private fun initLocation() {
        if (null == aMapLocationClient){
            aMapLocationClient = AMapLocationClient(context)
            aMapLocationClient?.setLocationListener(aMapLocationListener)
            aMapLocationClient?.setLocationOption(getDefaultOption())
        }
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     */
    private fun getDefaultOption(): AMapLocationClientOption {
        val mOption = AMapLocationClientOption()
        mOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        //mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);//省电模式
        mOption.isGpsFirst = false//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.httpTimeOut = 30000//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.interval = 2000//可选，设置定位间隔。默认为2秒
        mOption.isNeedAddress = true//可选，设置是否返回逆地理地址信息。默认是true
        mOption.isOnceLocation = false//可选，设置是否单次定位。默认是false
        mOption.isOnceLocationLatest = false//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.isSensorEnable = false//可选，设置是否使用传感器。默认是false
        mOption.isWifiScan = true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.isLocationCacheEnable = false //可选，设置是否使用缓存定位，默认为true
        return mOption
    }

    private val aMapLocationListener : AMapLocationListener = AMapLocationListener{
            gdLocationGpsHelper.onLocationChanged(it)
    }

}
package com.libs.module.gd

import android.os.Bundle
import android.util.Log
import com.alibaba.fastjson.JSON
import com.common.base.BaseAppCompactActivitiy
import com.common.libs.proj.R
import com.sz.ss.gd.map.GdLocationHelper
import com.sz.ss.gd.map.GdMapLocation
import com.sz.ss.gd.map.OnGdLocationChangedListener
import kotlinx.android.synthetic.main.activity_gd_location.*

class GdLocationActivity : BaseAppCompactActivitiy() {

    private var gdLocationHelper : GdLocationHelper ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gd_location)

        initActivitys()

        gdLocationHelper = GdLocationHelper(this , object : OnGdLocationChangedListener{
            override fun onLocationChanged(latitude: Double, longitude: Double, jsonInfo: String?) {
                Log.e("定位服务" , "位置信息11:   $jsonInfo")

                gpsInfoTextView.text = jsonInfo

                try {
                    val gdMapLocation = JSON.parseObject(jsonInfo , GdMapLocation::class.java)

                    Log.e("定位服务" , "位置信息22:   ${gdMapLocation.address}")
                }catch (e : Exception){
                    e.printStackTrace()
                    Log.e("定位服务" , "位置信息33:   ${e?.localizedMessage}")
                }
            }
        })

        gdLocationHelper?.startLocation()
    }

    override fun onPause() {
        super.onPause()
        gdLocationHelper?.stopLocation()
    }

    override fun onDestroy() {
        gdLocationHelper?.destroyLocation()
        super.onDestroy()
    }


}

package com.sz.ss.gd.map

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main_gd.*

class MainGdActivity : AppCompatActivity() {

    private var gdLocationHelper : GdLocationHelper ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_gd)

        gdLocationHelper = GdLocationHelper(this , object : OnGdLocationChangedListener{
            override fun onLocationChanged(latitude: Double, longitude: Double, jsonInfo: String?) {
                Log.e("定位服务" , "位置信息0:   $jsonInfo")

                gpsInfoTextView.text = jsonInfo
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

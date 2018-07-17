package com.sz.ss.gd.map

import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import org.json.JSONObject
import java.util.ArrayList

class GdLocationGpsHelper {

    //只保留最后一个位置点
    private val lastLatLngs = ArrayList<LatLng>()
    var onGdLocationChangedListener : OnGdLocationChangedListener ?= null

    fun onLocationChanged(aMapLocation: AMapLocation){
        try {
            val errorCode = aMapLocation.errorCode
            if (0 != errorCode){
                //出现定位异常或key不对
                return
            }

            //判断两点间位置有否有变化
            val latLng = LatLng(aMapLocation.latitude, aMapLocation.longitude)

            if (lastLatLngs.isEmpty()){
                //第一个点
                lastLatLngs.add(latLng)
                //回调
                //回调
                onGdLocationChangedListener?.onLocationChanged(aMapLocation.latitude , aMapLocation.longitude , toJson(aMapLocation))

                return
            }

            val equals = latLng == lastLatLngs[0]
            if (equals) {
                //位置没有变化
                Log.e("gdGps" , "location unchanged")
                return
            }

            //位置变化后计算两点间的距离
            val distance = AMapUtils.calculateLineDistance(latLng, lastLatLngs[0])
            if (distance == 0.0f) {//位置没有变化或者GPS点出现异常
                return
            }

            lastLatLngs.clear()
            lastLatLngs.add(latLng)

            //回调
            onGdLocationChangedListener?.onLocationChanged(aMapLocation.latitude , aMapLocation.longitude , toJson(aMapLocation))

        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun toJson(it: AMapLocation) : String?{
        val jsonObject = JSONObject()
        jsonObject.put("latitude" , it.latitude)
        jsonObject.put("longitude" , it.longitude)
        jsonObject.put("province" , it.province)
        jsonObject.put("city" , it.city)
        jsonObject.put("district" , it.district)
        jsonObject.put("cityCode" , it.cityCode)
        jsonObject.put("adCode" , it.adCode)
        jsonObject.put("address" , it.address)
        jsonObject.put("country" , it.country)
        jsonObject.put("poiName" , it.poiName)
        jsonObject.put("street" , it.street)
        jsonObject.put("streetNum" , it.streetNum)
        jsonObject.put("aoiName" , it.aoiName)
        jsonObject.put("errorCode" , it.errorCode)
        jsonObject.put("errorInfo" , it.errorInfo)
        jsonObject.put("locationDetail" , it.locationDetail)
        jsonObject.put("locationType" , it.locationType)
        jsonObject.put("speed" , it.speed)

        return jsonObject.toString()
    }

}
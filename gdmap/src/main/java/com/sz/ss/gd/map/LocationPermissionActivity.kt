package com.sz.ss.gd.map

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
/**
 * Check permission.
 * @author E
 */
class LocationPermissionActivity : Activity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 5535

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasLocationPermission = LocationPermissionHelper.hasLocationPermission(this)
        when(hasLocationPermission){
            true ->{
                finish()

                GdConfig.onLocationPermissionRequesterListener?.onGranted()

            }

            false ->{
                LocationPermissionHelper.requestLocationPermission(this , LOCATION_PERMISSION_REQUEST_CODE)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE ->{
                val hasLocationPermission = LocationPermissionHelper.hasLocationPermission(this)

                when(hasLocationPermission){
                    true ->{
                        finish()

                        GdConfig.onLocationPermissionRequesterListener?.onGranted()

                    }


                    false ->{
                        Toast.makeText(this , "没有权限将不能使用定位服务" , Toast.LENGTH_SHORT).show()

                        finish()

                        GdConfig.onLocationPermissionRequesterListener?.onDeny()
                    }

                }
            }
        }


    }



}

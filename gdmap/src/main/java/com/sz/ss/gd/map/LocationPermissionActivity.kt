package com.sz.ss.gd.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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

                        //点击了拒绝，并不再弹出权限窗口时
                        try {
                            val asked =  shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)

                            if (!asked){
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)

                                return
                            }
                        }catch (e : Exception){
                            finish()

                            GdConfig.onLocationPermissionRequesterListener?.onDeny()
                        }


                        Toast.makeText(this , "没有权限将不能使用定位服务" , Toast.LENGTH_SHORT).show()

                        finish()

                        GdConfig.onLocationPermissionRequesterListener?.onDeny()
                    }

                }
            }
        }


    }

    override fun shouldShowRequestPermissionRationale(permission: String?): Boolean {
        return super.shouldShowRequestPermissionRationale(permission)
    }

    override fun onRestart() {
        super.onRestart()
        finish()

        val hasLocationPermission = LocationPermissionHelper.hasLocationPermission(this)
        when(hasLocationPermission){
            true ->{
                GdConfig.onLocationPermissionRequesterListener?.onGranted()
            }

            false -> {
                GdConfig.onLocationPermissionRequesterListener?.onDeny()
            }
        }
    }


}

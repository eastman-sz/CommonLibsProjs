package com.sz.ss.gd.map;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
/**
 * Created by E on 2016/12/20.
 */
public class LocationPermissionHelper {

    public static boolean hasLocationPermission(Activity activity){
        int permission = ContextCompat.checkSelfPermission(activity , Manifest.permission.ACCESS_FINE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermission(Activity activity , int requestCode){
        if (hasLocationPermission(activity)){
            return;
        }
        ActivityCompat.requestPermissions(activity , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , requestCode);
    }


}

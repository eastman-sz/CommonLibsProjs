package com.photo.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
/**
 * Created by E on 2016/12/20.
 */
public class ImgPermissionHelpler {

    public static void requestTakePhotoPermissions(Activity activity){
        int permisson_2 = ContextCompat.checkSelfPermission(activity , Manifest.permission.CAMERA);
        int permisson_3 = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE);
        ArrayList<String> permission_list = new ArrayList<String>();

        if (permisson_2 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.CAMERA);
        }
        if (permisson_3 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permission_list.isEmpty()){
            return;
        }

        int size = permission_list.size();
        String[] permissions = new String[size];
        for (int i = 0 ; i < size ; i ++){
            permissions[i] = permission_list.get(i);
        }

        ActivityCompat.requestPermissions(activity , permissions , 1560);
    }

    public static void requestImgPermissions(Activity activity){
        int permisson_3 = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE);
        ArrayList<String> permission_list = new ArrayList<String>();

        if (permisson_3 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permission_list.isEmpty()){
            return;
        }

        int size = permission_list.size();
        String[] permissions = new String[size];
        for (int i = 0 ; i < size ; i ++){
            permissions[i] = permission_list.get(i);
        }

        ActivityCompat.requestPermissions(activity , permissions , 1570);
    }

    public static boolean hasValiedImgPermission(Activity activity){
        int permissonStorage = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissonStorage == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasValiedTakePhotoPermission(Activity activity){
        int permissonCamera = ContextCompat.checkSelfPermission(activity , Manifest.permission.CAMERA);
        int permissonStorage = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissonCamera == PackageManager.PERMISSION_GRANTED &&  permissonStorage == PackageManager.PERMISSION_GRANTED;
    }

}

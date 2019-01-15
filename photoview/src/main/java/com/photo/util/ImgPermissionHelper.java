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
public class ImgPermissionHelper {

    public static void requestTakePhotoPermissions(Activity activity){
        int permission_2 = ContextCompat.checkSelfPermission(activity , Manifest.permission.CAMERA);
        int permission_3 = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE);
        ArrayList<String> permission_list = new ArrayList<String>();

        if (permission_2 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.CAMERA);
        }
        if (permission_3 != PackageManager.PERMISSION_GRANTED){
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
        int permission_3 = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionStorageW = ContextCompat.checkSelfPermission(activity , Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ArrayList<String> permission_list = new ArrayList<String>();

        if (permission_3 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionStorageW != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
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

    public static boolean hasValidImgPermission(Activity activity){
        int permissionStorage = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionStorageW = ContextCompat.checkSelfPermission(activity , Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionStorage == PackageManager.PERMISSION_GRANTED
                && permissionStorageW == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasValidTakePhotoPermission(Activity activity){
        int permissionCamera = ContextCompat.checkSelfPermission(activity , Manifest.permission.CAMERA);
        int permissionStorage = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionStorageW = ContextCompat.checkSelfPermission(activity , Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCamera == PackageManager.PERMISSION_GRANTED
                &&  permissionStorage == PackageManager.PERMISSION_GRANTED
                &&  permissionStorageW == PackageManager.PERMISSION_GRANTED
                ;
    }

}

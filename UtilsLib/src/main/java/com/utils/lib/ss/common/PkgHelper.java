package com.utils.lib.ss.common;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.utils.lib.ss.info.LocalAppInfo;
/**
 * 应用辅助类。检查应用是否安装等。
 * @author E
 */
public class PkgHelper {

	
	/**
	 * 取得版本号。
	 * @param context 上下文环境
	 * @return 版本号
	 */
	public static String getVersionName(Context context){
		try {
			String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
			return versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
    /**
     * 检测应用是否安装
     * @param context 上下文环境
     * @param pkgName 包名
     * @return boolean
     */
	public static boolean checkInstall(Context context, String  pkgName) {
		if(CheckHelper.isNullOrEmpty(pkgName)) {
			return false;
		}
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info=pm.getPackageInfo(pkgName, 0);
			if(null != info) {
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 安装应用。
	 * @param context 上下文环境
	 * @param path 应用路径
	 */
	public static void instalAPK(Context context, String path) {
		if(CheckHelper.isNullOrEmpty(path)) {
			throw new NullPointerException("path is not allowed to be null or empty");
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 打开应用
	 * @param context 上下文环境
	 * @param pkgName 包名
	 * @return boolean 能打开返回True , 不能打开返回False
	 */
	public static boolean openAPKByPkgName(Context context, String  pkgName) {
		if(CheckHelper.isNullOrEmpty(pkgName)) {
			//pkgName is not allowed to be null or empty
			return false;
		}
		PackageManager pm = context.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(pkgName); 
        if (intent != null)  {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        	context.startActivity(intent);
        	return true;
        }
        return false;
	}
	
	/**
	 * 获取未安装应用的包名。
	 * @param context 上下文环境
	 * @param archiveFilePath 未安装应用的路径
	 * @return 包名
	 */
	public static String getUninstallAPKPkgName(Context context ,String archiveFilePath) {
		if(CheckHelper.isNullOrEmpty(archiveFilePath)) {
			return null;
		}
		String packageName = null;
		PackageManager pm = context.getPackageManager();
		PackageInfo pakinfo = pm.getPackageArchiveInfo(archiveFilePath,PackageManager.GET_ACTIVITIES);
		if (pakinfo!=null) {
			packageName=pakinfo.packageName;
		}
		return packageName;
	}
	
	/**
	 * 获取未安装应用的版本号。
	 * @param context 上下文环境
	 * @param archiveFilePath 未安装应用的路径
	 * @return 未安装应用的版本号
	 */
	public static String getUninstallAPKVesionName(Context context,String archiveFilePath) {
		if(CheckHelper.isNullOrEmpty(archiveFilePath)) {
			return null;
		}
		String versionName = null;
		PackageManager pm = context.getPackageManager();
		PackageInfo pakinfo = pm.getPackageArchiveInfo(archiveFilePath,PackageManager.GET_ACTIVITIES);
		if (pakinfo!=null) {
			versionName=pakinfo.versionName;
		}
		return versionName;
	}

	/**
	 * 获取本地应用的版本号。
	 * @param context 上下文环境
	 * @param pkgName 包名
	 * @return 本地应用的版本号
	 */
	public static String getLocalAppVersionName(Context context, String pkgName) {
		if(CheckHelper.isNullOrEmpty(pkgName)) {
			return null;
		}
		String versionName = null;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(pkgName, 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}  
		return versionName;
	}

	public static LocalAppInfo getLocalAppInfo(Context context , String pkgName){
	    try{
            PackageManager manager = context.getPackageManager();

            PackageInfo info = context.getPackageManager().getPackageInfo(pkgName, 0);
            Drawable icon = manager.getApplicationIcon(info.applicationInfo.packageName);
            String labelName = (String) info.applicationInfo.loadLabel(manager);
            String version = info.versionName;

            LocalAppInfo localAppInfo = new LocalAppInfo(pkgName, labelName, version, icon);

            return localAppInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * 查询手机内非系统系统应用
	 * @param context 上下文环境
	 * @return 所有的非系统应用。
	 */
	public static ArrayList<LocalAppInfo> getLocalInstalPackage(Context context){
		ArrayList<LocalAppInfo> loaclGames = new ArrayList<LocalAppInfo>();
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> pakList = manager.getInstalledPackages(0);
		try {
			for(PackageInfo info:pakList){
				if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
//					Drawable icon = manager.getApplicationIcon(info.applicationInfo.packageName);
					Drawable icon =info.applicationInfo.loadIcon(manager);
					String labelName = (String) info.applicationInfo.loadLabel(manager);
					String pkgName = info.packageName;
					String version = info.versionName;
					LocalAppInfo localAppInfo = new LocalAppInfo(pkgName, labelName, version, icon);
					loaclGames.add(localAppInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loaclGames;
	}

    /**
     * 查询手机内非系统系统应用
     * @param context 上下文环境
     * @return 所有的非系统应用。
     */
    public static ArrayList<LocalAppInfo> getLocalSystemInstalPackage(Context context){
        ArrayList<LocalAppInfo> loaclGames = new ArrayList<LocalAppInfo>();
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> pakList = manager.getInstalledPackages(0);
        try {
            for(PackageInfo info:pakList){
                if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                    //Drawable icon = manager.getApplicationIcon(info.applicationInfo.packageName);
                    Drawable icon =info.applicationInfo.loadIcon(manager);
                    String labelName = (String) info.applicationInfo.loadLabel(manager);
                    String pkgName = info.packageName;
                    String version = info.versionName;
                    LocalAppInfo localAppInfo = new LocalAppInfo(pkgName, labelName, version, icon);
                    loaclGames.add(localAppInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loaclGames;
    }

    /**
     * 查询手机内所有应用
     * @param context 上下文环境
     * @return 所有的非系统应用。
     */
    public static ArrayList<LocalAppInfo> getAllInstalPackage(Context context){
        ArrayList<LocalAppInfo> loaclGames = new ArrayList<LocalAppInfo>();
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> pakList = manager.getInstalledPackages(0);
        try {
            for(PackageInfo info:pakList){
                //if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0){
                Drawable icon = manager.getApplicationIcon(info.applicationInfo.packageName);
                String labelName = (String) info.applicationInfo.loadLabel(manager);
                String pkgName = info.packageName;
                String version = info.versionName;
                LocalAppInfo localAppInfo = new LocalAppInfo(pkgName, labelName, version, icon);
                loaclGames.add(localAppInfo);
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loaclGames;
    }
	
	/**
	 * 判断应用是否在前台运行。
	 * @return boolean boolean
	 */
	public static boolean isAppOnForeground(Context context){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null) {
			return false;
		} else {
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.processName.equals(packageName)
						&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获得属于桌面的应用的应用包名称
	 * @return 返回包含所有包名的字符串列表
	 */
	private static ArrayList<String> getHomes(Context context) {
		ArrayList<String> names = new ArrayList<String>();
		PackageManager packageManager = context.getPackageManager();
		//属性
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo ri : resolveInfo){
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}

	/**
	 * 判断当前界面是否是桌面
	 */
	public static boolean isHome(Context context){
		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		String runningPackage = rti.get(0).topActivity.getPackageName();
		ArrayList<String> desktopAppPackages = getHomes(context);
//		for (int i = 0; i < desktopAppPackages.size(); i++) {
//			Log.e("桌面包名", desktopAppPackages.get(i) + "-----");
//		}
//		Log.e("第一个", rti.get(0).topActivity.getPackageName());
		return desktopAppPackages.contains(runningPackage);
	}
	
	
}

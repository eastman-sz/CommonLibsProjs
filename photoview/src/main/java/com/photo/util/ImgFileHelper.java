package com.photo.util;

import android.os.Environment;
import java.io.File;
/**
 * 文件处理类。
 * @author E
 */
public class ImgFileHelper {

	//统一的项目存储地址
	private static String  UNIFROM_STORE_FILE_PATH = Environment.getExternalStorageDirectory() + "/sdk/";
	//图片存储地址
	private static String  UNIFROM_IMAGE_FILE_PATH = UNIFROM_STORE_FILE_PATH + "images/";

	/**
	 * 取得统一的存储图片的目录文件夹。
	 * @return 存储图片的文件夹
	 */
	public static File getImageDirFile(){
		return makeFileDirs(UNIFROM_IMAGE_FILE_PATH);
	}

	private static File makeFileDirs(String path){
		try {
			File dirFile = new File(path);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			return dirFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}

package com.photo.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 图像处理类。
 * @author E
 */
public class PhotoImgpHelper {

	public static String saveBitmapToFile(Bitmap bitmap){
		File file = saveBmpToFile(bitmap, getFileName(), 100);
		return null == file ? null : file.getAbsolutePath();
	}

	/**
	 * 将图片存入本地并返回图片文件。
	 * @param bitmap 要存入的图片
	 * @param fileName 图片文件名
	 * @param quality 图片质量0 ~ 100
	 * @return 图片文件
	 */
	public static File saveBmpToFile(Bitmap bitmap ,String fileName , int quality){
		boolean sdExist = sdCardExist();
		if (null == bitmap || !sdExist || null == fileName ||"".equals(fileName)) {
			return null;
		}
		try {
			File file = new File(ImgFileHelper.getImageDirFile() , fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fOut = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, quality, fOut);
			fOut.flush();
			fOut.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return degree;
		}
		return degree;
	}
	
	/**
	 * 旋转图片，使图片保持正确的方向。
	 * @param bitmap 原始图片
	 * @param degrees 原始图片的角度
	 * @return Bitmap 旋转后的图片
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
		if (degrees == 0 || null == bitmap) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		if (null != bitmap) {
			bitmap.recycle();
		}
		return bmp;
	}
	
	/**
	 * 用当前时间给取得的图片命名。
	 * @return 唯一的值，做为文件名
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getFileName() {
		Date date = new Date(System.currentTimeMillis());
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String addName = (""+System.currentTimeMillis()).substring(9);
		return dateFormat.format(date)  + addName + ".png";
	}

    /**
     * 检查是否有装SD卡。
     * @return boolean boolean
     */
    public static boolean sdCardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
	
}

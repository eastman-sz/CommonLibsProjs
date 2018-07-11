package com.photo.third;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * 在使用开源图片组件ImageLoader中，对传递的 ImageOptions的一个封装。
 * @author E
 */
public class ImageOptions {

	/**
	 * 初始化ImageOtions。
	 * @param defaultImageRes 默认图片的ID
	 * @param imageDegree 图片显示出来的角度，如圆形(360度)
	 * @return ImageOtions
	 */
	public static DisplayImageOptions initImageOptions(int defaultImageRes , int imageDegree){
		return initImageOptions(defaultImageRes, defaultImageRes, defaultImageRes, imageDegree);
	}
	
	/**
	 * 初始化ImageOtions。
	 * @return ImageOtions DisplayImageOptions
	 */
	public static DisplayImageOptions initImageOptions(){
		return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
	}	
	
	/**
	 * 初始化ImageOtions。
	 * @param defaultImageRes 默认图片的ID
	 * @return ImageOtions
	 */
	public static DisplayImageOptions initImageOptions(int defaultImageRes){
		return initImageOptions(defaultImageRes, defaultImageRes, defaultImageRes);
	}
	
	/**
	 * 初始化ImageOtions。
	 * @param deftImg  在加载过程中显示的图片
	 * @param empthUrlImg 当链接地址为空的时候显示的图片
	 * @param failedImg 当加载失败时显示的图片
	 * @return imageOptions
	 */
	public static DisplayImageOptions initImageOptions(int deftImg , int empthUrlImg , int failedImg){
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(deftImg)
		.showImageForEmptyUri(empthUrlImg)
		.showImageOnFail(failedImg)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new SimpleBitmapDisplayer())
		.build();
		return imageOptions;
	}
	
	/**
	 * 初始化ImageOtions。
	 * @param deftImg 在加载过程中显示的图片
	 * @param empthUrlImg 当链接地址为空的时候显示的图片
	 * @param failedImg 当加载失败时显示的图片
	 * @param imageDegree 图片显示的角度
	 * @return imageOptions
	 */
	public static DisplayImageOptions initImageOptions(int deftImg , int empthUrlImg , int failedImg , int imageDegree){
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(deftImg)
		.showImageForEmptyUri(empthUrlImg)
		.showImageOnFail(failedImg)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(imageDegree))
		.build();
		return imageOptions;
	}
}

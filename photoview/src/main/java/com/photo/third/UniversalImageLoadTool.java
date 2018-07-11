package com.photo.third;

import android.content.Context;
import android.os.Looper;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 图片加载类。
 * @author E
 */
public class UniversalImageLoadTool {

	private static ImageLoader imageLoader = ImageLoader.getInstance();
	
	public static ImageLoader getImageLoader(){
		return imageLoader;
	}
	
	public static boolean checkImageLoader(){
		return imageLoader.isInited();
	}
	
	/**
	 * 初始化。
	 * @param context 上下文环境
	 */
	public static void init(Context context){
		if (checkImageLoader()) {
			clear();
			return;
		}
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context).threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCacheExtraOptions(480, 800) 
//                .threadPoolSize(3)
//                .memoryCacheSize(2 * 1024 * 1024) 
//                .memoryCacheSizePercentage(13)  
////                .imageDecoder(new BaseImageDecoder(false))
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheExtraOptions(720, 1080) // default = device screen dimensions
				.diskCacheExtraOptions(720, 1080, null)
				.threadPoolSize(3) // default 3
				.threadPriority(Thread.NORM_PRIORITY - 1) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO) // default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(8) // default 13
				.diskCache(new UnlimitedDiscCache(context.getCacheDir())) // default
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(300)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(context)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//        .writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
	}
	
	/**
	 * 图片加载方法。
	 * @param uri 图片地址
	 * @param imageView  ImageView
	 * @param default_pic 默认图片
	 */
	public static void disPlay(String uri, ImageView imageView ,int default_pic){
		DisplayImageOptions options = ImageOptions.initImageOptions(default_pic);
		uri = ImageUrlUtils.Companion.comfirmHttpUrl(uri);
		imageLoader.displayImage(uri, imageView, options);
	}
	
	/**
	 * 图片加载方法。
	 * @param uri 图片地址
	 * @param imageView  ImageView
	 * @param default_pic 默认图片
	 */
	public static void disPlay(String uri, ImageView imageView ,int default_pic ,ImageLoadingListener listener){
		DisplayImageOptions options = ImageOptions.initImageOptions(default_pic);
		uri = ImageUrlUtils.Companion.comfirmHttpUrl(uri);
		imageLoader.displayImage(uri, imageView, options ,listener);
	}
	
	/**
	 * 图片加载方法。
	 * @param uri 图片地址
	 * @param imageView  ImageView
	 * @param default_pic 默认图片
	 * @param degree 角度
	 */
	public static void disPlay(String uri, ImageView imageView,int default_pic , int degree){
		DisplayImageOptions options = ImageOptions.initImageOptions(default_pic ,degree);
		uri = ImageUrlUtils.Companion.comfirmHttpUrl(uri);
		imageLoader.displayImage(uri, imageView, options);
	}
	
	/**
	 * 图片加载方法。
	 * @param uri 图片地址
	 * @param imageView  ImageView
	 * @param default_pic 默认图片
	 * @param degree 角度
	 */
	public static void disPlay(String uri, ImageView imageView,int default_pic , int degree ,ImageLoadingListener listener){
		DisplayImageOptions options = ImageOptions.initImageOptions(default_pic ,degree);
		uri = ImageUrlUtils.Companion.comfirmHttpUrl(uri);
		imageLoader.displayImage(uri, imageView, options , listener);
	}
	
	/**
	 *  图片加载方法。
	 * @param uri 图片地址
	 * @param imageView  ImageView
	 * @param imageOptions DisplayImageOptions
	 */
	public static void disPlay(String uri , ImageView imageView , DisplayImageOptions imageOptions){
		uri = ImageUrlUtils.Companion.comfirmHttpUrl(uri);
		imageLoader.displayImage(uri, imageView, imageOptions);
	}
	
	/**
	 * 图片加载方法。
	 * @param uri 图片地址
	 * @param imageView  ImageView
	 * @param imageOptions DisplayImageOptions
	 * @param listener 加载状态的监听
	 */
	public static void disPlay(String uri , ImageView imageView , DisplayImageOptions imageOptions , ImageLoadingListener listener){
		uri = ImageUrlUtils.Companion.comfirmHttpUrl(uri);
		imageLoader.displayImage(uri, imageView, imageOptions ,listener);
	}
	
	public static void clear(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();

				imageLoader.clearMemoryCache();
				imageLoader.clearDiskCache();

				Looper.loop();
			}
		}).start();
	}
	
	/**
	 * 列表滑动时调用。
	 * @param scrollState 滑动状态 0停止 ,其他为滑动
	 */
	public static void onScrollStateChanged(int scrollState){
		if (0 == scrollState) {
			resume();
		}else {
			pause();
		}
	}
	
	public static void resume(){
		imageLoader.resume();
	}
	/**
	 * 暂停加载
	 */
	public static void pause(){
		imageLoader.pause();
	}
	/**
	 * 停止加载
	 */
	public static void stop(){
		imageLoader.stop();
	}
	/**
	 * 销毁加载
	 */
	public static void destroy() {
		imageLoader.destroy();
	}
}

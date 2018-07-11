package com.photo.third;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoadListener implements ImageLoadingListener {

	public ImageLoadListener() {
	}
	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
	}
	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
	}
	@Override
	public void onLoadingStarted(String arg0, View arg1) {
	}

}

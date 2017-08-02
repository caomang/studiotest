package com.sunboxsoft.monitor.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import petrochina.ghzy.a10fieldwork.R;

public class ImageLoaderOptions {
	public static DisplayImageOptions list_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.zhaopian)
			.showImageForEmptyUri(R.drawable.zhaopian)
			.showImageOnFail(R.drawable.zhaopian).cacheInMemory(true)// 在内存中会缓存该图片
			.cacheOnDisk(true)// 在硬盘中会缓存该图片
			.considerExifParams(true)// 会识别图片的方向信息
			.displayer(new FadeInBitmapDisplayer(500)).build();
	// .displayer(new RoundedBitmapDisplayer(10)).build();;设置圆角
	public static DisplayImageOptions pager_options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.zhaopian)
			.showImageOnFail(R.drawable.zhaopian)
			.resetViewBeforeLoading(true)//在显示图片之前先清空ImageView上的图片
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY)//会对图片进一步的缩放
			.bitmapConfig(Bitmap.Config.RGB_565)//此种模消耗的内存会很小,2个byte存储一个像素
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300)).build();
	// .displayer(new RoundedBitmapDisplayer(10)).build();;
}

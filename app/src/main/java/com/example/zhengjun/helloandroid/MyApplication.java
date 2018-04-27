package com.example.zhengjun.helloandroid;

import android.app.Application;
import android.graphics.Bitmap;

import com.example.localalbum.common.LocalImageHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class MyApplication extends Application {

	private static MyApplication instance;

	public static MyApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		init();
	}

	private void initImageLoader() {
		File caFile = StorageUtils.getOwnCacheDirectory(this,"UniversalImageLoader/Cache");
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_default)
                 // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.ic_default)
                   // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.ic_default)
                       // 加载失败时的图片
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(false)
				.cacheOnDisk(true) // 启用外存缓存
                          // 启用EXIF和JPEG图像格式
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		UnlimitedDiskCache cakCache = new UnlimitedDiskCache(caFile);
		ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this)
// maxwidth, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
                  // You can pass your own memory cache
                  // implementation/你可以通过自己的内存缓存实现
				.discCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
                // 缓存的文件数量
				.discCache(cakCache)
               // 自定义缓存路径
				.defaultDisplayImageOptions(options)
				.memoryCache(new WeakMemoryCache())
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout
				.writeDebugLogs() // Remove for releaseapp
				.build();
		ImageLoader.getInstance().init(imageLoaderConfiguration);

		LocalImageHelper.init(instance);
	}
	public String getCachePath() {
		File cacheDir;
		if
				(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			cacheDir = getExternalCacheDir();
		else
			cacheDir = getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir.getAbsolutePath();
	}


	private void init() {
		initImageLoader();
	}
}

package com.example.sjyy_expert_android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.sjyy_expert_android.util.CustomProgressDialog;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BaseActivity extends Activity implements OnClickListener {

	private CustomProgressDialog progressDialog = null;
	public MyApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {

	}

	public void goNewActivity(Class clazz) {

		startActivity(new Intent(this, clazz));

	}

	public void showToast(String str) {

		Toast.makeText(this, str, 0).show();
	}

	public void closeKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public void initImageLoader() {
		// File cacheDir = StorageUtils.getOwnCacheDirectory(this,
		// "fdlm/Cache");// 获取到缓存的目录地址
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(
		// this).threadPoolSize(2).denyCacheImageMultipleSizesInMemory()
		// .memoryCache(new LruMemoryCache(3 * 1024 * 1024))
		// .memoryCacheSize(3 * 1024 * 1024).memoryCacheSizePercentage(15)//
		// default
		// .diskCache(new UnlimitedDiscCache(cacheDir))// default
		// .diskCacheSize(70 * 1024 * 1024).diskCacheFileCount(500)
		// .build();
		// ImageLoader.getInstance().init(config);// 全局初始化此配置

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSizePercentage(13) // default
				.defaultDisplayImageOptions(defaultOptions).writeDebugLogs()// Remove
																			// for
																			// release
																			// app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(
		// getApplicationContext()).build();
		// ImageLoader.getInstance().init(config);

	}
	
	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);

		}

		progressDialog.show();
	}

	public void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}

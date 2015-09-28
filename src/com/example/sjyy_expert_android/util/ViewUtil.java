package com.example.sjyy_expert_android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.PreferenceConstans.SP_ConfigManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewUtil {

	private static AlertDialog alertDialog = null;
	private static SharedPreferences config_sp;
	private static boolean isWifi = false;
	protected static ImageLoader imageLoader;
	private static DisplayImageOptions options;

	public static AlertDialog showLargePicDialog(Context context, String name, String specification,
			String description, String picUrl,
			View.OnClickListener closeListerner) {
		if (context == null) {
			return null;
		}
		config_sp = SP_ConfigManager.getConfig_sp(context);
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null) {
			int nType = networkInfo.getType();
			if (nType == ConnectivityManager.TYPE_WIFI) {
				isWifi = true;
			}
		}
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.cacheInMemory(true).cacheOnDisc(true).build();
//		options = Options.getListOptions();
		alertDialog = new AlertDialog.Builder(context).create();
		if (alertDialog != null && !alertDialog.isShowing()) {
			alertDialog.show();
		}
		alertDialog.getWindow().setContentView(R.layout.dialog_show_large_pic);
		TextView tv_name = (TextView) alertDialog.findViewById(R.id.tv_name);
		tv_name.setText(name);
		TextView tv_specification = (TextView) alertDialog.findViewById(R.id.tv_specification);
		if (specification != null && !specification.equals("") && !specification.equals("null")) {
			tv_specification.setVisibility(View.VISIBLE);
			tv_specification.setText(specification);
		}
		TextView tv_description = (TextView) alertDialog
				.findViewById(R.id.tv_description);
		tv_description.setText(description);
		ImageView iv_close = (ImageView) alertDialog
				.findViewById(R.id.iv_close);
		iv_close.setOnClickListener(closeListerner);
		ImageView iv_large_pic = (ImageView) alertDialog
				.findViewById(R.id.iv_large_pic);
		iv_large_pic.setOnClickListener(closeListerner);

//		if (picUrl == null || picUrl.equals("") || picUrl.equals("null")) {
//
//			iv_large_pic.setBackgroundResource(R.drawable.market_photo);
//		} else {
//			AssetManager assets = context.getAssets();
//
//			Bitmap bit = null;
//			try {
//				bit = BitmapFactory.decodeStream(assets.open(picUrl));
//				iv_large_pic.setImageBitmap(bit);
//			} catch (IOException e) {
//				if (config_sp.getBoolean("setting_image", true) || isWifi) {
//					imageLoader = ImageLoader.getInstance();
//					imageLoader.displayImage(UrlConstans.RA_STATIC + picUrl,
//							iv_large_pic, options);
//				} else {
//					iv_large_pic.setBackgroundResource(R.drawable.market_photo);
//				}
//			}
//		}
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(picUrl, iv_large_pic, options);

		return alertDialog;
	}

	public static AlertDialog showDialog(final Activity act, String title,
			String content, String okname, String cancelname,
			OnClickListener ok, OnClickListener cancel) {
		AlertDialog alertDialog = new AlertDialog.Builder(act).create();
		if (alertDialog != null && !alertDialog.isShowing()) {
			alertDialog.show();
		}
		alertDialog.getWindow().setContentView(R.layout.view_dialog);
		TextView tv_title = (TextView) alertDialog.findViewById(R.id.tv_title);
		TextView tv_content = (TextView) alertDialog
				.findViewById(R.id.tv_content);
		final Button btn_cancel = (Button) alertDialog
				.findViewById(R.id.btn_cancel);
		final Button btn_ok = (Button) alertDialog.findViewById(R.id.btn_ok);
		tv_title.setText(title);
		tv_content.setText(content);
		btn_cancel.setText(cancelname);
		btn_ok.setText(okname);
		btn_cancel.setOnClickListener(cancel);
		btn_ok.setOnClickListener(ok);

		return alertDialog;
	}
	
	
	
}

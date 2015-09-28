package com.example.sjyy_expert_android.util;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.example.sjyy_expert_android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewPagerManager {

	private ViewPager viewPager;
	private ArrayList<ImageView> images;
	private MyPagerAdapter adapter;
	private ScheduledExecutorService scheduledExecutor;
	private int currentIndex = 300; // 当前页面的位�?
	private View dot0, dot1, dot2;
	private Activity mContext;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	// 准备初始化数�?
	public void initData(Activity act, ViewPager viewPager,
			LinearLayout dotsContainer, String[] banners,
			final String[] bannerUrl) {
		this.mContext = act;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher).cacheInMemory(false)
				.cacheOnDisc(true).build();
		imageLoader = ImageLoader.getInstance();
		// 用来显示的图�?
		this.viewPager = viewPager;
		
		images = new ArrayList<ImageView>();

		// ImageView view1 = new ImageView(act);
		// view1.setImageResource(R.drawable.img_default_banner);
		// view1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT));
		// view1.setScaleType(ScaleType.FIT_XY);

		ImageView view1 = new ImageView(act);
		view1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		view1.setScaleType(ScaleType.FIT_XY);
		imageLoader.displayImage( banners[0], view1, options);
		if (!bannerUrl[0].equals("0")) {
			view1.setOnClickListener(new ClickListener(act, bannerUrl[0]));
		}

		ImageView view2 = new ImageView(act);
		view2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		view2.setScaleType(ScaleType.FIT_XY);
		imageLoader.displayImage( banners[1], view2,
				options);
		if (!bannerUrl[1].equals("0")) {
			view2.setOnClickListener(new ClickListener(act, bannerUrl[1]));
		}

		ImageView view3 = new ImageView(act);
		view3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		view3.setScaleType(ScaleType.FIT_XY);
		imageLoader.displayImage( banners[2], view3,
				options);
		if (!bannerUrl[2].equals("0")) {
			view3.setOnClickListener(new ClickListener(act, bannerUrl[2]));
		}

		images.add(view1);
		images.add(view2);
		images.add(view3);

		dot0 = (View) dotsContainer.findViewById(R.id.v_dot0);
		dot1 = (View) dotsContainer.findViewById(R.id.v_dot1);
		dot2 = (View) dotsContainer.findViewById(R.id.v_dot2);

		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setCurrentItem(currentIndex);
	}

	class ClickListener implements OnClickListener {

		String bannerUrl;
		Activity act;

		public ClickListener(Activity act, String bannerUrl) {
			this.bannerUrl = bannerUrl;
			this.act = act;
		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
//			Intent intent = new Intent();
//			intent.putExtra("bannerUrl", bannerUrl);
//			intent.setClass(mContext, WebActivity.class);
//			mContext.startActivity(intent);
		}

	}

	public void onStart() {
		// TODO Auto-generated method stub

		 scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		
		 scheduledExecutor.scheduleAtFixedRate(new MyPagerTask(), 8, 8,
		 TimeUnit.SECONDS);

	}

	public void onStop() {
		// TODO Auto-generated method stub
		// 停止切换图片
		 scheduledExecutor.shutdown();
	}

	private class MyPagerTask implements Runnable {

		@Override
		public void run() {
			// 切换页面
//			currentIndex = (currentIndex + 1) % images.size();
			currentIndex++;

//			 viewPager.setCurrentItem(item);//设置当前页面

			// 更新UI Handler
			handler.sendEmptyMessage(0);

		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// super.handleMessage(msg);
			// 切换viewPager当前页面
			try {
				viewPager.setCurrentItem(currentIndex);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// 记录当前页面
			currentIndex = position;
			int dotPosition = position % images.size();
			switch (dotPosition) {
			case 0:
				dot0.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.dot_focused));
				dot1.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.dot_normal));
				dot2.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.dot_normal));
				break;
			case 1:
				dot0.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.dot_normal));
				dot1.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.dot_focused));
				dot2.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.dot_normal));
				break;
			case 2:
				dot0.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.dot_normal));
				dot1.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.dot_normal));
				dot2.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.dot_focused));
				break;

			default:
				break;
			}
		}

	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// return images.size();
			return Integer.MAX_VALUE;

		}

		// 判断当前页面显示的数�?�?新页面的数据是否相同
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		// �?��数据
		@Override
		public void destroyItem(ViewGroup viewPager, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
//			viewPager.removeView(images.get(position % images.size()));
		}

		// 初始化数�?
		@Override
		public Object instantiateItem(ViewGroup viewPager, int position) {
			// TODO Auto-generated method stub
			// viewPager.addView(images.get(position));
			Log.i("现在的页数是position", position + "");
			try {
//				viewPager.addView(images.get(position % images.size()));
				 viewPager.addView(images.get(position % images.size()), 0);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return images.get(position % images.size());
		}
		
		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}

}

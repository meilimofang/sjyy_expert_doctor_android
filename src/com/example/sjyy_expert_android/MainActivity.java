package com.example.sjyy_expert_android;

import java.util.Date;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjyy_expert_android.activity.LoginActivity;
import com.example.sjyy_expert_android.activity.fragment.Appointmentfragment;
import com.example.sjyy_expert_android.activity.fragment.MyOrderfragment;
import com.example.sjyy_expert_android.activity.fragment.MyInformationfragment;
import com.example.sjyy_expert_android.activity.fragment.SeekingNeedsfragment;
import com.example.sjyy_expert_android.entity.BaseRequest;
import com.example.sjyy_expert_android.entity.home.HomePageRespons;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.example.sjyy_expert_android.util.ViewUtil;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private FragmentManager fragmentManager;
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private RelativeLayout rl_home, rl_order, rl_community,
			rl_myinformation;

	private ImageView iv_home_image, iv_order,
			iv_community_image, iv_information_image,iv_yuandian;

	private TextView tv_home_text, tv_order_text,
			tv_community_text, tv_information_text;

	private MyOrderfragment homefra;
	private Appointmentfragment appointmentfra;
	private MyInformationfragment inforfra;
	private SeekingNeedsfragment seekfra;
	public static long lastClickTime = 0l;
	private AlertDialog alertDialog;
	private HomePageRespons base;
	private BaseRequest bean;
	private final static int SUCCESS = 1;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof HomePageRespons) {

					base = (HomePageRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						MyApplication.hasRemandInfo=base.hasRemandInfo;
						if(MyApplication.hasRemandInfo!=0){
							iv_yuandian.setVisibility(View.VISIBLE);
						}else{
							iv_yuandian.setVisibility(View.GONE);
						}
					} else {
						
						MyApplication.hasRemandInfo=0;
						
					}
				} else {
					
					MyApplication.hasRemandInfo=0;
					
				}

				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				 switch (updateStatus) {
			        case UpdateStatus.Yes: // has update
//			            UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
//			            tv_app_update.setText("有新版本");
			        	MyApplication.if_app_update=true;
			            break;
			        case UpdateStatus.No: // has no update
//			        	tv_app_update.setText("已是新版");
			        	
			        	MyApplication.if_app_update=false;
//			        	Toast.makeText(SettingActivity.this, "!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.NoneWifi: // none wifi
//			            Toast.makeText(SettingActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.Timeout: // time out
//			            Toast.makeText(SettingActivity.this, "超时", Toast.LENGTH_SHORT).show();
			            break;
			        }
			}
		});
		UmengUpdateAgent.update(this);
		initview();
		fragmentManager = getSupportFragmentManager();
		setTabSelection(0);
		initImageLoader();
	}

	private void initview() {

		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setText("");
		btn_title_right.setText("");
		tv_title_content.setText("私家医院");
		rl_home = (RelativeLayout) findViewById(R.id.rl_home);
		rl_order = (RelativeLayout) findViewById(R.id.rl_order);
		rl_community = (RelativeLayout) findViewById(R.id.rl_community);
		rl_myinformation = (RelativeLayout) findViewById(R.id.rl_myinformation);

		iv_home_image = (ImageView) findViewById(R.id.iv_home_image);
		iv_order = (ImageView) findViewById(R.id.iv_order);
		iv_community_image = (ImageView) findViewById(R.id.iv_community_image);
		iv_information_image = (ImageView) findViewById(R.id.iv_information_image);

		tv_home_text = (TextView) findViewById(R.id.tv_home_text);
		tv_order_text = (TextView) findViewById(R.id.tv_order_text);
		tv_community_text = (TextView) findViewById(R.id.tv_community_text);
		tv_information_text = (TextView) findViewById(R.id.tv_information_text);
		iv_yuandian=(ImageView) findViewById(R.id.iv_yuandian);
		if(ShareUtil.getAccountId(MainActivity.this)!=-1){
			new HomeThread().start();
		}
		rl_home.setOnClickListener(this);
		rl_order.setOnClickListener(this);
		rl_community.setOnClickListener(this);
		rl_myinformation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 首页
		case R.id.rl_home:
			setTabSelection(0);
			break;
		// 预约
		case R.id.rl_order:
			setTabSelection(1);
			break;
		// 我的订单
		case R.id.rl_community:
			if(ShareUtil.getifUserInfo(MainActivity.this)==1){
				if(MyApplication.hasRemandInfo!=0){
					new getRemandThread().start();
					iv_yuandian.setVisibility(View.GONE);
				}
				setTabSelection(2);
			}else{
				Toast.makeText(MainActivity.this, "您的信息不完整，请点击个人中心完善个人信息!", 0).show();
			}
			
			break;
		// 我的
		case R.id.rl_myinformation:
			String password = ShareUtil.getPasswordContent(MainActivity.this);
			if(!password.equals("")){
				setTabSelection(3);
			}else{
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			
			break;
		default:
			break;
		}

	}

	public static int tabIndex = 0;

	public  void setTabSelection(int index) {
		if (lastClickTime == 0L) {
			lastClickTime = (new Date()).getTime();
		} else if (lastClickTime == -1L
				|| (new Date()).getTime() - lastClickTime < 600) {
			return;
		}
		lastClickTime = (new Date()).getTime();

		tabIndex = index;

		clearSelection();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (index) {
		case 0:
			btn_title_left.setVisibility(View.INVISIBLE);
			btn_title_right.setVisibility(View.INVISIBLE);
			tv_title_content.setText("寻需求");
			iv_home_image.setImageResource(R.drawable.xunxuqiu_click);
			tv_home_text.setTextColor(getResources().getColor(
					R.color.font_orange));
			if (seekfra == null) {
				seekfra = new SeekingNeedsfragment();
				transaction.add(R.id.fl_content, seekfra);
			} else {
				transaction.show(seekfra);
			}
			break;
		case 1:
			btn_title_left.setVisibility(View.VISIBLE);
			btn_title_right.setVisibility(View.INVISIBLE);
			tv_title_content.setText("名医荟");
			// 防止背景图片与文字显示重叠，将背景图片设置成背景颜色
			// purchaseCarFragment.setListener(this);

			iv_order.setImageResource(R.drawable.mingyihui_click);
			tv_order_text.setTextColor(getResources().getColor(
					R.color.font_orange));

			if (appointmentfra == null) {
				appointmentfra = new Appointmentfragment();
				transaction.add(R.id.fl_content, appointmentfra);
			} else {
				transaction.show(appointmentfra);
			}

			// if (purchaseCarFragment == null) {
			// purchaseCarFragment = new PurchaseCarFragment();
			// }
			// transaction.replace(R.id.fl_content, purchaseCarFragment);
			// purchaseCarFragment.setListener(this);
			break;
		case 2:

			btn_title_left.setVisibility(View.INVISIBLE);
			btn_title_right.setVisibility(View.INVISIBLE);
			tv_title_content.setText("我的订单");
			iv_community_image.setImageResource(R.drawable.myorder_click);
			tv_community_text.setTextColor(getResources().getColor(
					R.color.font_orange));
			if (homefra == null) {
				homefra = new MyOrderfragment();
				transaction.add(R.id.fl_content, homefra);
			} else {
				transaction.show(homefra);
			}
			break;
		case 3:

			btn_title_left.setText("");
			btn_title_right.setVisibility(View.GONE);
			tv_title_content.setText("个人中心");
			iv_information_image.setImageResource(R.drawable.wode_click);
			tv_information_text.setTextColor(getResources().getColor(
					R.color.font_orange));
			if (inforfra == null) {
				inforfra = new MyInformationfragment();
				transaction.add(R.id.fl_content, inforfra);
			} else {
				transaction.show(inforfra);
			}
			// if (aboutfra == null) {
			// aboutfra = new AboutFragment();
			// }
			// transaction.replace(R.id.fl_content, aboutfra);

			break;
		default:
			break;
		}
		transaction.commitAllowingStateLoss();
	}

	private  void clearSelection() {

		iv_home_image.setImageResource(R.drawable.xunxuqiu);
		tv_home_text.setTextColor(Color.parseColor("#999999"));

		iv_order.setImageResource(R.drawable.mingyihui);
		tv_order_text.setTextColor(Color.parseColor("#999999"));

		iv_information_image.setImageResource(R.drawable.wode);
		tv_information_text.setTextColor(Color.parseColor("#999999"));
		
		iv_community_image.setImageResource(R.drawable.myorder);
		tv_community_text.setTextColor(Color.parseColor("#999999"));
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (homefra != null) {
			transaction.hide(homefra);
			homefra.onPause();
		}
		if (appointmentfra != null) {
			transaction.hide(appointmentfra);
			appointmentfra.onPause();
		}
		if (inforfra != null) {
			transaction.hide(inforfra);
			inforfra.onPause();
		}
		if (seekfra != null) {
			transaction.hide(seekfra);
			seekfra.onPause();
		}
	};

	private void initImageLoader() {
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
	
	
	@Override
	public void onBackPressed() {
		alertDialog = ViewUtil.showDialog(this, "提示", "确定退出私家医院客户端吗？", "离开",
				"再看看", new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alertDialog.dismiss();
						finish();

					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alertDialog.dismiss();
					}
				});

		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);

		alertDialog.show();
	}
	class HomeThread extends Thread {
		@Override
		public void run() {
			bean = new BaseRequest();
			bean.userId = ShareUtil.getAccountId(MainActivity.this);
			Object obj = NetRequestEngine.homepage(bean);
			Message msg = Message.obtain();
			msg.what = SUCCESS;
			msg.obj = obj;
			handler.sendMessage(msg);

		}
	}
	
	class getRemandThread extends Thread {
		@Override
		public void run() {
			bean = new BaseRequest();
			bean.userId = ShareUtil.getAccountId(MainActivity.this);
			Object obj = NetRequestEngine.removeRemandInfo(bean);
		}

	}

}

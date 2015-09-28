package com.example.sjyy_expert_android.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.user.AuthenticationRequest;
import com.example.sjyy_expert_android.entity.user.RenZheng;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.CircularImage;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AuthenticationActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private final static int SUCCESS = 1;
	private LinearLayout ll_comm_bg_loadingfail,ll_renzheng_zhuti;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private AuthenticationRequest bean;
	private RenZheng base;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options;
	private String doctorId;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_renzheng_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null
						&& msg.obj instanceof RenZheng) {

					base = (RenZheng) msg.obj;
					if (base.resultCode.equals("1000")) {
						showview();
					} else {
						ll_renzheng_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}
				} else {
					ll_renzheng_zhuti.setVisibility(View.GONE);
					ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
					tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
				}

				break;

			default:
				break;
			}
			stopProgressDialog();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_renzhengxinxi);
		initview();
		initdata();
		initImageLoader();

	}

	private void initdata() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		startProgressDialog();
		new dataThread().start();
	}

	private CircularImage iv_renzheng_icon;
	private TextView tv_renzheng_name, tv_renzheng_zhicheng,
			tv_renzheng_address, tv_renzheng_skill, tv_renzheng_beijing;

	private void initview() {
		Intent intent =getIntent();
		doctorId=intent.getStringExtra("doctorId");
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.img_no_net_pic)
				.showImageForEmptyUri(R.drawable.img_no_net_pic)
				.cacheInMemory(true).cacheOnDisc(true).build();
		
		iv_renzheng_icon=(CircularImage) findViewById(R.id.iv_renzheng_icon);
		tv_renzheng_name=(TextView) findViewById(R.id.tv_renzheng_name);
		tv_renzheng_zhicheng=(TextView) findViewById(R.id.tv_renzheng_zhicheng);
		tv_renzheng_address=(TextView) findViewById(R.id.tv_renzheng_address);
		tv_renzheng_skill=(TextView) findViewById(R.id.tv_renzheng_skill);
		tv_renzheng_beijing=(TextView) findViewById(R.id.tv_renzheng_beijing);
		
		
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("认证信息");
		btn_title_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ll_renzheng_zhuti=(LinearLayout) findViewById(R.id.ll_renzheng_zhuti);
		ll_comm_bg_loadingfail = (LinearLayout) findViewById(R.id.ll_comm_bg_loadingfail);
		ll_comm_bg_loadingfail.setVisibility(View.INVISIBLE);
		iv_comm_bg_loadingfail = (ImageView) findViewById(R.id.iv_comm_bg_loadingfail);
		tv_comm_bg_loadingfail = (TextView) findViewById(R.id.tv_comm_bg_loadingfail);
		iv_comm_bg_loadingfail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initdata();
			}
		});
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new AuthenticationRequest();
			bean.doctorId=doctorId;
			bean.userId=ShareUtil.getAccountId(AuthenticationActivity.this);
			Object data = NetRequestEngine.getDoctorCertificationInfo(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}

	private void showview() {
		imageLoader.displayImage(base.doctorCertificationInfo.iconUrl,
				iv_renzheng_icon, options);
		tv_renzheng_name.setText(base.doctorCertificationInfo.name);
		tv_renzheng_zhicheng.setText(base.doctorCertificationInfo.qualification);
		tv_renzheng_address.setText(base.doctorCertificationInfo.hospitalName);
		tv_renzheng_skill.setText(base.doctorCertificationInfo.summary);
		tv_renzheng_beijing.setText(base.doctorCertificationInfo.medicalBackground);
		
	}
}

package com.example.sjyy_expert_android.activity.patient;

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
import com.example.sjyy_expert_android.entity.BaseRequest;
import com.example.sjyy_expert_android.entity.user.PatientUserInfo;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.CircularImage;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
/***
 * 类描述：患者信息
 * @author 海洋
 */
public class PatientUserInfoActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private LinearLayout ll_comm_bg_loadingfail,ll_patient_zhuti;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private final static int SUCCESS = 1;

	private BaseRequest bean;
	private PatientUserInfo base;

	private CircularImage iv_patient_icon;
	private TextView tv_patient_nicheng, tv_patient_name, tv_patient_phone,
			tv_patient_address;
	private LinearLayout ll_usericon, ll_user_nicheng, ll_user_name,
			ll_user_phone, ll_user_address;

	protected ImageLoader imageLoader;
	private DisplayImageOptions options;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_patient_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof PatientUserInfo) {

					base = (PatientUserInfo) msg.obj;
					if (base.resultCode.equals("1000")) {
						showview();

					} else {
						ll_patient_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}
				} else {
					ll_patient_zhuti.setVisibility(View.GONE);
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
		setContentView(R.layout.patient_user_info);

		initview();
		initdata();
		initImageLoader();
	}

	private void initdata() {
		startProgressDialog();
		new dataThread().start();
	}

	private void showview() {

		imageLoader.displayImage(base.iconUrl, iv_patient_icon, options);
		tv_patient_nicheng.setText(base.nickName);
		tv_patient_name.setText(base.name);
		tv_patient_phone.setText(base.tel);
		tv_patient_address.setText(base.address);

	}

	private void initview() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.img_no_net_pic)
				.showImageForEmptyUri(R.drawable.img_no_net_pic)
				.cacheInMemory(true).cacheOnDisc(true).build();
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setText("");
		btn_title_right.setText("");
		tv_title_content.setText("个人信息");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_left.setOnClickListener(this);
		iv_patient_icon = (CircularImage) findViewById(R.id.iv_patient_icon);
		tv_patient_nicheng = (TextView) findViewById(R.id.tv_patient_nicheng);
		tv_patient_name = (TextView) findViewById(R.id.tv_patient_name);
		tv_patient_phone = (TextView) findViewById(R.id.tv_patient_phone);
		tv_patient_address = (TextView) findViewById(R.id.tv_patient_address);
		ll_usericon = (LinearLayout) findViewById(R.id.ll_usericon);
		ll_user_nicheng = (LinearLayout) findViewById(R.id.ll_user_nicheng);
		ll_user_name = (LinearLayout) findViewById(R.id.ll_user_name);
		ll_user_phone = (LinearLayout) findViewById(R.id.ll_user_phone);
		ll_user_address = (LinearLayout) findViewById(R.id.ll_user_address);
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
		ll_patient_zhuti=(LinearLayout) findViewById(R.id.ll_patient_zhuti);
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new BaseRequest();
			bean.userId = ShareUtil.getAccountId(PatientUserInfoActivity.this);
			Object data = NetRequestEngine.getPatient(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {

		// 个人信息
		case R.id.btn_title_left:
			finish();
			break;

		// 个人信息
		case R.id.ll_usericon:

			break;
		// 设置
		case R.id.ll_user_nicheng:
			break;
		// 服务中心
		case R.id.ll_user_name:
			break;
		// 关于私+医院
		case R.id.ll_user_phone:
			break;

		// 关于私+医院
		case R.id.ll_user_address:
			break;

		default:
			break;
		}
	}

}

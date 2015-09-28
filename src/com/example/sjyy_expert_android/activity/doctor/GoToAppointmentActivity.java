package com.example.sjyy_expert_android.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.doctorBean.DoctorRequest;
import com.example.sjyy_expert_android.entity.doctorBean.DoctorRespons;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.CircularImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 类描述：医生详情
 * 
 * @author 海洋
 */
public class GoToAppointmentActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private CircularImage iv_goto_head;
	private ImageView iv_goto_renzheng;
	private TextView tv_goto_name, tv_goto_qualification, tv_goto_appointment,
			tv_goto_renzheng, tv_goto_address;
	private TextView tv_goto_skill,tv_goto_practiceExperience;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options;
	private DoctorRequest bean;
	private DoctorRespons beanrespons;
	private final static int SUCCESS = 1;
	private String doctorId;
	private int currentPage;
//	private ListView lv_my_evaluation;

	private View loadMoreView;
	private TextView loadMoreButton;
//	private goToAdapter gotoadapter;
	private String success;
	private LinearLayout ll_renzheng;
	private LinearLayout ll_comm_bg_loadingfail,ll_goto_zhuti;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_goto_zhuti.setVisibility(View.VISIBLE);
				
				if (msg.obj != null && msg.obj instanceof DoctorRespons) {

					beanrespons = (DoctorRespons) msg.obj;
					if (beanrespons.resultCode.equals("1000")) {
						showview();
					} else {
						ll_goto_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}
				} else {
					ll_goto_zhuti.setVisibility(View.GONE);
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
		setContentView(R.layout.goto_appointment_activity);
		initview();
		initdatas();
		initImageLoader();

	}

	private void initdatas() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		startProgressDialog();
		new GoToThread().start();
	}

	private void initview() {

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.img_no_net_pic)
				.showImageForEmptyUri(R.drawable.img_no_net_pic)
				.cacheInMemory(true).cacheOnDisc(true).build();
		Intent intent = getIntent();
		doctorId = intent.getStringExtra("doctorId");
		currentPage = 1;
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("主治医生");
		ll_renzheng = (LinearLayout) findViewById(R.id.ll_renzheng);
		iv_goto_head = (CircularImage) findViewById(R.id.iv_goto_head);
		iv_goto_renzheng = (ImageView) findViewById(R.id.iv_goto_renzheng);
		tv_goto_name = (TextView) findViewById(R.id.tv_goto_name);
		tv_goto_qualification = (TextView) findViewById(R.id.tv_goto_qualification);
		tv_goto_appointment = (TextView) findViewById(R.id.tv_goto_appointment);
		tv_goto_renzheng = (TextView) findViewById(R.id.tv_goto_renzheng);
		tv_goto_address = (TextView) findViewById(R.id.tv_goto_address);
		tv_goto_skill = (TextView) findViewById(R.id.tv_goto_skill);
		tv_goto_practiceExperience=(TextView) findViewById(R.id.tv_goto_practiceExperience);
//		lv_my_evaluation = (ListView) findViewById(R.id.lv_my_evaluation);
//		loadMoreView = LayoutInflater.from(GoToAppointmentActivity.this)
//				.inflate(R.layout.common_loadmore, null);
//		loadMoreButton = (TextView) loadMoreView
//				.findViewById(R.id.loadMoreButton);
//		loadMoreButton.setOnClickListener(this);
//		lv_my_evaluation.addFooterView(loadMoreView);
		ll_renzheng.setOnClickListener(this);
		ll_comm_bg_loadingfail = (LinearLayout) findViewById(R.id.ll_comm_bg_loadingfail);
		ll_comm_bg_loadingfail.setVisibility(View.INVISIBLE);
		iv_comm_bg_loadingfail = (ImageView) findViewById(R.id.iv_comm_bg_loadingfail);
		tv_comm_bg_loadingfail = (TextView) findViewById(R.id.tv_comm_bg_loadingfail);
		iv_comm_bg_loadingfail.setOnClickListener(this);
		ll_goto_zhuti=(LinearLayout) findViewById(R.id.ll_goto_zhuti);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.iv_comm_bg_loadingfail:
			initdatas();
			break;
		case R.id.ll_renzheng:
//			Intent intent = new Intent();
//			intent.setClass(GoToAppointmentActivity.this,
//					AuthenticationActivity.class);
//			intent.putExtra("doctorId", beanrespons.doctorDetails.serverId);
//			startActivity(intent);
			break;

		case R.id.loadMoreButton:
			loadMoreButton.setText("正在加载中..."); // 设置按钮文字
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					currentPage++;
					initdatas();
				}
			}, 2000);
			break;
		default:
			break;
		}

	}

	class GoToThread extends Thread {
		@Override
		public void run() {
			bean = new DoctorRequest();
			bean.doctorId = doctorId;
			Object data = NetRequestEngine.gotoappointment(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);

		}
	}

	private void showview() {
		tv_goto_name.setText(beanrespons.doctorDetails.doctorName);
		tv_goto_qualification.setText("("
				+ beanrespons.doctorDetails.qualification + ")");
		tv_goto_appointment.setText(beanrespons.doctorDetails.departmentName);
		tv_goto_address.setText(beanrespons.doctorDetails.hospitalName);
		tv_goto_skill.setText(beanrespons.doctorDetails.professionalField,BufferType.NORMAL);
		tv_goto_practiceExperience.setText(beanrespons.doctorDetails.practiceExperience,BufferType.NORMAL);
		imageLoader.displayImage(beanrespons.doctorDetails.iconUrl,
				iv_goto_head, options);
	};

}

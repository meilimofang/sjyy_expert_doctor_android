package com.example.sjyy_expert_android.activity.patient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.activity.order.AppointmentOrderDetailActivity;
import com.example.sjyy_expert_android.entity.BaseRequest;
import com.example.sjyy_expert_android.entity.patient.PatientRespons;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.CircularImage;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.example.sjyy_expert_android.util.XListView;
import com.example.sjyy_expert_android.util.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 类描述：医院我的预约列表
 * 
 * @author 海洋
 */
public class MyAppointmentActivity extends BaseActivity implements
		IXListViewListener {

	private XListView lv_my_appointment;
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private final static int SUCCESS = 1;
	private BaseRequest bean;
	private PatientRespons base;
	private int currentPage;
	private onlineAdapter adapter;
	private boolean zhuangtai = true;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options;
	private LinearLayout ll_comm_bg_loadingfail, ll_appoint_zhuti;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_appoint_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof PatientRespons) {

					base = (PatientRespons) msg.obj;
					if (base.resultCode.equals("1000")) {

						initadapter();

					} else {
						ll_appoint_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}
				} else {
					ll_appoint_zhuti.setVisibility(View.GONE);
					ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
					tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
				}

				break;
			default:
				break;
			}
			stopProgressDialog();
			onLoad();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_appointment_activity);
		currentPage = 1;
		initview();
		initdata();
		initImageLoader();

	}

	private void initdata() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		startProgressDialog();
		new dataThread().start();
	}

	private void initadapter() {
		if (!base.totalCount.equals("0")) {
			if (zhuangtai) {
				adapter = new onlineAdapter();
				lv_my_appointment.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		}

	}

	private void initview() {
		ll_appoint_zhuti = (LinearLayout) findViewById(R.id.ll_appoint_zhuti);
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
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.img_no_net_pic)
				.showImageForEmptyUri(R.drawable.img_no_net_pic)
				.cacheInMemory(true).cacheOnDisc(true).build();
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_right.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		tv_title_content.setText("我的预约");
		btn_title_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		lv_my_appointment = (XListView) findViewById(R.id.lv_my_appointment);
		lv_my_appointment.setPullLoadEnable(false);
		lv_my_appointment.setXListViewListener(this);
		lv_my_appointment.setPullRefreshEnable(true);
	}

	class onlineAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return base.orderList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		class ViewHolder {
			TextView tv_patient_name, tv_patient_suggest, tv_patient_num,
					tv_patient_problem, tv_patient_orderstate, tv_patient_time
					;
			Button bt_patient_details;
			CircularImage iv_my_appointment_icon;
			ImageView iv_my_appointment_zuo,iv_my_appointment_shu,iv_my_appointment_hui;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(MyAppointmentActivity.this)
						.inflate(R.layout.my_appointmen_item, null);
				holder = new ViewHolder();
				holder.tv_patient_name = (TextView) convertView
						.findViewById(R.id.tv_patient_name);
				holder.tv_patient_suggest = (TextView) convertView
						.findViewById(R.id.tv_patient_suggest);
//				holder.tv_patient_num = (TextView) convertView
//						.findViewById(R.id.tv_patient_num);
				holder.tv_patient_problem = (TextView) convertView
						.findViewById(R.id.tv_patient_problem);
				holder.tv_patient_orderstate = (TextView) convertView
						.findViewById(R.id.tv_patient_orderstate);
				holder.tv_patient_time = (TextView) convertView
						.findViewById(R.id.tv_patient_time);
				holder.bt_patient_details = (Button) convertView
						.findViewById(R.id.bt_patient_details);
//				holder.iv_my_appointment_icon = (CircularImage) convertView
//						.findViewById(R.id.iv_my_appointment_icon);
				holder.iv_my_appointment_zuo=(ImageView) convertView.findViewById(R.id.iv_my_appointment_zuo);
				holder.iv_my_appointment_shu=(ImageView) convertView.findViewById(R.id.iv_my_appointment_shu);
				holder.iv_my_appointment_hui=(ImageView) convertView.findViewById(R.id.iv_my_appointment_hui);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_patient_name
					.setText(base.orderList.get(position).doctorName);
			holder.tv_patient_suggest.setText("("
					+ base.orderList.get(position).qualificationName + ")"
					+ base.orderList.get(position).departmentName);
			holder.tv_patient_num
					.setText(base.orderList.get(position).orderNumber);
			holder.tv_patient_problem.setText(base.orderList
					.get(position).bespeakRequire);
			holder.tv_patient_orderstate.setText(base.orderList
					.get(position).orderStatusString);
			holder.tv_patient_time
					.setText(base.orderList.get(position).bespeakTimeString);
			imageLoader.displayImage(
					base.orderList.get(position).iconUrl,
					holder.iv_my_appointment_icon, options);
			if(base.orderList.get(position).serviceTypeId==1){
				holder.iv_my_appointment_zuo.setVisibility(View.VISIBLE);
			}else if(base.orderList.get(position).serviceTypeId==2){
				holder.iv_my_appointment_hui.setVisibility(View.VISIBLE);
			}else if(base.orderList.get(position).serviceTypeId==3){
				holder.iv_my_appointment_shu.setVisibility(View.VISIBLE);
			}
			// 查看详情
			holder.bt_patient_details.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MyAppointmentActivity.this,
							AppointmentOrderDetailActivity.class);
					intent.putExtra("orderId",
							base.orderList.get(position).orderId + "");
					startActivity(intent);

				}
			});

			return convertView;
		}

	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new BaseRequest();
			bean.userId = ShareUtil.getAccountId(MyAppointmentActivity.this);
			Object data = NetRequestEngine.getPatientOrderList(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		initdata();
	}

	@Override
	public void onRefresh() {
		initdata();

	}

	@Override
	public void onLoadMore() {

	}

	private void onLoad() {

		lv_my_appointment.stopRefresh();
		lv_my_appointment.stopLoadMore();
		lv_my_appointment.setRefreshTime("刚刚");
	}
}

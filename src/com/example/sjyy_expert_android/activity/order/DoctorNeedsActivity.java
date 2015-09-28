package com.example.sjyy_expert_android.activity.order;

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
import android.widget.TextView.BufferType;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRequest;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.orderBean.ChangeOrderDetail;
import com.example.sjyy_expert_android.entity.orderBean.MyVisit;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.CollapsibleTextView;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.example.sjyy_expert_android.util.XListView;
import com.example.sjyy_expert_android.util.XListView.IXListViewListener;


/***
 * 类描述：医院我的需求
 * 
 * @author 海洋
 */
public class DoctorNeedsActivity extends BaseActivity implements
		IXListViewListener {

	private XListView lv_doctor_needs;
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private final static int SUCCESS = 1;
	private final static int SUCCESSTWO = 2;
	private BaseRequest bean;
	private MyVisit base;
	private MyVisit basetwo;
	private int jiezhen = 1;
	private int jiuzhen = 2;
	private int jujue = 101;
	private int orderStatus;
	private int currentPage;
	private ChangeOrderDetail request;
	private BaseRespons respons;
	private onlineAdapter adapter;
	private int orderId;
	private boolean zhuangtai = true;
	private LinearLayout ll_comm_bg_loadingfail, ll_mycall_zhuti;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_mycall_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof MyVisit) {

					base = (MyVisit) msg.obj;
					if (base.resultCode.equals("1000")) {
						initadapter();

					} else {
						ll_mycall_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}
				} else {
					ll_mycall_zhuti.setVisibility(View.GONE);
					ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
					tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
				}

				break;

			case SUCCESSTWO:

				if (msg.obj != null && msg.obj instanceof BaseRespons) {

					respons = (BaseRespons) msg.obj;
					if (respons.resultCode.equals("1000")) {
						initdata();

					} else {
						// goNewActivity(ErrorActivity.class);
					}
				} else {
					// goNewActivity(ErrorActivity.class);
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
		setContentView(R.layout.doctor_needs_activity);
		currentPage = 1;
		initview();
		initdata();

	}

	private void initdata() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		startProgressDialog();
		new dataThread().start();
	}

	private void initadapter() {
		if (base.totalCount != 0) {
			if (zhuangtai) {
				adapter = new onlineAdapter();
				lv_doctor_needs.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			} else {
				adapter.notifyDataSetChanged();
			}
		}

	}

	private void initview() {
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
		ll_mycall_zhuti = (LinearLayout) findViewById(R.id.ll_mycall_zhuti);
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_right.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		tv_title_content.setText("我的需求");
		btn_title_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		lv_doctor_needs = (XListView) findViewById(R.id.lv_doctor_needs);
		lv_doctor_needs.setPullLoadEnable(false);
		lv_doctor_needs.setXListViewListener(this);
		lv_doctor_needs.setPullRefreshEnable(true);
	}

	class onlineAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return base.demandList.size();
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
			TextView tv_doctor_order_name, tv_doctor_order_time,
					tv_doctor_order_budgetprice, tv_doctor_order_state,
					tv_doctor_order_yuyue, tv_doctor_order_problem;
			Button bt_doctor_order_details, bt_doctor_order_refuse;
			CollapsibleTextView tv_doctor_order_content;
			ImageView iv_doctor_needs_zuo, iv_doctor_needs_hui,
					iv_doctor_needs_shu;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(DoctorNeedsActivity.this)
						.inflate(R.layout.doctor_needs_item, null);
				holder = new ViewHolder();
//				holder.tv_doctor_order_name = (TextView) convertView
//						.findViewById(R.id.tv_doctor_order_name);
				holder.tv_doctor_order_time = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_time);
				holder.tv_doctor_order_problem = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_problem);
				holder.tv_doctor_order_state = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_state);
				holder.bt_doctor_order_details = (Button) convertView
						.findViewById(R.id.bt_doctor_order_details);
				holder.bt_doctor_order_refuse = (Button) convertView
						.findViewById(R.id.bt_doctor_order_refuse);
				holder.tv_doctor_order_yuyue = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_yuyue);
				holder.tv_doctor_order_content = (CollapsibleTextView) convertView
						.findViewById(R.id.tv_doctor_order_content);
				holder.tv_doctor_order_budgetprice = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_budgetprice);
				holder.iv_doctor_needs_hui = (ImageView) convertView
						.findViewById(R.id.iv_doctor_needs_hui);
				holder.iv_doctor_needs_zuo = (ImageView) convertView
						.findViewById(R.id.iv_doctor_needs_zuo);
				holder.iv_doctor_needs_shu = (ImageView) convertView
						.findViewById(R.id.iv_doctor_needs_shu);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_doctor_order_name
					.setText(base.demandList.get(position).demanderName);
			holder.tv_doctor_order_time
					.setText(base.demandList.get(position).createTimeString);
			holder.tv_doctor_order_problem.setText(base.demandList
					.get(position).demandTitle);
			holder.tv_doctor_order_yuyue
					.setText(base.demandList.get(position).demandTimeString);
			holder.tv_doctor_order_state
					.setText(base.demandList.get(position).demandStatusStr);
			holder.tv_doctor_order_content
					.setDesc(base.demandList.get(position).demandDesc,
							BufferType.NORMAL);
			holder.tv_doctor_order_budgetprice.setText(base.demandList
					.get(position).demandPrice + "");
			if (base.demandList.get(position).demandStatus != 102
					&& base.demandList.get(position).demandStatus != 6
					&& base.demandList.get(position).demandStatus != 3) {
				holder.bt_doctor_order_refuse.setVisibility(View.VISIBLE);
			} else {
				holder.bt_doctor_order_refuse.setVisibility(View.GONE);
			}
			
			if(base.demandList.get(position).serviceTypeId==1){
				holder.iv_doctor_needs_zuo.setVisibility(View.VISIBLE);
			}else if(base.demandList.get(position).serviceTypeId==2){
				holder.iv_doctor_needs_hui.setVisibility(View.VISIBLE);
			}else if(base.demandList.get(position).serviceTypeId==3){
				holder.iv_doctor_needs_shu.setVisibility(View.VISIBLE);
			}
			// 查看详情
			holder.bt_doctor_order_details
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(DoctorNeedsActivity.this,
									OrderDetailActivity.class);
							intent.putExtra("orderId",
									base.demandList.get(position).demandId + "");
							startActivity(intent);

						}
					});

			// 拒绝接诊
			holder.bt_doctor_order_refuse
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(DoctorNeedsActivity.this,
									RefuseActivity.class);
							intent.putExtra("orderId",
									base.demandList.get(position).demandId + "");
							intent.putExtra("orderStatus", 3);
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
			bean.userId = ShareUtil.getAccountId(DoctorNeedsActivity.this);
			Object data = NetRequestEngine.getHospitalDemandList(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}

	class stateThread extends Thread {
		@Override
		public void run() {
			zhuangtai = false;
			request = new ChangeOrderDetail();
			request.userId = ShareUtil.getAccountId(DoctorNeedsActivity.this);
			// request.orderStatus = orderStatus;
			// request.orderId = orderId;
			Object data = NetRequestEngine.getOrderDetail(request);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTWO;
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
		currentPage = 1;
		initdata();

	}

	@Override
	public void onLoadMore() {

	}

	private void onLoad() {

		lv_doctor_needs.stopRefresh();
		lv_doctor_needs.stopLoadMore();
		lv_doctor_needs.setRefreshTime("刚刚");
	}

}

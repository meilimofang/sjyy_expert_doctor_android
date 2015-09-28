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
 * 类描述：医生我的需求
 * @author 海洋
 */
public class MyCallActivity extends BaseActivity  implements IXListViewListener{

	private XListView lv_mycall;
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private final static int SUCCESS = 1;
	private final static int SUCCESSTWO = 2;
	private BaseRequest bean;
	private MyVisit base;
	private MyVisit basetwo;
	private int jiezhen = 5;
	private int jiuzhen = 6;
	private int jujue = 101;
	private int orderStatus;
	private int currentPage;
	private ChangeOrderDetail request;
	private BaseRespons respons;
	private onlineAdapter adapter;
	private int orderId;
	private boolean zhuangtai = true;
	private LinearLayout ll_comm_bg_loadingfail,ll_mycall_zhuti;
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
//						goNewActivity(ErrorActivity.class);
					}
				} else {
//					goNewActivity(ErrorActivity.class);
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
		setContentView(R.layout.my_call_activity);
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
		if(base.totalCount!=0){
			if (zhuangtai) {
				adapter = new onlineAdapter();
				lv_mycall.setAdapter(adapter);
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
		ll_mycall_zhuti=(LinearLayout) findViewById(R.id.ll_mycall_zhuti);
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

		lv_mycall = (XListView) findViewById(R.id.lv_mycall);
		lv_mycall.setPullLoadEnable(false);
		lv_mycall.setXListViewListener(this);
		lv_mycall.setPullRefreshEnable(true);
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
			TextView tv_order_name, tv_order_time,tv_order_budgetprice,tv_order_state,tv_order_yuyue,tv_order_problem;
			Button bt_order_details, bt_order_cance,bt_order_jiezhen;
			CollapsibleTextView tv_order_content;
			ImageView iv_my_call_zuo,iv_my_call_hui,iv_my_call_shu;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(MyCallActivity.this).inflate(
						R.layout.my_call_item, null);
				holder = new ViewHolder();
				holder.tv_order_name = (TextView) convertView
						.findViewById(R.id.tv_order_name);
				holder.tv_order_time = (TextView) convertView
						.findViewById(R.id.tv_order_time);
				holder.tv_order_problem = (TextView) convertView
						.findViewById(R.id.tv_order_problem);
				holder.tv_order_state = (TextView) convertView
						.findViewById(R.id.tv_order_state);
				holder.bt_order_details = (Button) convertView
						.findViewById(R.id.bt_order_details);
				holder.bt_order_cance = (Button) convertView
						.findViewById(R.id.bt_order_cance);
				holder.bt_order_jiezhen = (Button) convertView
						.findViewById(R.id.bt_order_jiezhen);
				holder.tv_order_yuyue = (TextView) convertView
						.findViewById(R.id.tv_order_yuyue);
				holder.tv_order_content = (CollapsibleTextView) convertView
						.findViewById(R.id.tv_order_content);
				holder.tv_order_budgetprice = (TextView) convertView
						.findViewById(R.id.tv_order_budgetprice);
				holder.iv_my_call_hui=(ImageView) convertView.findViewById(R.id.iv_my_call_hui);
				holder.iv_my_call_zuo=(ImageView) convertView.findViewById(R.id.iv_my_call_zuo);
				holder.iv_my_call_shu=(ImageView) convertView.findViewById(R.id.iv_my_call_shu);
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_order_name
					.setText(base.demandList.get(position).demanderName);
			holder.tv_order_time
					.setText(base.demandList.get(position).createTimeString);
			holder.tv_order_problem
					.setText(base.demandList.get(position).demandTitle);
			holder.tv_order_state.setText(base.demandList.get(position).demandStatusStr);
			holder.tv_order_yuyue.setText(base.demandList.get(position).demandTimeString);
			holder.tv_order_content .setDesc(base.demandList.get(position).demandDesc,BufferType.NORMAL);
			holder.tv_order_budgetprice.setText(base.demandList.get(position).demandPrice+"");
			if(base.demandList.get(position).demandStatus==2){
				holder.bt_order_jiezhen.setText("接诊");
			}else if(base.demandList.get(position).demandStatus==5){
				holder.bt_order_jiezhen.setText("就诊");
			}else if(base.demandList.get(position).demandStatus==6){
				holder.bt_order_jiezhen.setVisibility(View.GONE);
			}
			
			if(base.demandList.get(position).demandStatus==2){
				holder.bt_order_cance.setVisibility(View.VISIBLE);
			}else{
				holder.bt_order_cance.setVisibility(View.GONE);
			}
			if(base.demandList.get(position).demandStatus==4){
				holder.bt_order_jiezhen.setVisibility(View.GONE);
			}
			
			if(base.demandList.get(position).serviceTypeId==1){
				holder.iv_my_call_zuo.setVisibility(View.VISIBLE);
			}else if(base.demandList.get(position).serviceTypeId==2){
				holder.iv_my_call_hui.setVisibility(View.VISIBLE);
			}else if(base.demandList.get(position).serviceTypeId==3){
				holder.iv_my_call_shu.setVisibility(View.VISIBLE);
			}
			// 查看详情
			holder.bt_order_details.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent=new Intent();
					intent.setClass(MyCallActivity.this, OrderDetailActivity.class);
					intent.putExtra("orderId", base.demandList.get(position).demandId+"");
					startActivity(intent);
				}
			});
			//拒绝接诊
			holder.bt_order_cance.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent();
					intent.setClass(MyCallActivity.this, RefuseActivity.class);
					intent.putExtra("orderId", base.demandList.get(position).demandId+"");
					intent.putExtra("orderStatus",4);
					startActivity(intent);
				}
			});
			
			holder.bt_order_jiezhen.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(base.demandList.get(position).demandStatus==2){
						startProgressDialog();
						orderId=base.demandList.get(position).demandId;
						orderStatus=jiezhen;
						new jiezhenThread().start();
					}else if(base.demandList.get(position).demandStatus==5){
						startProgressDialog();
						orderId=base.demandList.get(position).demandId;
						orderStatus=jiuzhen;
						new jiezhenThread().start();
					}
					
					
				}
			});
			return convertView;
		}
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new BaseRequest();
			bean.userId = ShareUtil.getAccountId(MyCallActivity.this);
			Object data = NetRequestEngine.getDoctorDemandList(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}

	class stateThread extends Thread {
		@Override
		public void run() {
			request = new ChangeOrderDetail();
			request.userId = ShareUtil.getAccountId(MyCallActivity.this);
			request.demandStatus = orderStatus;
			request.demandId = orderId;
			Object data = NetRequestEngine.cancelDemond(request);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTWO;
			handler.sendMessage(message);
		}
	}
	//接诊
	class jiezhenThread extends Thread {
		@Override
		public void run() {
			request = new ChangeOrderDetail();
			request.userId = ShareUtil.getAccountId(MyCallActivity.this);
			request.demandStatus = orderStatus;
			request.demandId = orderId;
			Object data = NetRequestEngine.admissionDemand(request);
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
		currentPage=1;
		initdata();
	
		
	}

	@Override
	public void onLoadMore() {
		
		
	}
	private void onLoad() {

		lv_mycall.stopRefresh();
		lv_mycall.stopLoadMore();
		lv_mycall.setRefreshTime("刚刚");
	}
	
}

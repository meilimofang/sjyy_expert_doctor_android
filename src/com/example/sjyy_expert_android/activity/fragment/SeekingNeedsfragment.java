package com.example.sjyy_expert_android.activity.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.example.sjyy_expert_android.MyApplication;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.loadmore.LoadMoreXunBean;
import com.example.sjyy_expert_android.entity.orderBean.MyVisit;
import com.example.sjyy_expert_android.entity.orderBean.OrderDemand;
import com.example.sjyy_expert_android.entity.seekneed.SeekNeed;
import com.example.sjyy_expert_android.entity.seekneed.SeekNeedBaseRespons;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.CollapsibleTextView;
import com.example.sjyy_expert_android.util.CustomProgressDialog;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.example.sjyy_expert_android.util.ViewUtil;
import com.example.sjyy_expert_android.util.XListView;
import com.example.sjyy_expert_android.util.XListView.IXListViewListener;

/***
 * 类描述：寻需求
 * 
 * @author 海洋
 */
public class SeekingNeedsfragment extends Fragment implements OnClickListener,
		IXListViewListener {

	private MyApplication application;
	private XListView lv_seeking;
	private CustomProgressDialog progressDialog = null;
	private final static int SUCCESS = 1;
	private final static int SUCCESSTWO = 2;
	private final static int SUCCESSTHREE = 3;
	private LoadMoreXunBean loadBean;
	private SeekNeedBaseRespons base;
	private SeekNeedBaseRespons loadMoreBase;
	private MyVisit basetwo;
	private int orderStatus;
	private Integer currentPage;
	private OrderDemand request;
	private BaseRespons respons;
	private onlineAdapter adapter;
	private int orderId;
	private boolean zhuangtai = true;
	private Integer demandId;
	private LinearLayout ll_comm_bg_loadingfail, ll_mycall_zhuti,
			ll_common_nodata;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private AlertDialog alertDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_common_nodata.setVisibility(View.GONE);
				ll_mycall_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof SeekNeedBaseRespons) {

					base = (SeekNeedBaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						if (base.totalCount != 0) {
							initadapter();
						} else {
							ll_mycall_zhuti.setVisibility(View.GONE);
							ll_comm_bg_loadingfail.setVisibility(View.GONE);
							ll_common_nodata.setVisibility(View.VISIBLE);
						}

					} else {
						ll_mycall_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						ll_common_nodata.setVisibility(View.GONE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}
				} else {
					ll_mycall_zhuti.setVisibility(View.GONE);
					ll_common_nodata.setVisibility(View.GONE);
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
						// goNewActivity(null);
						Toast.makeText(getActivity(), respons.resultInfo, 1)
								.show();
					}
				} else {
					// goNewActivity(null);
				}

				break;
			case SUCCESSTHREE:

				if (msg.obj != null && msg.obj instanceof SeekNeedBaseRespons) {
					loadMoreBase=(SeekNeedBaseRespons) msg.obj;
					if (loadMoreBase.totalCount==base.demandList.size()) {
						Toast.makeText(getActivity(), "无更多数据!", 0)
						.show();
					} else {
						for(SeekNeed sn:loadMoreBase.demandList){
							base.demandList.add(sn);
						}
						loadMoreinitdata();
					}
				} else {
					// goNewActivity(null);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.seeking_needs_fragment,
				container, false);
		currentPage = 1;
		ll_comm_bg_loadingfail = (LinearLayout) view
				.findViewById(R.id.ll_comm_bg_loadingfail);
		ll_comm_bg_loadingfail.setVisibility(View.INVISIBLE);
		iv_comm_bg_loadingfail = (ImageView) view
				.findViewById(R.id.iv_comm_bg_loadingfail);
		tv_comm_bg_loadingfail = (TextView) view
				.findViewById(R.id.tv_comm_bg_loadingfail);
		ll_common_nodata = (LinearLayout) view
				.findViewById(R.id.ll_common_nodata);

		iv_comm_bg_loadingfail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				initdata();
			}
		});
		ll_mycall_zhuti = (LinearLayout) view
				.findViewById(R.id.ll_mycall_zhuti);
		lv_seeking = (XListView) view.findViewById(R.id.lv_seeking);
		lv_seeking.setPullLoadEnable(true);
		lv_seeking.setXListViewListener(this);
		lv_seeking.setPullRefreshEnable(true);
		initdata();

		return view;
	}

	private void initdata() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		ll_common_nodata.setVisibility(View.GONE);
		startProgressDialog();
		new dataThread().start();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onRefresh() {
		currentPage = 1;
		initdata();
	}

	

	private void initadapter() {
		if (base.totalCount != 0) {
			if (zhuangtai) {
				adapter = new onlineAdapter();
				lv_seeking.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			} else {
				adapter.notifyDataSetChanged();
			}
		}

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
			TextView tv_seek_need_name, tv_seek_need_time,
					tv_seek_need_problem, tv_seek_need_plantime,
					tv_seek_need_budgetprice;
			Button bt_seek_need_yuyue;
			CollapsibleTextView tv_seek_need_content;
			ImageView iv_seek_need_zuo, iv_seek_need_hui, iv_seek_need_shu;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.seek_need_item, null);
				holder = new ViewHolder();
				holder.tv_seek_need_name = (TextView) convertView
						.findViewById(R.id.tv_seek_need_name);
				holder.tv_seek_need_time = (TextView) convertView
						.findViewById(R.id.tv_seek_need_time);
				holder.tv_seek_need_problem = (TextView) convertView
						.findViewById(R.id.tv_seek_need_problem);
				holder.tv_seek_need_content = (CollapsibleTextView) convertView
						.findViewById(R.id.tv_seek_need_content);
				holder.tv_seek_need_plantime = (TextView) convertView
						.findViewById(R.id.tv_seek_need_plantime);
				holder.tv_seek_need_budgetprice = (TextView) convertView
						.findViewById(R.id.tv_seek_need_budgetprice);
				holder.bt_seek_need_yuyue = (Button) convertView
						.findViewById(R.id.bt_seek_need_yuyue);

				holder.iv_seek_need_hui = (ImageView) convertView
						.findViewById(R.id.iv_seek_need_hui);
				holder.iv_seek_need_zuo = (ImageView) convertView
						.findViewById(R.id.iv_seek_need_zuo);
				holder.iv_seek_need_shu = (ImageView) convertView
						.findViewById(R.id.iv_seek_need_shu);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_seek_need_name
					.setText(base.demandList.get(position).demanderName);
			holder.tv_seek_need_time
					.setText(base.demandList.get(position).createTimeString);
			holder.tv_seek_need_problem
					.setText(base.demandList.get(position).demandTitle);
			holder.tv_seek_need_content
					.setDesc(base.demandList.get(position).demandDesc,
							BufferType.NORMAL);
			holder.tv_seek_need_plantime
					.setText(base.demandList.get(position).demandTimeString);
			holder.tv_seek_need_budgetprice.setText(base.demandList
					.get(position).demandPrice + "元");

			if (base.demandList.get(position).demandStatus == 1
					|| base.demandList.get(position).demandStatus == 4) {
				holder.bt_seek_need_yuyue.setText("预约");
				holder.bt_seek_need_yuyue.setEnabled(true);
				holder.bt_seek_need_yuyue
						.setBackgroundResource(R.drawable.button_background);
			} else if (base.demandList.get(position).demandStatus == 2) {
				holder.bt_seek_need_yuyue.setText("已预约");
				holder.bt_seek_need_yuyue.setEnabled(false);
				holder.bt_seek_need_yuyue
						.setBackgroundResource(R.drawable.button_background_selector);
			}

			if (base.demandList.get(position).serviceTypeId == 1) {
				holder.iv_seek_need_zuo.setVisibility(View.VISIBLE);
			} else {
				holder.iv_seek_need_zuo.setVisibility(View.GONE);
			}

			if (base.demandList.get(position).serviceTypeId == 2) {
				holder.iv_seek_need_hui.setVisibility(View.VISIBLE);
			} else {
				holder.iv_seek_need_hui.setVisibility(View.GONE);
			}

			if (base.demandList.get(position).serviceTypeId == 3) {
				holder.iv_seek_need_shu.setVisibility(View.VISIBLE);
			} else {
				holder.iv_seek_need_shu.setVisibility(View.GONE);
			}

			holder.bt_seek_need_yuyue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog = ViewUtil.showDialog(getActivity(), "提示",
							"确定预约吗?", "确认", "取消", new OnClickListener() {

								@Override
								public void onClick(View v) {
									demandId = base.demandList.get(position).demandId;
									new stateThread().start();
									startProgressDialog();
									alertDialog.dismiss();
								}
							}, new OnClickListener() {

								@Override
								public void onClick(View v) {
									alertDialog.dismiss();
								}
							});
					alertDialog.setCancelable(false);
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.show();

				}
			});
			return convertView;
		}

	}

	class dataThread extends Thread {

		@Override
		public void run() {
			loadBean = new LoadMoreXunBean();
			loadBean.userId = ShareUtil.getAccountId(getActivity());
			loadBean.currentPage=1;
			Object data = NetRequestEngine.getDemandList(loadBean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}

	class stateThread extends Thread {
		@Override
		public void run() {
			request = new OrderDemand();
			request.userId = ShareUtil.getAccountId(getActivity());
			request.demandId = demandId;
			Object data = NetRequestEngine.orderDemand(request);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTWO;
			handler.sendMessage(message);
		}
	}

	private void onLoad() {

		lv_seeking.stopRefresh();
		lv_seeking.stopLoadMore();
		lv_seeking.setRefreshTime("刚刚");
	}

	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(getActivity());

		}

		progressDialog.show();
	}

	public void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		initdata();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			initdata();
		}
	}
	
	@Override
	public void onLoadMore() {
		startProgressDialog();
		currentPage++;
		new loadMoredataThread().start();
	}
	class loadMoredataThread extends Thread {

		@Override
		public void run() {
			loadBean = new LoadMoreXunBean();
			loadBean.userId = ShareUtil.getAccountId(getActivity());
			loadBean.currentPage=currentPage;
			Object data = NetRequestEngine.getDemandList(loadBean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTHREE;
			handler.sendMessage(message);
		}
	}
	
	private void loadMoreinitdata() {
		adapter.notifyDataSetChanged();
		onLoad();
	}
}

package com.example.sjyy_expert_android.activity.choose;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.activity.user.PerfectDoctorInfoActivity;
import com.example.sjyy_expert_android.entity.choosebean.ChooseDepartmentBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseServiceProductsBean;
import com.example.sjyy_expert_android.entity.registered.Disease;



/***
 * 类描述：选择疾病
 * 
 * @author 海洋
 */
public class ChooseServiceItemsActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private int pos;
	private String partentname;
	private int keshiID;
	private List<Integer> beiyongt;
	private LinearLayout ll_disease_item, ll_disease_show_item;
	private boolean state = true;
	private List<String> item;
	private List<Integer> itemID;
	private int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_service_item_activity);
//		initview();
	}

//	private void initview() {
//		beiyongt = new ArrayList<Integer>();
//		item = new ArrayList<String>();
//		itemID = new ArrayList<Integer>();
//		ll_disease_item = (LinearLayout) findViewById(R.id.ll_disease_item);
//		ll_disease_show_item = (LinearLayout) findViewById(R.id.ll_disease_show_item);
//		btn_title_left = (Button) findViewById(R.id.btn_title_left);
//		btn_title_right = (Button) findViewById(R.id.btn_title_right);
//		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
//		for (Disease dis : application.regis.departmentList
//				.get(ChooseDepartmentBean.position).diseaseList) {
//
//			ll_disease_item.addView(getView(dis.disease, dis.diseaseId, i++));
//		}
//		
//		if (ChooseServiceProductsBean.ChooseServiceProductslist != null) {
//			for (String str : ChooseServiceProductsBean.ChooseServiceProductslist) {
//				ll_disease_show_item.addView(getViewItem(str));
//			}
//		}
//		btn_title_left.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//		btn_title_left.setText("");
//		btn_title_left.setBackgroundResource(R.drawable.back);
//		btn_title_right.setText("提交");
//		tv_title_content.setText("选择疾病");
//		btn_title_right.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ChooseServiceProductsBean.ifIntent = true;
//				ChooseServiceProductsBean.ChooseServiceProductslist = item;
//				ChooseServiceProductsBean.ChooseServiceIdlist = itemID;
//				Intent intent = new Intent();
//				intent.setClass(ChooseServiceItemsActivity.this,
//						PerfectDoctorInfoActivity.class);
//				startActivity(intent);
//			}
//		});
//
//	}
//
//	private View getView(final String str, final Integer id, final int i) {
//		View view = getLayoutInflater().inflate(R.layout.disease_item, null);
//		TextView tv_disease_item = (TextView) view
//				.findViewById(R.id.tv_disease_item);
//
//		tv_disease_item.setText(str);
//		tv_disease_item.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (ChooseServiceProductsBean.ChooseServiceProductslist != null) {
//					item=ChooseServiceProductsBean.ChooseServiceProductslist;
//					itemID=ChooseServiceProductsBean.ChooseServiceIdlist;
//					for(Disease dis: application.regis.departmentList
//							.get(ChooseDepartmentBean.position).diseaseList){
//						for(Integer ter: ChooseServiceProductsBean.ChooseServiceIdlist){
//							if(dis.diseaseId==ter){
//								dis.stateOn=false;
//							}
//						}
//						
//					}
//				}
//					if (application.regis.departmentList
//							.get(ChooseDepartmentBean.position).diseaseList
//							.get(i).stateOn) {
//						item.add(str);
//						itemID.add(id);
//						application.regis.departmentList
//								.get(ChooseDepartmentBean.position).diseaseList
//								.get(i).stateOn = false;
//						ll_disease_show_item.addView(getViewItem(str));
//						
//					} else {
//						item.remove(str);
//						itemID.remove(id);
//						application.regis.departmentList
//								.get(ChooseDepartmentBean.position).diseaseList
//								.get(i).stateOn = true;
//						ll_disease_show_item.removeAllViews();
//						for (String str : item) {
//							ll_disease_show_item.addView(getViewItem(str));
//						}
//					}
//				
//			}
//		});
//		return view;
//	}
//
//	private View getViewItem(final String strs) {
//		View view = getLayoutInflater().inflate(R.layout.disease_show_item,
//				null);
//		TextView tv_disease_show_item = (TextView) view
//				.findViewById(R.id.tv_disease_show_item);
//		tv_disease_show_item.setText(strs);
//		return view;
//	}
}

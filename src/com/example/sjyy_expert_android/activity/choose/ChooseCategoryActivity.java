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
import com.example.sjyy_expert_android.entity.choosebean.ChooseServiceType;
import com.example.sjyy_expert_android.entity.choosebean.ChooseServiceTypeBean;


public class ChooseCategoryActivity extends BaseActivity{

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private List<String> item;
	private List<Integer> itemID;
	private LinearLayout ll_category_item,ll_category_showitem;
	private int i = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_category_activity);
		initdata();
	}
	
	
	private void initdata() {
		item=new ArrayList<String>();
		itemID=new ArrayList<Integer>();
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_right.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("提交");
		tv_title_content.setText("服务类别");
		ll_category_item=(LinearLayout) findViewById(R.id.ll_category_item);
		ll_category_showitem=(LinearLayout) findViewById(R.id.ll_category_showitem);
		for( ChooseServiceType type :application.regis.serviceTypeList){
			ll_category_item.addView(getView(type.serviceTypeName, type.serviceTypeId, i++));
		}
		if (ChooseServiceTypeBean.ChooseServiceName != null) {
			for (String str : ChooseServiceTypeBean.ChooseServiceName) {
				ll_category_showitem.addView(getViewItem(str));
			}
		}
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_title_right:
			ChooseServiceTypeBean.ifIntent = true;
			ChooseServiceTypeBean.ChooseServiceName = item;
			ChooseServiceTypeBean.ChooseServiceNameId = itemID;
			Intent intent = new Intent();
			intent.setClass(ChooseCategoryActivity.this,
					PerfectDoctorInfoActivity.class);
			startActivity(intent);
			
			break;
		default:
			break;
		}
	}

	private View getView(final String str, final Integer id, final int i) {
		View view = getLayoutInflater().inflate(R.layout.disease_item, null);
		TextView tv_disease_item = (TextView) view
				.findViewById(R.id.tv_disease_item);
		tv_disease_item.setText(str);
		tv_disease_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				if (ChooseServiceTypeBean.ChooseServiceName != null) {
					item=ChooseServiceTypeBean.ChooseServiceName;
					itemID=ChooseServiceTypeBean.ChooseServiceNameId;
					for(ChooseServiceType dis: application.regis.serviceTypeList){
						for(Integer ter: ChooseServiceTypeBean.ChooseServiceNameId){
							if(dis.serviceTypeId==ter){
								dis.serviceState=false;
							}
						}
						
					}
				}
				
					if (application.regis.serviceTypeList.get(i).serviceState) {
						item.add(str);
						itemID.add(id);
						application.regis.serviceTypeList.get(i).serviceState= false;
						ll_category_showitem.addView(getViewItem(str));
						
					} else {
						item.remove(str);
						itemID.remove(id);
						application.regis.serviceTypeList.get(i).serviceState= true;
						ll_category_showitem.removeAllViews();
						for (String str : item) {
							ll_category_showitem.addView(getViewItem(str));
						}
					}
				
			}
		});
		return view;
	}
	
	private View getViewItem(final String strs) {
		View view = getLayoutInflater().inflate(R.layout.disease_show_item,
				null);
		TextView tv_disease_show_item = (TextView) view
				.findViewById(R.id.tv_disease_show_item);
		tv_disease_show_item.setText(strs);
		return view;
	}
}

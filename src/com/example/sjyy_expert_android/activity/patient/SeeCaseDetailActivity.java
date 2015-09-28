package com.example.sjyy_expert_android.activity.patient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.MyApplication;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.patient.SeeCaseDemandRequestBean;
import com.example.sjyy_expert_android.entity.patient.SeeCaseDemandResons;
import com.example.sjyy_expert_android.entity.patient.SeeCaseOrderRequestBean;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;


public class SeeCaseDetailActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private final static int SUCCESS = 1;
	private SeeCaseDemandRequestBean bean;
	private SeeCaseOrderRequestBean orderbean;
	private SeeCaseDemandResons base;
	private TextView tv_see_case_name, tv_see_case_ege, tv_see_case_city,
			tv_see_case_casename, tv_see_case_condition_description,
			tv_see_case_operation_history, tv_see_case_history_of_drug_use,
			tv_see_case_historical_condition;
	private Integer caseId;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof SeeCaseDemandResons) {
					base = (SeeCaseDemandResons) msg.obj;
					if (base.resultCode.equals("1000")) {
						initdata();
					} else {
						showToast(base.resultInfo);
					}
				} else {
					showToast(msg.obj + "");
				}

				break;

			default:
				break;
			}
			stopProgressDialog();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.see_case_detail_activity);
		initview();
	}

	private void initview() {
		Intent intent=getIntent();
		caseId=intent.getIntExtra("caseId", 0);
		
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_left.setText("");
		btn_title_right.setText("");
		tv_title_content.setText("查看病例");
		tv_see_case_name=(TextView) findViewById(R.id.tv_see_case_name);
		tv_see_case_ege=(TextView) findViewById(R.id.tv_see_case_ege);
		tv_see_case_city=(TextView) findViewById(R.id.tv_see_case_city);
		tv_see_case_casename=(TextView) findViewById(R.id.tv_see_case_casename);
		tv_see_case_condition_description=(TextView) findViewById(R.id.tv_see_case_condition_description);
		tv_see_case_operation_history=(TextView) findViewById(R.id.tv_see_case_operation_history);
		tv_see_case_history_of_drug_use=(TextView) findViewById(R.id.tv_see_case_history_of_drug_use);
		tv_see_case_historical_condition=(TextView) findViewById(R.id.tv_see_case_historical_condition);
		
		startProgressDialog();
		if(MyApplication.if_demand){
			new dataThread().start();
		}else{
			
		}
		
		
		
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new SeeCaseDemandRequestBean();
			bean.demandId=caseId;
			bean.userId=ShareUtil.getAccountId(SeeCaseDetailActivity.this);
			Object data = NetRequestEngine.getDemandPatientInfo(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}
	
	
	class orderDataThread extends Thread {

		@Override
		public void run() {
			orderbean = new SeeCaseOrderRequestBean();
			orderbean.orderId=caseId;
			orderbean.userId=ShareUtil.getAccountId(SeeCaseDetailActivity.this);
			Object data = NetRequestEngine.getOrderPatientInfo(orderbean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

	private void initdata() {
		tv_see_case_name.setText(base.patientInfo.name);
		tv_see_case_ege.setText(base.patientInfo.age+"");
		tv_see_case_city.setText(base.patientInfo.city);
		tv_see_case_casename.setText(base.patientInfo.diseaseName);
		tv_see_case_condition_description.setText(base.patientInfo.patientCondition);
		tv_see_case_operation_history.setText(base.patientInfo.operationHistory);
		tv_see_case_history_of_drug_use.setText(base.patientInfo.drugHistory);
		tv_see_case_historical_condition.setText(base.patientInfo.historyCondition);
	}

}

package com.example.sjyy_expert_android.activity.doctor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.simonvt.datepicker.DatePickDialog;
import net.simonvt.datepicker.DatePickDialog.IgetDate;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.appointment.GoAppintment;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;


/***
 * 类描述：患者预约医生登门时间
 * @author 海洋
 */
public class BeSureAppointmentActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private final static int SUCCESS = 1;

	private GoAppintment bean;
	private BaseRespons base;

	private RelativeLayout rl_change_time;
	private EditText et_problem;
	private String problem;
	private Button btn_commit;
	private String time;
	private TextView tv_time;
	private String gettime, doctorId;
	private Intent intent;
	private int showYear;
	private Spinner sp_spinner;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	private int serviceTypeId;
	
	private ArrayList<Integer> list;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {

					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						intent.putExtra("success", "success");
						BeSureAppointmentActivity.this.setResult(RESULT_OK,
								intent);
						showToast("预约成功!");
						finish();
					} else {
						showToast(base.resultInfo);
					}
				} else {
					
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
		setContentView(R.layout.be_sure_appointment_activity);
		initviews();
	}

	private void initviews() {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
//		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
//		time = formatter.format(curDate);
		
		
		
		intent = getIntent();
		doctorId = intent.getStringExtra("doctorId");
		list=intent.getIntegerArrayListExtra("serviceTypeIdList");
		tv_time = (TextView) findViewById(R.id.tv_time);
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("预约");
		sp_spinner=(Spinner) findViewById(R.id.sp_spinner);
		
		btn_commit = (Button) findViewById(R.id.btn_commit);
		rl_change_time = (RelativeLayout) findViewById(R.id.rl_change_refuse);
		et_problem = (EditText) findViewById(R.id.et_refuse);
		rl_change_time.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
		
		
		 data_list = new ArrayList<String>();
		 
		 Collections.sort(list);
		 for(Integer i: list){
			 if(i==1){
				 data_list.add("坐诊");
			 }else if(i==2){
				 data_list.add("会诊");
			 }else if(i==3){
				 data_list.add("主刀");
			 }
		 }
		 
	        //适配器
	        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
	        //设置样式
	        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        //加载适配器
	        sp_spinner.setAdapter(arr_adapter);
	        
	        sp_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
//				showToast(position+"");
				String str=sp_spinner.getSelectedItem().toString();
				if(str.equals("坐诊")){
					serviceTypeId=1;
				}else if(str.equals("会诊")){
					serviceTypeId=2;
				}else if(str.equals("主刀")){
					serviceTypeId=3;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.rl_change_refuse:

//			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
//					BeSureAppointmentActivity.this, null);
//			dateTimePicKDialog.dateTimePicKDialog(tv_time, time);
			
			DatePickDialog datePickDialog = new DatePickDialog(BeSureAppointmentActivity.this,new IgetDate(){
        		public void getDate(int year, int month, int day) {
        			// TODO Auto-generated method stub
        			showYear=month+1;
        			String Month=showYear<10?"0"+showYear:""+showYear;
        			String Day=day<10?"0"+day:""+day;
        			time=year+"-"+Month+"-"+Day;
        			tv_time.setText(time);
        		}
        		
        	}, "日期选择", "确定", "取消");
        	datePickDialog.show();
			
			break;
		case R.id.btn_commit:
			problem = et_problem.getText().toString().trim();
			gettime = tv_time.getText().toString().trim();
			if (!problem.equals("")) {
				if (!gettime.equals("")) {
					startProgressDialog();
					new dataThread().start();
				} else {
					showToast("请选择登门时间");
				}
			} else {
				showToast("请填写问题描述");
			}
			break;
		default:
			break;
		}

	}

	class dataThread extends Thread {
		@Override
		public void run() {
			bean = new GoAppintment();
			bean.doctorId = doctorId;
			bean.userId = ShareUtil
					.getAccountId(BeSureAppointmentActivity.this);
			bean.subscribeDate = gettime;
			bean.questionDesciption = problem;
			bean.serviceTypeId=serviceTypeId;
			Object data = NetRequestEngine.besureappointment(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

}

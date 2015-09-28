package com.example.sjyy_expert_android.activity.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.simonvt.datepicker.DatePickDialog;
import net.simonvt.datepicker.DatePickDialog.IgetDate;
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
import com.example.sjyy_expert_android.MainActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.orderBean.ReleaseDemandBean;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;


public class ReleaseDemandActivity extends BaseActivity{
	
	
	
	//发布需求
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private Button btn_release_demand;
	private RelativeLayout rl_change_out_time;
	private final static int SUCCESS = 1;
	private ReleaseDemandBean bean;
	private BaseRespons base;
	private String time;
	private EditText et_perfect_location_choice,et_perfect_specific,et_perfect_Budget_money;
	private TextView tv_out_time;
	
	
	private Spinner spinner;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	private int serviceTypeId;
	private int showYear;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {
					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						goNewActivity(MainActivity.class);
						showToast("发布成功，快去看看吧!");
						finish();
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
		setContentView(R.layout.release_demand_activity);
		initview();
	}

	private void initview() {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
//		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
//		time = formatter.format(curDate);
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_right.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("发布需求");
		tv_out_time=(TextView) findViewById(R.id.tv_out_time);
		btn_release_demand=(Button) findViewById(R.id.btn_release_demand);
		rl_change_out_time=(RelativeLayout) findViewById(R.id.rl_change_out_time);
		et_perfect_location_choice=(EditText) findViewById(R.id.et_perfect_location_choice);
		et_perfect_specific=(EditText) findViewById(R.id.et_perfect_specific);
		et_perfect_Budget_money=(EditText) findViewById(R.id.et_perfect_Budget_money);
		rl_change_out_time.setOnClickListener(this);
		btn_release_demand.setOnClickListener(this);
		
		
		
		 spinner = (Spinner) findViewById(R.id.spinner);
	        //数据
	        data_list = new ArrayList<String>();
	        data_list.add("坐诊");
	        data_list.add("会诊");
	        data_list.add("主刀");
	        //适配器
	        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
	        //设置样式
	        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        //加载适配器
	        spinner.setAdapter(arr_adapter);
	        
	       spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
//				showToast(position+"");
				if(position==0){
					serviceTypeId=1;
				}else if(position==1){
					serviceTypeId=2;
				}else if(position==2){
					serviceTypeId=3;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
	}
	
	
	private String location_choice,specific,outtime;
	private double Budget_money;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 注册
		case R.id.btn_title_left:
			finish();
			break;
		// 登陆
		case R.id.btn_release_demand:
			location_choice=et_perfect_location_choice.getText().toString();
			specific=et_perfect_specific.getText().toString();
			Budget_money=Double.parseDouble(et_perfect_Budget_money.getText().toString());
			outtime=tv_out_time.getText().toString();
			if(!location_choice.equals("")){
				if(!specific.equals("")){
					if(!outtime.equals("")){
						if(!et_perfect_Budget_money.getText().toString().equals("")){
							startProgressDialog();
							new dataThread().start();
						}else{
							showToast("请填写您的预算");
						}
					}else{
						showToast("请填写医生的出诊时间");
					}
				}else{
					showToast("请详细的描述一下您的需求");
				}
			}else{
				showToast("请简单的描述一下您的需求");
			}
			break;
		// 選擇出診時間
		case R.id.rl_change_out_time:
			
//			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
//					ReleaseDemandActivity.this, null);
//			dateTimePicKDialog.dateTimePicKDialog(tv_out_time, time);
			DatePickDialog datePickDialog = new DatePickDialog(ReleaseDemandActivity.this,new IgetDate(){
        		public void getDate(int year, int month, int day) {
        			// TODO Auto-generated method stub
        			showYear=month+1;
        			String Month=showYear<10?"0"+showYear:""+showYear;
        			String Day=day<10?"0"+day:""+day;
        			time=year+"-"+Month+"-"+Day;
        			tv_out_time.setText(time);
        		}
        		
        	}, "日期选择", "确定", "取消");
        	datePickDialog.show();
			break;

		default:
			break;
		}
	}

	
	class dataThread extends Thread {
		@Override
		public void run() {
			super.run();
			bean = new ReleaseDemandBean();
			bean.demandTitle=location_choice;
			bean.demandDesc=specific;
			bean.demandTimeString=outtime;
			bean.serviceTypeId=serviceTypeId;
			bean.demandPrice=new BigDecimal(Budget_money);
			bean.userId=ShareUtil.getAccountId(ReleaseDemandActivity.this);
			Object data = NetRequestEngine.makeDemand(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
			
		}
	}
}

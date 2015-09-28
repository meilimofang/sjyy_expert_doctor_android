package com.example.sjyy_expert_android.activity.about;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.util.ViewUtil;

/***
 * 类描述：呼叫销售
 * 
 * @author 海洋
 */
public class CallSalesActivity extends BaseActivity{
	
	
	private TextView tv_call_name,tv_call_phone;
	private Button btn_call_sales;
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private String name,phone;
	private AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_sales_activity);
		initview();
	}

	private void initview() {
		Intent intent=getIntent();
		name=intent.getStringExtra("employeeName");
		phone=intent.getStringExtra("employeeTel");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_right.setText("");
		tv_title_content.setText("私家医院");
		tv_call_name=(TextView) findViewById(R.id.tv_call_name);
		tv_call_phone=(TextView) findViewById(R.id.tv_call_phone);
		btn_call_sales=(Button) findViewById(R.id.btn_call_sales);
		tv_call_name.setText(name);
		tv_call_phone.setText(phone);
		btn_title_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_call_sales.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 alertDialog = ViewUtil.showDialog(CallSalesActivity.this, "提示",
							"确认呼叫销售吗？", "确认", "取消", new OnClickListener() {

								@Override
								public void onClick(View v) {
									 Intent intent5 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
						             startActivity(intent5);  
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
		
		
		
	}
	
	
}

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



public class ServiceActivity extends BaseActivity{
	private Button btn_title_left, btn_title_right,btn_service_call;
	private TextView tv_title_content;
	private AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_activity);
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_service_call=(Button) findViewById(R.id.btn_service_call);
		btn_service_call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 alertDialog = ViewUtil.showDialog(ServiceActivity.this, "提示",
							"确认拨打客服电话4009262200", "确认", "取消", new OnClickListener() {

								@Override
								public void onClick(View v) {
									 Intent intent5 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:4009262200"));  
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
		
		
		btn_title_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("服务中心");
	}

}

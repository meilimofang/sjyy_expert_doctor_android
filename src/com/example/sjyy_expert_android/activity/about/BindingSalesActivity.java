package com.example.sjyy_expert_android.activity.about;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.employyee.SubmitEmployeeInfoBean;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;


/***
 * 类描述：绑定销售
 * 
 * @author 海洋
 */
public class BindingSalesActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private final static int SUCCESS = 1;
	private SubmitEmployeeInfoBean bean;
	private BaseRespons base;
	
	private EditText et_employee_number,et_employee_name;
	private Button btn_binding_submit;
	private String name;
	private Integer number;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {
					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						showToast("绑定成功！");
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
		setContentView(R.layout.binding_sales_activity);
		initview();
	}

	private void initview() {
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_right.setText("");
		tv_title_content.setText("绑定员工");
		et_employee_number=(EditText) findViewById(R.id.et_employee_number);
		et_employee_name=(EditText) findViewById(R.id.et_employee_name);
		btn_binding_submit=(Button) findViewById(R.id.btn_binding_submit);
		btn_binding_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.btn_binding_submit:
			name=et_employee_name.getText().toString();
			number=Integer.parseInt(et_employee_number.getText().toString());
			if(!number.equals("")){
				if(!name.equals("")){
					startProgressDialog();
					new dataThread().start();
				}else{
					showToast("员工名字不能为空");
				}
			}else{
				showToast("员工编号不能为空");
			}
			break;
		case  R.id.btn_title_left:
			finish();
			break;
		default:
			break;
		}
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new SubmitEmployeeInfoBean();
			bean.employeeName=name;
			bean.employeeId=number;
			bean.userId=ShareUtil.getAccountId(BindingSalesActivity.this);
			Object data = NetRequestEngine.matchEmployee(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}
}

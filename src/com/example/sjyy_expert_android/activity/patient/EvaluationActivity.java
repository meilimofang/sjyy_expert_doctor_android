package com.example.sjyy_expert_android.activity.patient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.patient.EvaluationRequest;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;



/***
 * 类描述：评价列表
 * 
 * @author 海洋
 */
public class EvaluationActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private final static int SUCCESS = 1;

	private EvaluationRequest bean;
	private BaseRespons base;

	private int shuiping;
	private int taidu;
	private int chengdu;
	private EditText et_eva;

	private ImageView imageView1, imageView2, imageView3, imageView4,
			imageView5;
	private ImageView iv_1, iv_2, iv_3, iv_4, iv_5;
	private Button btn_eva_commit;
	private String orderId;
	private String evaluateContent;
	private RadioGroup rg_chengdu;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {

					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						finish();
						showToast("评价成功!");
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
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evaluation_activity);
		initview();
	}

	private void initview() {
		Intent intent = getIntent();
		orderId = intent.getStringExtra("orderId");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("评价");
		rg_chengdu = (RadioGroup) findViewById(R.id.rg_chengdu);
		et_eva = (EditText) findViewById(R.id.et_eva);
		imageView1 = (ImageView) findViewById(R.id.ev_1);
		imageView2 = (ImageView) findViewById(R.id.ev_2);
		imageView3 = (ImageView) findViewById(R.id.ev_3);
		imageView4 = (ImageView) findViewById(R.id.ev_4);
		imageView5 = (ImageView) findViewById(R.id.ev_5);
		imageView1.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView3.setOnClickListener(this);
		imageView4.setOnClickListener(this);
		imageView5.setOnClickListener(this);
		iv_1 = (ImageView) findViewById(R.id.iv_1);
		iv_2 = (ImageView) findViewById(R.id.iv_2);
		iv_3 = (ImageView) findViewById(R.id.iv_3);
		iv_4 = (ImageView) findViewById(R.id.iv_4);
		iv_5 = (ImageView) findViewById(R.id.iv_5);
		iv_1.setOnClickListener(this);
		iv_2.setOnClickListener(this);
		iv_3.setOnClickListener(this);
		iv_4.setOnClickListener(this);
		iv_5.setOnClickListener(this);
		rg_chengdu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.radioBtn1:
					chengdu=0;
					break;
				case R.id.radioBtn2:
					chengdu=1;
					break;
				case R.id.radioBtn3:
					chengdu=2;
					break;

				default:
					break;
				}

			}
		});
		btn_eva_commit = (Button) findViewById(R.id.btn_eva_commit);
		btn_eva_commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:

			finish();
			break;

		case R.id.ev_1:
			shuiping = 1;
			imageView1.setBackgroundResource(R.drawable.star);
			imageView2.setBackgroundResource(R.drawable.star_down);
			imageView3.setBackgroundResource(R.drawable.star_down);
			imageView4.setBackgroundResource(R.drawable.star_down);
			imageView5.setBackgroundResource(R.drawable.star_down);
			break;
		case R.id.ev_2:
			shuiping = 2;
			imageView1.setBackgroundResource(R.drawable.star);
			imageView2.setBackgroundResource(R.drawable.star);
			imageView3.setBackgroundResource(R.drawable.star_down);
			imageView4.setBackgroundResource(R.drawable.star_down);
			imageView5.setBackgroundResource(R.drawable.star_down);
			break;
		case R.id.ev_3:
			shuiping = 3;
			imageView1.setBackgroundResource(R.drawable.star);
			imageView2.setBackgroundResource(R.drawable.star);
			imageView3.setBackgroundResource(R.drawable.star);
			imageView4.setBackgroundResource(R.drawable.star_down);
			imageView5.setBackgroundResource(R.drawable.star_down);
			break;
		case R.id.ev_4:
			shuiping = 4;
			imageView1.setBackgroundResource(R.drawable.star);
			imageView2.setBackgroundResource(R.drawable.star);
			imageView3.setBackgroundResource(R.drawable.star);
			imageView4.setBackgroundResource(R.drawable.star);
			imageView5.setBackgroundResource(R.drawable.star_down);
			break;
		case R.id.ev_5:
			shuiping = 5;
			imageView1.setBackgroundResource(R.drawable.star);
			imageView2.setBackgroundResource(R.drawable.star);
			imageView3.setBackgroundResource(R.drawable.star);
			imageView4.setBackgroundResource(R.drawable.star);
			imageView5.setBackgroundResource(R.drawable.star);
			break;
		case R.id.iv_1:
			taidu = 1;
			iv_1.setBackgroundResource(R.drawable.star);
			iv_2.setBackgroundResource(R.drawable.star_down);
			iv_3.setBackgroundResource(R.drawable.star_down);
			iv_4.setBackgroundResource(R.drawable.star_down);
			iv_5.setBackgroundResource(R.drawable.star_down);
			break;
		case R.id.iv_2:
			taidu = 2;
			iv_1.setBackgroundResource(R.drawable.star);
			iv_2.setBackgroundResource(R.drawable.star);
			iv_3.setBackgroundResource(R.drawable.star_down);
			iv_4.setBackgroundResource(R.drawable.star_down);
			iv_5.setBackgroundResource(R.drawable.star_down);
			break;
		case R.id.iv_3:
			taidu = 3;
			iv_1.setBackgroundResource(R.drawable.star);
			iv_2.setBackgroundResource(R.drawable.star);
			iv_3.setBackgroundResource(R.drawable.star);
			iv_4.setBackgroundResource(R.drawable.star_down);
			iv_5.setBackgroundResource(R.drawable.star_down);
			break;
		case R.id.iv_4:
			taidu = 4;
			iv_1.setBackgroundResource(R.drawable.star);
			iv_2.setBackgroundResource(R.drawable.star);
			iv_3.setBackgroundResource(R.drawable.star);
			iv_4.setBackgroundResource(R.drawable.star);
			iv_5.setBackgroundResource(R.drawable.star_down);
			break;
		case R.id.iv_5:
			taidu = 5;
			iv_1.setBackgroundResource(R.drawable.star);
			iv_2.setBackgroundResource(R.drawable.star);
			iv_3.setBackgroundResource(R.drawable.star);
			iv_4.setBackgroundResource(R.drawable.star);
			iv_5.setBackgroundResource(R.drawable.star);
			break;
		case R.id.btn_eva_commit:
			evaluateContent = et_eva.getText().toString();
			startProgressDialog();
			new dataThread().start();
			break;

		default:
			break;
		}
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new EvaluationRequest();
			bean.orderId = orderId;
			bean.doctorLevel = shuiping;
			bean.evaluateContent = evaluateContent;
			bean.evaluateLevel = chengdu;
			bean.serviceLevel = taidu;
			bean.userId = ShareUtil.getAccountId(EvaluationActivity.this);
			Object data = NetRequestEngine.patientEvaluateOrder(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}
}

package com.example.sjyy_expert_android.activity.about;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.MyApplication;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.activity.LoginActivity;
import com.example.sjyy_expert_android.entity.choosebean.ChooseDepartmentBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseHospitalBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseServiceTypeBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseTitle;
import com.example.sjyy_expert_android.entity.login.LoginFinshBean;
import com.example.sjyy_expert_android.util.ClearClassStatic;
import com.example.sjyy_expert_android.util.PreferenceConstans;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.example.sjyy_expert_android.util.ViewUtil;
import com.example.sjyy_expert_android.view.SwitchButton;
import com.umeng.update.UmengUpdateAgent;


public class SettingActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content,tv_app_update;
	private RelativeLayout rl_clear_cache, rl_app_update;
	private AlertDialog alertDialog;
	private Button btn_sign_out;
	private SwitchButton sb_setstate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		initview();
	}

	private void initview() {
		sb_setstate=(SwitchButton) findViewById(R.id.sb_setstate);
		sb_setstate.setChecked(true);
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("设置");
		rl_clear_cache = (RelativeLayout) findViewById(R.id.rl_clear_cache);
		rl_app_update = (RelativeLayout) findViewById(R.id.rl_app_update);
		rl_clear_cache.setOnClickListener(this);
		rl_app_update.setOnClickListener(this);
		btn_sign_out=(Button) findViewById(R.id.btn_sign_out);
		btn_sign_out.setOnClickListener(this);
		tv_app_update=(TextView) findViewById(R.id.tv_app_update);
		
		if(MyApplication.if_app_update){
			tv_app_update.setText("有新版本");
		}else{
			tv_app_update.setText("已是新版");
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_clear_cache:
			alertDialog = ViewUtil.showDialog(this, "提示", "确定清除缓存吗?", "确认",
					"取消", new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showToast("缓存已清空!");
							alertDialog.dismiss();

						}
					}, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							alertDialog.dismiss();
						}
					});
			alertDialog.setCancelable(false);
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
			break;
		case R.id.rl_app_update:
			 UmengUpdateAgent.forceUpdate(SettingActivity.this);
			break;
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_sign_out:
			ShareUtil.putStringShare(SettingActivity.this,
					PreferenceConstans.SP_PASSWORD, "");
			ClearClassStatic.clear(ChooseServiceTypeBean.class);
			ClearClassStatic.clear(ChooseTitle.class);
			ClearClassStatic.clear(ChooseDepartmentBean.class);
			ClearClassStatic.clear(ChooseHospitalBean.class);
			LoginFinshBean.if_finsh=true;
			Intent intent = new Intent();
			intent.setClass(SettingActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

}

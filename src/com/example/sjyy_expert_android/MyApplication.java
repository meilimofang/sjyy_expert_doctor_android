package com.example.sjyy_expert_android;



import android.app.Application;

import com.example.sjyy_expert_android.entity.orderBean.MyVisit;
import com.example.sjyy_expert_android.entity.registered.RegisterResponse;

public class MyApplication extends Application{
	public static RegisterResponse regis;
	public  static MyVisit visit;
	public static boolean if_app_update=false;
	public static Integer hasRemandInfo=0;
	public static boolean if_demand=true;
	@Override
	public void onCreate() {
		super.onCreate();
		
		
	}
}

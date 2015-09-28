package com.example.sjyy_expert_android;



import com.example.sjyy_expert_android.activity.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 3000; // 延迟三秒

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this,
						LoginActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);
	}
}

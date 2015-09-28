package com.example.sjyy_expert_android.PreferenceConstans;




import android.content.Context;
import android.content.SharedPreferences;

import com.example.sjyy_expert_android.util.PreferenceConstans;

public class SP_ConfigManager {
	
	
	public static SharedPreferences getConfig_sp(Context ctx){
		
		return ctx.getSharedPreferences(PreferenceConstans.SP_USERINFO, Context.MODE_PRIVATE);
		
	}

}

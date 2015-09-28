package com.example.sjyy_expert_android.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.sjyy_expert_android.PreferenceConstans.SP_ConfigManager;



public class ShareUtil {
	
	static Context mContext;
	static SharedPreferences config_sp;
	/**
	 * 
	 * @param mContext
	 * @param shareName
	 * @param b
	 */
	public static void putBooleanShare(Context mContext,String field,Boolean b){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		Editor editor = config_sp.edit();
		editor.putBoolean(field, b);
		editor.commit();
		
	}
	
	public static boolean getBoolean (Context mContext,String field){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getBoolean(field, false);
	}
	/**
	 * 
	 * @param mContext
	 * @param shareName
	 * @param num
	 */
	public static void putIntShare(Context mContext,String field,int num){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		Editor editor = config_sp.edit();
		editor.putInt(field, num);
		editor.commit();
	}
	/**
	 * 
	 * @param mContext
	 * @param shareName
	 * @param str
	 */
	public static void putStringShare(Context mContext,String field,String str){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		Editor editor = config_sp.edit();
		editor.putString(field, str);
		editor.commit();
	}
	/**
	 * 
	 * @param mContext
	 * @return 返回AccountId
	 */
	public static int getAccountId(Context mContext){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getInt(PreferenceConstans.SP_ACCOUNTID, -1);
	}
	/**
	 * 
	 * @param mContext
	 * @return 返回Role
	 */
	public static int getRole(Context mContext){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getInt(PreferenceConstans.SP_ROLE, -1);
	}
	/**
	 * 
	 * @param mContext
	 * @return 返回用户名
	 */
	public static String getAccount(Context mContext){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getString(PreferenceConstans.SP_ACCOUNT, "");
	}
	/**
	 * 
	 * @param mContext
	 * @return 返回是否记住密码
	 */
	public static boolean getPassword(Context mContext){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getBoolean(PreferenceConstans.SP_SAVEPASSWORD, true);
	}
	/**
	 * 
	 * @param mContext
	 * @return 返回密码的内容
	 */
	public static String getPasswordContent(Context mContext){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getString(PreferenceConstans.SP_PASSWORD, "");
	}
	/**
	 * 
	 * @param mContext
	 * @return 返回账号
	 */
	public static String getUserNameContent(Context mContext){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getString(PreferenceConstans.SP_USERNAME, "");
	}
	/**
	 * 
	 * @param mContext
	 * @return 登录状态
	 * true已登录，false未登录
	 */
	public static boolean getLoginState(Context mContext){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getBoolean(PreferenceConstans.SP_ISLOGIN, false);
	}
	
	
	/**
	 * 
	 * @param mContext
	 * @return 返回CityId
	 */
	public static int getSessionId(Context mContext){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getInt(PreferenceConstans.SP_SESSIONID, -1);
	}
	
	/**
	 * 
	 * @param mContext
	 * @return 返回CityId
	 */
	public static int getifUserInfo(Context mContext){
		config_sp = SP_ConfigManager.getConfig_sp(mContext);
		return config_sp.getInt(PreferenceConstans.SP_IFUSERINFO, -1);
	}
	
	/**
	 * 清空share
	 * @param shareName
	 */
	public static void clearShare(Context mContext,String shareName) {
		config_sp = mContext.getSharedPreferences(shareName,Activity.MODE_PRIVATE);
		Editor editor = config_sp.edit();
		editor.clear();
		editor.commit();
	}
	
	/**
	 * 把对象放进share里
	 * 
	 * @param shareName
	 * @param o
	 */
	public void setShareForEntry(String shareName, Object o) {
		try {
			config_sp = SP_ConfigManager.getConfig_sp(mContext);
			Editor editor = config_sp.edit();
			editor.clear();
			editor.commit();

			List<Field> list = getObjFieldWithExtends(o.getClass());
			for (Field f : list) {
				String name = f.getName();
				try {
					Object rtcs = f.get(o);
					if (rtcs instanceof String) {
						editor.putString(name, (String) rtcs);
					} else if (rtcs instanceof Boolean) {
						editor.putBoolean(name, (Boolean) rtcs);
					} else if (rtcs instanceof Float) {
						editor.putFloat(name, (Float) rtcs);
					} else if (rtcs instanceof Integer) {
						editor.putInt(name, (Integer) rtcs);
					} else if (rtcs instanceof Long) {
						editor.putLong(name, (Long) rtcs);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			editor.commit();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 获取对象里边的public属性
	 * 
	 * @param cls
	 * @return
	 */
	public static List<Field> getObjFieldWithExtends(Class<?> cls) {
		if (cls.equals(Object.class)) {
			return null;
		}
		List<Field> list = new ArrayList<Field>();
		Field[] mds = cls.getFields();
		for (int i = 0; i < mds.length; i++) {
			list.add(mds[i]);
		}
		return list;
	}

}

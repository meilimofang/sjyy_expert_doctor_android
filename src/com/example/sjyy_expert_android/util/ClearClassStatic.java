package com.example.sjyy_expert_android.util;

import java.lang.reflect.Field;

public class ClearClassStatic {
	public static void clear(Class userCla){
		Field[] fs = userCla.getDeclaredFields();  
		for(Field field :fs){
			try {
				if("boolean".equals(field.getType().toString())){
					field.set(null, false);
				}
				field.set(null, null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

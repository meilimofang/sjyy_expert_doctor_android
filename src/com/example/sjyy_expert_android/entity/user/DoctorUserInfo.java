package com.example.sjyy_expert_android.entity.user;

import java.util.List;
import java.util.Set;

import com.example.sjyy_expert_android.entity.registered.RegisterResponse;




public class DoctorUserInfo extends RegisterResponse{
	
	public String  iconUrl;
	public String  name;
	public String  tel;
	public String  hospital;
	public String  department;//科室
	public String  qualification;//职称
	public String  professionField;//技能
	public String  practiceExperience;//医学背景
	public int role;
	public Integer visbility;
	public  List<String> diseaseList;
	public List<Integer> diseaseIdsList; 
	public List<String> serviceTypeNameList;
	public List<Integer> serviceTypeIdList;

}

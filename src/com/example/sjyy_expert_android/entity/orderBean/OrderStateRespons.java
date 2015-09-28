package com.example.sjyy_expert_android.entity.orderBean;

import java.util.List;

import com.example.sjyy_expert_android.entity.BaseRespons;



public class OrderStateRespons extends BaseRespons{

	public List<DetailList> demandDetailList;
	public Info demand;
	public  DoctorInfo source;
	public Integer sourceType;//1为医生  2 为医院
}

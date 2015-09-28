package com.example.sjyy_expert_android.entity.orderBean;

import java.util.List;

import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.patient.PatientList;

public class MyOrderBean extends BaseRespons{
	
	public List<PatientList> orderList;
	public int orderTotalCount;
}

package com.example.sjyy_expert_android.entity.orderBean;

import java.util.List;

import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.appointment.Doctor;


public class AppointmentOrderDetailRepson extends BaseRespons{

	
	public List<OrderDetail> details;
	public DetailInFo orderInfo;
	public Integer sourceType;
	public DoctorInfo source;
	
}

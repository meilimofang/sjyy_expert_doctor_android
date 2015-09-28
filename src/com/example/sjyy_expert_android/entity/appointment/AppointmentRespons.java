package com.example.sjyy_expert_android.entity.appointment;

import java.util.List;

import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.registered.ServiceType;



public class AppointmentRespons extends BaseRespons{
	public List<Doctor> doctordatas;
	public List<ServiceType> apponitmentdatas;
	public int totalCount;
	
}

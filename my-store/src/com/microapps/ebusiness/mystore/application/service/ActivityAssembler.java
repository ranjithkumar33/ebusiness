package com.microapps.ebusiness.mystore.application.service;

import java.sql.Timestamp;

import com.microapps.ebusiness.mystore.application.domain.ActivityDto;
import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.entity.Activity;
import com.microapps.ebusiness.mystore.application.entity.Customer;

public class ActivityAssembler {
	
	public static ActivityDto toDto(Activity entity, CustomerDto c) {
		ActivityDto dto = new ActivityDto();
		dto.setAmount(entity.getAmount());
		dto.setName(entity.getName());
		dto.setCreatedOn(entity.getCreatedOn());
		dto.setCustomer(c);
		dto.setEarnedPoints(entity.getEarnedPoints());
		dto.setId(entity.getId());
		dto.setItemGroup(entity.getItemGroup());
		return dto;
	}
	
	public static Activity toEntity(ActivityDto dto, Customer c) {
		Activity entity = new Activity();
		entity.setAmount((long)dto.getAmount());
		entity.setName(dto.getName());
		entity.setCreatedOn(new Timestamp(System.currentTimeMillis()));
		entity.setCreatedBy(SecurityContext.getSecurityContext().getAuthenticatedUser().getUsername());
		entity.setCustomer(c);
		entity.setEarnedPoints((int)dto.getEarnedPoints());
		entity.setId(dto.getId());
		entity.setItemGroup(dto.getItemGroup());
		return entity;
	}


}

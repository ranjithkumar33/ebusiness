package com.microapps.ebusiness.mystore.application.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.microapps.ebusiness.mystore.application.domain.ActivityDto;
import com.microapps.ebusiness.mystore.application.entity.Activity;

public class ActivityAssembler {
	
	public static ActivityDto toDto(Activity entity) {
		ActivityDto dto = new ActivityDto();
		dto.setAmount(entity.getAmount());
		dto.setName(entity.getName());
		dto.setCreatedOn(entity.getCreatedOn());
		dto.setEarnedPoints(entity.getEarnedPoints());
		dto.setId(entity.getId());
		dto.setItemGroup(entity.getItemGroup());
		return dto;
	}
	
	public static Activity toEntity(ActivityDto dto) {
		Activity entity = new Activity();
		entity.setAmount((long)dto.getAmount());
		entity.setName(dto.getName());
		entity.setCreatedOn(new Timestamp(System.currentTimeMillis()));
		entity.setCreatedBy(SecurityContext.getSecurityContext().getAuthenticatedUser().getUsername());
		entity.setEarnedPoints((int)dto.getEarnedPoints());
		entity.setId(dto.getId());
		entity.setItemGroup(dto.getItemGroup());
		return entity;
	}
	
	public static List<ActivityDto> toActivityDtoList(List<Activity> dList) {
		List<ActivityDto> l = new ArrayList<>();
		if(dList !=  null && !dList.isEmpty()) {
			for(Activity a : dList) {
				l.add(toDto(a));
			}
		}
		return l;
	}
	


}

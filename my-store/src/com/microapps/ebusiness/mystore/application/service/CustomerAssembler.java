package com.microapps.ebusiness.mystore.application.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.entity.Customer;

public class CustomerAssembler {
	
	
	public static List<Customer> toEntityList(List<CustomerDto> dList) {
		List<Customer> cList = null;
		if(dList != null) {
			cList = new ArrayList<>();
			for(CustomerDto c : dList) {
				cList.add(toEntity(c));
			}
		}
		return cList;
	}
	
	
	
	public static Customer toEntity(CustomerDto dto) {
		Customer e = new Customer();
		e.setCardNumber(dto.getCardNumber());
		e.setCreatedBy(SecurityContext.getSecurityContext().getAuthenticatedUser().getUsername());
		e.setCreatedOn(new Timestamp(System.currentTimeMillis()));
		e.setDob(dto.getDob());
		e.setEmail(dto.getEmail());
		e.setGender(dto.getGender());
		e.setId(dto.getId());
		e.setMobile(dto.getMobile());
		e.setName(dto.getName());
		e.setStatus(true);
		return e;
	}
	
	
	public static CustomerDto toDto(Customer e) {
		if(e == null) return null;
		CustomerDto dto = new CustomerDto();
		dto.setCardNumber(e.getCardNumber());
		dto.setCreatedOn(e.getCreatedOn());
		dto.setDob(e.getDob());
		dto.setEmail(e.getEmail());
		dto.setGender(e.getGender());
		dto.setId((int)e.getId());
		dto.setMobile(e.getMobile());
		dto.setName(e.getName());
		dto.setAuthenticated(e.isStatus());
		if(e.getTotalPurchaseAmount() != null) {
			dto.setTotalPurchaseAmount(e.getTotalPurchaseAmount());
		}
		dto.setActivities(ActivityAssembler.toActivityDtoList(e.getActivities()));
		return dto;
	}

}

package com.microapps.ebusiness.mystore.application.service;

import java.sql.Timestamp;

import com.microapps.ebusiness.mystore.application.domain.AppSettingsDto;
import com.microapps.ebusiness.mystore.application.entity.AppSettings;

public class SettingsAssembler {

	
		public static AppSettingsDto toDto(AppSettings e) {
			AppSettingsDto dto = new AppSettingsDto();
			dto.setMpf(e.getMpf());
			dto.setPmf(e.getPmf());
			dto.setRtp(e.getRtp());
			return dto;
		}
		
		
		public static AppSettings toEntity(AppSettingsDto dto) {
			AppSettings s = new AppSettings();
			s.setCreatedBy(SecurityContext.getSecurityContext().getAuthenticatedUser().getUsername());
			s.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			s.setMpf(dto.getMpf());
			s.setPmf(dto.getPmf());
			s.setRtp(dto.getRtp());
			s.setStatus(true);
			return s;
		}
	
}

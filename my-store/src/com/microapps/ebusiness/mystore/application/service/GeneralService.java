package com.microapps.ebusiness.mystore.application.service;

import java.io.IOException;

import com.microapps.ebusiness.mystore.application.dao.SettingsDao;
import com.microapps.ebusiness.mystore.application.dao.SettingsDaoImpl;
import com.microapps.ebusiness.mystore.application.dao.SystemDaoImpl;
import com.microapps.ebusiness.mystore.application.domain.AppSettingsDto;
import com.microapps.ebusiness.mystore.application.entity.AppSettings;
import com.microapps.ebusiness.mystore.application.exception.SettingNotFoundException;

public class GeneralService {
	
	public GeneralService() {
		dao = new SettingsDaoImpl();
		this.sysdao = new SystemDaoImpl();
	}
	
	private SettingsDao dao;
	
	private SystemDaoImpl sysdao;
	
	public AppSettingsDto saveSettings(AppSettingsDto as) {
		AppSettings s = SettingsAssembler.toEntity(as);
		s = dao.saveSettings(s);
		as = SettingsAssembler.toDto(s);
		Session.getSession().setSettings(as);
		return Session.getSession().getSettings();
	}
	
	public AppSettingsDto loadAppSettings() throws SettingNotFoundException {
		try {
			AppSettings as = dao.findSettings();
			if(null == as) {
				throw new SettingNotFoundException("Application settings not found!");
			}
			Session.getSession().setSettings(SettingsAssembler.toDto(as));
		} catch (Exception e) {
			throw new SettingNotFoundException("Application settings not found!");
		}
		return Session.getSession().getSettings();
	}
	
	public void backupDb() throws IOException {
		sysdao.backup();
	}

}

package com.microapps.ebusiness.mystore.application.service;

import com.microapps.ebusiness.mystore.application.domain.AppSettingsDto;
import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.util.License;

public class Session {
    
	private Session() {}
	
	private static Session session;
	
	private CustomerDto c;
	
	private AppSettingsDto settings;
	
	private License license;
	
	private int customerId;
	
	public static Session getSession() {
		if(null == session) {
			session = new Session();
		}
		return session;
	}
	
	
	public int getCustomerId() {
		return customerId;
	}


	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}


	public void addCustomerToSession(CustomerDto c) {
		this.c=c;
	}
	
	public void removeCustomerFromSession() {
		this.c=null;
	}
	
	public CustomerDto getCustomerFromSession() {
		return this.c;
	}


	public AppSettingsDto getSettings() {
		return settings;
	}


	public void setSettings(AppSettingsDto settings) {
		this.settings = settings;
	}


	public License getLicense() {
		return license;
	}


	public void setLicense(License license) {
		this.license = license;
	}
	
}

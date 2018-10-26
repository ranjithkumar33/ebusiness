package com.microapps.ebusiness.mystore.application.service;

import com.microapps.ebusiness.mystore.application.dao.RegistrationDao;
import com.microapps.ebusiness.mystore.application.dao.UserRegistrationDaoImpl;
import com.microapps.ebusiness.mystore.application.entity.BusinessUser;

public class LoginService {
	
	public LoginService() {
		this.dao= new UserRegistrationDaoImpl();
	}
	
	private RegistrationDao dao;
	
	public boolean login(String username, String password) {
			BusinessUser bu = dao.findBusinessUser(username, password);
			if(bu == null) {
				return false;
			}else {
				SecurityContext.getSecurityContext().addUserToContext(new UserDetails(bu.getName(), bu.getUserName()));
				return true;
			}
	}
	
}

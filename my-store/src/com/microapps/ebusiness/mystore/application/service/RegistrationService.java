package com.microapps.ebusiness.mystore.application.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import com.microapps.ebusiness.mystore.application.dao.BusinessRegistrationDaoImpl;
import com.microapps.ebusiness.mystore.application.dao.RegistrationDao;
import com.microapps.ebusiness.mystore.application.dao.UserRegistrationDaoImpl;
import com.microapps.ebusiness.mystore.application.domain.Registration;
import com.microapps.ebusiness.mystore.application.entity.Business;
import com.microapps.ebusiness.mystore.application.entity.BusinessUser;
import com.microapps.ebusiness.mystore.application.exception.BusinessNotRegisteredException;
import com.microapps.ebusiness.mystore.application.exception.DuplicateEntryException;
import com.microapps.ebusiness.mystore.application.exception.RegistrationFailedException;

public class RegistrationService {
	
	private static final Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());
	
	private static final String APP_PREF_NAME= "MyShopAppPref";
	
	private static final String APP_PREF_KEY= "APP_ACT_STS";
	
	private RegistrationDao businessRegistrationDao;
	
	private RegistrationDao userRegistrationDao;
	
	public RegistrationService() {
		this.businessRegistrationDao=new BusinessRegistrationDaoImpl();
		this.userRegistrationDao=new UserRegistrationDaoImpl();
	}
	
	public Registration registerAndActivateBusiness(Registration reg) throws RegistrationFailedException  {
		
		validateRegistrationKey(reg.getActivationCode().trim());
		
			Business b = new Business();
			b.setName(reg.getProName());
			b.setBusinessName(reg.getBussinessName());
			b.setAddress(reg.getBusinessAddress());
			b.setActivationCode(reg.getActivationCode());
			b.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			b.setStatus(true);
			b = businessRegistrationDao.saveBusiness(b);
			
			updatePref();
			
			return reg;
	}
	
	private void updatePref() {
		Preferences prefs = Preferences.userRoot().node(APP_PREF_NAME);
		prefs.putBoolean(APP_PREF_KEY, true);
	}

	
	private boolean isAppActivated() {
		Preferences prefs = Preferences.userRoot().node(APP_PREF_NAME);
		return prefs.getBoolean(APP_PREF_KEY, false);
	}
	
	
	private void validateRegistrationKey(String key) throws RegistrationFailedException{
		
		try {
			boolean b = isKeyExpired(key);
			if(b) {
				 throw new RegistrationFailedException("Activation code is expired!");
			 }
		} catch (IllegalArgumentException e) {
			throw new RegistrationFailedException("Invalid activation code!");
		}
		 
		
	}

	public void registerUser(String name, String userName, String password) throws RegistrationFailedException  {
			BusinessUser bu = new BusinessUser();
			bu.setName(name);
			bu.setUserName(userName);
			bu.setPassword(password);
			try {
				bu=  userRegistrationDao.saveBusinessUser(bu);
			} catch (com.microapps.ebusiness.mystore.application.dao.exception.DuplicateEntryException e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
				throw new RegistrationFailedException("User registration failed -> " + e.getMessage());
			}
	}
	

	public boolean isBusinessRegistered() throws BusinessNotRegisteredException, SQLException {
		return isAppActivated();
		
	}
	
	private static boolean isKeyExpired(String key) throws RegistrationFailedException {
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(key);
			LocalDateTime tokenDate = LocalDateTime.parse(new String(decodedBytes));
			LocalDateTime yesterDaySameTime = LocalDateTime.now().minusDays(1);
			
			if(yesterDaySameTime.isBefore(tokenDate)) {//is not expired
				System.out.println("Not expired");
				return false;
			}else {
				System.out.println("Expired");
				return true;
			
			}
		} catch (Exception e) {
			throw new RegistrationFailedException("Invalid activation code!");
		}
	}
	
	private static String generateKey() {
		LocalDateTime dt = LocalDateTime.of(LocalDate.of(2018, Month.OCTOBER, 18), LocalTime.of(16, 30, 0));
		byte[] encodedBytes = Base64.getEncoder().encode(dt.toString().getBytes());
		return new String(encodedBytes);
	}
	
	public static void main(String[] args) throws RegistrationFailedException {
		
		String key = generateKey();
		
	    System.out.println("Generated key >>  " + key);
		
		isKeyExpired(key);
		
		//storeKeyinPref();
		
	}

	private static void storeKeyinPref() {
		Preferences prefs = Preferences.userRoot().node("MyShopAppPref");
	//	prefs.putBoolean("APP_ACT_STS", true);
		System.out.println(prefs.getBoolean("APP_ACT_STS", false));
		
	}

}

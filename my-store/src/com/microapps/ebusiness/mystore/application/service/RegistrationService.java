package com.microapps.ebusiness.mystore.application.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.microapps.ebusiness.mystore.application.dao.BusinessRegistrationDaoImpl;
import com.microapps.ebusiness.mystore.application.dao.RegistrationDao;
import com.microapps.ebusiness.mystore.application.dao.UserRegistrationDaoImpl;
import com.microapps.ebusiness.mystore.application.domain.Registration;
import com.microapps.ebusiness.mystore.application.entity.Business;
import com.microapps.ebusiness.mystore.application.entity.BusinessUser;
import com.microapps.ebusiness.mystore.application.exception.RegistrationFailedException;
import com.microapps.ebusiness.mystore.application.util.License;
import com.microapps.ebusiness.mystore.application.util.LicenseType;

public class RegistrationService {
	
	private static final Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());
	
	private static final String APP_PREF_NAME= "MyStoreAppLic";
	
	private static final String APP_LICENSE_ACTIVATION_STATUS= "APP_LIC_ACT_STS";
	private static final String APP_LICENSE_ACTIVATION_TYPE= "APP_LIC_TYPE";
	private static final String APP_LICENSE_ACTIVATION_DATETIME= "APP_LIC_DT";
	private static final String APP_LICENSE_ACTIVATION_EXTENSION_COUNT= "APP_LIC_EXT_CNT";
	
	private RegistrationDao businessRegistrationDao;
	
	private RegistrationDao userRegistrationDao;
	
	public RegistrationService() {
		this.businessRegistrationDao=new BusinessRegistrationDaoImpl();
		this.userRegistrationDao=new UserRegistrationDaoImpl();
	}
	
	public Registration registerAndActivateBusiness(Registration reg) throws RegistrationFailedException  {
			//validateRegistrationKey(reg.getActivationCode().trim());
			Business b = new Business();
			b.setName(reg.getProName());
			b.setBusinessName(reg.getBussinessName());
			b.setAddress(reg.getBusinessAddress());
			b.setActivationCode(reg.getActivationCode());
			b.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			b.setStatus(true);
			b = businessRegistrationDao.saveBusiness(b);
			
			updatePref(reg.getActivationCode());
			
			return reg;
	}
	
	private void updatePref(String licenseTypeName) throws RegistrationFailedException {
		Preferences prefs = Preferences.userRoot().node(APP_PREF_NAME);
		prefs.putBoolean(APP_LICENSE_ACTIVATION_STATUS, true);
		prefs.put(APP_LICENSE_ACTIVATION_TYPE, LicenseType.getLicenseTypeByName(licenseTypeName).getCode());
		prefs.putLong(APP_LICENSE_ACTIVATION_DATETIME, System.currentTimeMillis());
		prefs.putLong(APP_LICENSE_ACTIVATION_DATETIME, System.currentTimeMillis());
		prefs.putInt(APP_LICENSE_ACTIVATION_EXTENSION_COUNT, getLicenseExtensionCount(licenseTypeName));
		
		checkExpiry(prefs.getLong(APP_LICENSE_ACTIVATION_DATETIME, System.currentTimeMillis()), prefs.get(APP_LICENSE_ACTIVATION_TYPE, null));
	}

	
	private int getLicenseExtensionCount(String licenseTypeName) {
		Preferences prefs = Preferences.userRoot().node(APP_PREF_NAME);
		int count = prefs.getInt(APP_LICENSE_ACTIVATION_EXTENSION_COUNT, 0) + 1;
		return count;
	}
	
	public void uninstall() {
		if(Session.getSession().getLicense() != null && LicenseType.DEMO == Session.getSession().getLicense().getType()) {
			clearAppPrefs();
		}
	}
	
	private void clearAppPrefs() {
		Preferences prefs = Preferences.userRoot().node(APP_PREF_NAME);
		try {
			prefs.clear();
		} catch (BackingStoreException e) {
			LOGGER.log(Level.WARNING, "Could not clear LIC settings");
		}
	}

	private boolean checkExpiry(long regDayTimeInmillis, String licenseCode) throws  RegistrationFailedException {
		boolean result = false;
			switch(LicenseType.getLicenseTypeByCode(licenseCode)) {
				case DEMO: 
					result = isExpired(LicenseType.DEMO, regDayTimeInmillis);
					if(!result) {
						int daysLeft = expiresIn(LicenseType.DEMO, regDayTimeInmillis);
						Session.getSession().setLicense(new License(LicenseType.DEMO, daysLeft));
					}
				break;
				case TWO_WEEKS_TRAIL: 
					result = isExpired(LicenseType.TWO_WEEKS_TRAIL, regDayTimeInmillis);
					if(!result) {
						int daysLeft = expiresIn(LicenseType.TWO_WEEKS_TRAIL, regDayTimeInmillis);
						Session.getSession().setLicense(new License(LicenseType.TWO_WEEKS_TRAIL, daysLeft));
					}
				break;	
				case ONE_YEAR_PAID: 
					result =  isExpired(LicenseType.ONE_YEAR_PAID, regDayTimeInmillis);
					if(!result) {
						int daysLeft = expiresIn(LicenseType.ONE_YEAR_PAID, regDayTimeInmillis);
						Session.getSession().setLicense(new License(LicenseType.ONE_YEAR_PAID, daysLeft));
					}
				break;	
			}
		return result;
	}
	
	private int expiresIn(LicenseType licenseType, long regDayTimeInmillis) {
		LocalDateTime regDate = Instant.ofEpochMilli(regDayTimeInmillis).atZone(ZoneOffset.UTC).toLocalDateTime();
		int daysElapsed = Period.between(regDate.toLocalDate(), LocalDate.now()).getDays();
		return licenseType.getValidity() - daysElapsed;
	}

	private boolean isExpired(LicenseType licenseType, long regDayTimeInmillis) {
		LocalDateTime regDate = Instant.ofEpochMilli(regDayTimeInmillis).atZone(ZoneOffset.UTC).toLocalDateTime();
		int daysElapsed = Period.between(regDate.toLocalDate(), LocalDate.now()).getDays();
		if(daysElapsed > licenseType.getValidity()) {
			return true;
		}
		return false;
	}


	/*private void validateRegistrationKey(String key) throws RegistrationFailedException{
		
		try {
			boolean b = isKeyExpired(key);
			if(b) {
				 throw new RegistrationFailedException("Activation code is expired!");
			 }
		} catch (IllegalArgumentException e) {
			throw new RegistrationFailedException("Invalid activation code!");
		}
		 
		
	}*/

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
	

	public boolean isBusinessRegistered() {
		Preferences prefs = Preferences.userRoot().node(APP_PREF_NAME);
		if(prefs.getBoolean(APP_LICENSE_ACTIVATION_STATUS, false)) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isLicenseExpired() throws RegistrationFailedException {
		Preferences prefs = Preferences.userRoot().node(APP_PREF_NAME);
		return checkExpiry(prefs.getLong(APP_LICENSE_ACTIVATION_DATETIME, System.currentTimeMillis()), prefs.get(APP_LICENSE_ACTIVATION_TYPE, null));
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

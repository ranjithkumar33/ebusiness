package com.microapps.ebusiness.mystore.application.util;

import com.microapps.ebusiness.mystore.application.exception.RegistrationFailedException;

public enum LicenseType {
	
	DEMO("demo","D", 1, 5), TWO_WEEKS_TRAIL("2weekstrial","2WT", 14, 1), ONE_YEAR_PAID("1yearpaid","1YP", 365, 0);
	
	LicenseType(String name, String code, int validity, int licenceExtensionCount){
		this.code=code;
		this.name=name;
		this.validity=validity;
		this.licenceExtensionCount=licenceExtensionCount;
	}
 
	private String code;
	
	private String name;
	
	private int validity;
	
	private int licenceExtensionCount;

	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public int getValidity() {
		return validity;
	}

	public int getLicenceExtensionCount() {
		return licenceExtensionCount;
	}

	public static LicenseType getLicenseTypeByName(String name) throws RegistrationFailedException {
		for(LicenseType at : values()) {
			if(name.equalsIgnoreCase(at.getName())) {
				return at;
			}
		}
		throw new RegistrationFailedException ("Invalid activation code");
	}
	
	public static LicenseType getLicenseTypeByCode(String code) throws RegistrationFailedException {
		for(LicenseType at : values()) {
			if(code.equalsIgnoreCase(at.getCode())) {
				return at;
			}
		}
		throw new RegistrationFailedException ("Invalid activation code");
	}
}

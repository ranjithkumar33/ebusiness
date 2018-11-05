package com.microapps.ebusiness.mystore.application.util;

import java.io.Serializable;

public final class License implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7096047756303610270L;
	
	public License(LicenseType type, int expiresInDays) {
		this.type=type;
		this.expiresInDays=expiresInDays;
	}
	
	private LicenseType type;
	
	private int expiresInDays;

	public LicenseType getType() {
		return type;
	}

	public void setType(LicenseType type) {
		this.type = type;
	}

	public int getExpiresInDays() {
		return expiresInDays;
	}

	public void setExpiresInDays(int expiresInDays) {
		this.expiresInDays = expiresInDays;
	}

}

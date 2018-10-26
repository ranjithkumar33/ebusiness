package com.microapps.ebusiness.mystore.application.domain;

public class Registration {
	
	

	public Registration(String proName, String bussinessName, String businessAddress, String activationCode) {
		super();
		this.proName = proName;
		this.bussinessName = bussinessName;
		this.businessAddress = businessAddress;
		this.activationCode = activationCode;
	}

	private String proName;
	
	private String bussinessName;
	
	private String businessAddress;
	
	private String activationCode;

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getBussinessName() {
		return bussinessName;
	}

	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
	
	
}

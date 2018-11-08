package com.microapps.ebusiness.mystore.application.domain;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CustomerDto {
	
	public CustomerDto() {
		this.activities = new ArrayList<>();
	}
	
	public CustomerDto(int id, String name, String gender, double totalPurchaseAmount, Timestamp createdOn) {
		this.id=id;
		this.name=name;
		this.totalPurchaseAmount=totalPurchaseAmount;
		this.createdOn=createdOn;
		this.gender = gender.charAt(0);
	}
	
    public CustomerDto(String name, String mobile, String email, String cardNumber, char gender, Date dob) {
		this.name=name;
		this.email=email;
		this.mobile=mobile;
		this.gender=gender;
		this.cardNumber=cardNumber;
		this.dob=dob;
		this.activities = new ArrayList<>();
	}
	
    public CustomerDto(int id, String name, String mobile, String email, String cardNumber, char gender, Date dob, Timestamp createdOn) {
    	this.id=id;
		this.name=name;
		this.email=email;
		this.mobile=mobile;
		this.gender=gender;
		this.cardNumber=cardNumber;
		this.dob=dob;
		this.createdOn=createdOn;
		this.activities = new ArrayList<>();
	}
	
	private int id;
	
	private String name;
	
	private String mobile;
	
	private String email;
	
	private Date dob;
	
	private char gender;
	
	private String cardNumber;
	
	private double totalPurchaseAmount;
	
	private Timestamp createdOn;
	
	private List<ActivityDto> activities;
	
	private boolean isAuthenticated;

	public List<ActivityDto> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityDto> activities) {
		this.activities = activities;
	}

	public double getTotalPurchaseAmount() {
		return totalPurchaseAmount;
	}

	public void setTotalPurchaseAmount(double totalPurchaseAmount) {
		this.totalPurchaseAmount = totalPurchaseAmount;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public char getGender() {
		return gender;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

}

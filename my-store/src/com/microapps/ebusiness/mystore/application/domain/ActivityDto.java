package com.microapps.ebusiness.mystore.application.domain;

import java.sql.Timestamp;

public class ActivityDto {
	
	public ActivityDto() {this.customer=new CustomerDto();}
	
	public ActivityDto(String name, double amount) {
		this.name=name;
		this.amount=amount;
	}
	
	
	public ActivityDto(String name, double amount, Timestamp createdOn) {
		this.name=name;
		this.amount=amount;
		this.createdOn=createdOn;
	}
	
	
	public ActivityDto(String name, String itemGroup, double amount) {
		this.name=name;
		this.amount=amount;
		this.itemGroup=itemGroup;
	}
	
	
	private long id;
	
	private CustomerDto customer;
	
	private String name;
	
	private double amount;
	
	private String itemGroup;
	
	private long earnedPoints;
	
	private Timestamp createdOn;

	public long getId() {
		return id;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public CustomerDto getCustomer() {
		return customer;
	}

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}

	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getEarnedPoints() {
		return earnedPoints;
	}

	public void setEarnedPoints(long earnedPoints) {
		this.earnedPoints = earnedPoints;
	}
	

}

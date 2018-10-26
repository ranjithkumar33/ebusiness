package com.microapps.ebusiness.mystore.application.service;

import java.io.Serializable;

public class UserDetails implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2979683625280442355L;
	
	

	public UserDetails(String name, String username, int id) {
		super();
		this.name = name;
		this.username = username;
		this.id = id;
	}
	
	public UserDetails(String name, String username) {
		super();
		this.name = name;
		this.username = username;
	}

	private String name;
	
	private String username;
	
	private int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

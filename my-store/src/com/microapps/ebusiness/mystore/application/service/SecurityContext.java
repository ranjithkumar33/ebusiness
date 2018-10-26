package com.microapps.ebusiness.mystore.application.service;

public class SecurityContext {
	
	private SecurityContext() {}
	
	private static SecurityContext context;
	
	private UserDetails ud;
	
	public static SecurityContext getSecurityContext() {
		if(null == context) {
			context = new SecurityContext();
		}
		return context;
	}
	
	public void addUserToContext(UserDetails ud) {
		this.ud=ud;
	}
	
	public void removeUserFromContext() {
		this.ud=null;
	}
	
	
	public boolean isUserLoggedIn() {
		return (getAuthenticatedUser()!=null);
	}
	
	public UserDetails getAuthenticatedUser() {
		return this.ud;
	}

}

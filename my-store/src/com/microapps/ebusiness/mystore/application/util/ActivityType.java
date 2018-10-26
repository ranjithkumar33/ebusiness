package com.microapps.ebusiness.mystore.application.util;

import java.util.Arrays;
import java.util.List;

public enum ActivityType {
	
	NEW_SALE("New sale"), SALE_RETURN("Sale return"), CANCELLATION("Cancellation"), REDEMPTION("Points Redemption");
	
	private ActivityType(String name){
		this.name=name;
	}

	private String name;
	
	public String getName() {
		return name;
	}

	public static List<ActivityType> getAllActivityTypes(){
	    return Arrays.asList(values());
	}
	
	public static ActivityType getActivityTypeByName(String name) {
		for(ActivityType at : values()) {
			if(name.equals(at.getName())) {
				return at;
			}
		}
		return null;
	}
	
}

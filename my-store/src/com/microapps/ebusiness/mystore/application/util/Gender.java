package com.microapps.ebusiness.mystore.application.util;

public enum Gender {
	
	Male('M'), Female('F');
	
	Gender(char g){
		this.g=g;
	}
	
	private char g;
	
	
	
	public static Gender getGender(char gender) {
		
		for(Gender gn : values()) {
			if(gn.g == gender) {
				return gn;
			}
		}
		
		return null;
	}

}

package com.microapps.ebusiness.mystore.application.util;

public enum DuplicateEntryField {
	
	mobile(2), cardNumber(3);
	
	DuplicateEntryField(int key){
		this.key=key;
	}
	
	private int key;
	
	
	
	public int getKey() {
		return key;
	}



	public static DuplicateEntryField findFieldByKey(int key) {
		for(DuplicateEntryField at : values()) {
			if(key == at.getKey()) {
				return at;
			}
		}
		return null;
	}
	

}

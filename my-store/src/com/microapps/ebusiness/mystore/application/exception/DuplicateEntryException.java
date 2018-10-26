package com.microapps.ebusiness.mystore.application.exception;

public class DuplicateEntryException extends Exception{
	
	
	private int key;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7597036930883312035L;

	public DuplicateEntryException(String message, int key) {
		super(message);
		this.key=key;
	}

	public int getKey() {
		return key;
	}
	

}

package com.microapps.ebusiness.mystore.application.util;

public interface UIValidationUtils {

	int MAX_LENGTH_OF_CARD_NUMBER = 16;
	
	int MAX_LENGTH_OF_MOBILE_NUMBER = 10;
	
	int MAX_LENGTH_OF_CUSTOMER_NAME = 50;
	
	int MAX_LENGTH_OF_LOYALTY_POINTS = 10;
	
	int MAX_LENGTH_OF_EMAIL = 50;
	
	int MAX_LENGTH_OF_USER_NAME = 10;
	
	int MAX_LENGTH_OF_PASSWORD = 10;
	
	String REGEX_FOR_NUMBER = "\\d+";
	
	String REGEX_FOR_CARD_NUMBER = "\\d{16}";
	
	String REGEX_FOR_CUSTOMER_NAME = "^[\\p{L} .'-]+$";
	
	String REGEX_FOR_MOBBILE_NUMBER = "^[6-9]\\d{9}$";
	
	String REGEX_FOR_EMAIL = "^(.+)@(.+)$";
	
	String NAME_ERROR = "Not a proper name!";
	
	String LOGIN_ERROR = "Invalid username or password!";
	
	String CUSTOMER_SEARCH_ERROR_INVALID_CHARACTER = "Only numbers are allowed!";
	
	String MOBILE_NUMBER_ERROR = "Invalid mobile number!";
	
	String DUPLICATE_MOBILE_NUMBER_ERROR = "Duplicate mobile number!";
	
	String DUPLICATE_CARD_NUMBER_ERROR = "Duplicate card number!";
	
	String DUPLICATE_USERNAME = "Duplicate username!";
	
	String EMAIL_ERROR = "Invalid email!";
	
	String DATE_ERROR = "The DoB can not be a future date!";
	
	String GENDER_ERROR = "Please select a gender!";
	
	String CARD_NUMBER_ERROR = "Invalid card number!";
	
	String ACT_CODE_ERROR = "Invalid activation code!";
	
	String CONFIRM_PASSWORD_MISMATCH_ERROR = "Confirm password doesn't match with password!";
}

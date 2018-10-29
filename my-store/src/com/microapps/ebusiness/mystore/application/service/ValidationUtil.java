package com.microapps.ebusiness.mystore.application.service;

import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.exception.ValidationFailedException;

public class ValidationUtil {
	
	
	public static boolean validate(CustomerDto c) throws ValidationFailedException{
		
		if(c.getName() == null || c.getName().trim().length() == 0) {
			throw new ValidationFailedException("Name must not be empty");
		}
		
		if(c.getEmail() == null || c.getEmail().trim().length() == 0) {
			throw new ValidationFailedException("Email must not be empty");
		}
		
		if(c.getMobile() == null || c.getMobile().trim().length() == 0) {
			throw new ValidationFailedException("Mobile must not be empty");
		}
		

		if(c.getDob() == null) {
			throw new ValidationFailedException("DoB must not be empty");
		}
		return true;
		
	}

}


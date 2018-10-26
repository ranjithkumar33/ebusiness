package com.microapps.ebusiness.mystore.application.util;

import com.microapps.ebusiness.mystore.application.domain.CustomerDto;

public class CustomerNameUtils {
	
	public static String getNameWithTitle(CustomerDto c) {
		if(c.getGender() == 'M') {
			return "Mr. "+c.getName();
		}else if(c.getGender() == 'F') {
			return "Ms. "+c.getName();
		}
		return null;
	}


}

package com.microapps.ebusiness.mystore.application.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {
	
	 private static NumberFormat currenyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
	 
	 public static String getFormattedAmount(double amount) {
		return currenyFormat.format(amount);
	 }
	    

}

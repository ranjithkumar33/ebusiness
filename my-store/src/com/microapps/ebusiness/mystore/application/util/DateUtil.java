package com.microapps.ebusiness.mystore.application.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

	
	public static final String PATTERN = "dd-MM-yyyy";
	
	public static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
	
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN);
	
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);


	public static LocalDate fromString(String string) {
		if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
	}

	public static String toString(LocalDate date) {
		if (date != null) {
            return dateFormatter.format(date);
        } else {
            return "";
        }
	}
	
	public static String toString(LocalDateTime date) {
		if (date != null) {
            return dateTimeFormatter.format(date);
        } else {
            return "";
        }
	}
	
	public static LocalDate format(LocalDate date) {
		if (date != null) {
            return LocalDate.parse(dateFormatter.format(date), dateFormatter);
        } else {
            return null;
        }
	}
}

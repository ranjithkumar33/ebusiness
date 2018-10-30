package com.microapps.ebusiness.mystore.application.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

	
	public static final String PATTERN = "dd-MM-yyyy";
	
	public static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
	
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN);
	
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
	
	private static DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


	public static LocalDate fromString(String string) {
		if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
	}
	
	public static LocalDateTime fromString(String string, boolean isDateTime) throws ParseException {
		if (string != null && !string.isEmpty()) {
			//LocalDateTime dt = LocalDateTime.parse(string.trim(), dateTimeFormatter);
			Date d = df.parse(string);
            return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
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

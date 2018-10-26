package com.microapps.ebusiness.mystore.application.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class DateConvertor extends StringConverter<LocalDate>{
	
	public static final String PATTERN = "dd-MM-yyyy";
	
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN);


	@Override
	public LocalDate fromString(String string) {
		if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
	}

	@Override
	public String toString(LocalDate date) {
		if (date != null) {
            return dateFormatter.format(date);
        } else {
            return "";
        }
	}

}

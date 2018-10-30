package com.microapps.ebusiness.mystore.application.util;

import java.io.File;
import java.util.List;
import java.util.function.BiConsumer;

import com.microapps.ebusiness.mystore.application.exception.CSVParseException;

public interface Parser {
	
	void setProgressUpdate(BiConsumer<Integer, Integer> progress);
	
	void setProgressMessage(BiConsumer<String, String> message);
	
	void parseFile()throws CSVParseException;
	
	int getSuccessCount() ;
	
	List<String> getErrorList();
	
	File getErroFile();

}

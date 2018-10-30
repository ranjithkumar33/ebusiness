package com.microapps.ebusiness.mystore.application.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.microapps.ebusiness.mystore.application.exception.CSVParseException;

public class ItemMasterDataCsvParcer {
	
	private static final int ATTR_LENGTH = 1;
	
	private static final String DELIMITER = ",";
	
	private BufferedReader br;
	
	private List<String> itemGroups;
	
	private CsvLineParser lp;
	
	public ItemMasterDataCsvParcer(File file) throws CSVParseException{
		if(null == file) throw new CSVParseException("File not found");
		try {
			this.br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new CSVParseException("File not found");
		}
		this.itemGroups = new ArrayList<>();
	}
	
	public void parseFile() throws CSVParseException {
		 String line = "";
		 try {
	            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
	            	this.lp = new CsvLineParser(line);
	            	lp.parseLine();
	            	this.itemGroups.add(lp.getItemGroup());
	            }

	        } catch (FileNotFoundException e) {
	        	throw new CSVParseException("File not found");
	        } catch (IOException e) {
	        	throw new CSVParseException("Error");
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	}
	
	
	
	
	public List<String> getItemGroups() {
		return itemGroups;
	}

	class CsvLineParser{
		
		private String line;
		
		private int curPos;
		
		private String[] attrs;
		
		private String itemGroup;
		
		CsvLineParser(String line) throws CSVParseException{
			if(line==null) throw new CSVParseException("Null line");
			this.line=line;
			this.attrs = line.split(DELIMITER);
			if(this.attrs.length != ATTR_LENGTH) throw new CSVParseException("Invalid data format");
		}
		
		String getItemGroup() {
			return this.itemGroup;
		}
		
		void parseLine() throws CSVParseException{
			
			List<String> attList = Arrays.asList(this.attrs);
			for(String attr : attList) {
				
				switch(this.curPos) {
					case 0 : this.itemGroup = attr;
					break;
				}
			}
		}
	}
          
}


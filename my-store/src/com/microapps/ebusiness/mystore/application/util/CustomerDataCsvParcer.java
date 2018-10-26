package com.microapps.ebusiness.mystore.application.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.exception.CSVParseException;

public class CustomerDataCsvParcer {
	
	private static final int ATTR_LENGTH = 6;
	
	private static final String DELIMITER = ",";
	
	private BufferedReader br;
	
	private List<CustomerDto> customers;
	
	private CsvLineParser lp;
	
	public CustomerDataCsvParcer(File file) throws CSVParseException{
		if(null == file) throw new CSVParseException("File not found");
		try {
			this.br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new CSVParseException("File not found");
		}
		this.customers = new ArrayList<>();
	}
	
	public void parseFile() throws CSVParseException {
		 String line = "";
		 try {
	            while ((line = br.readLine()) != null) {
	            	this.lp = new CsvLineParser(line);
	            	lp.parseLine();
	            	this.customers.add(lp.getCustomer());
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
	
	
	
	
	public List<CustomerDto> getCustomers() {
		return customers;
	}




	class CsvLineParser{
		
		private String line;
		
		private int curPos;
		
		private String[] attrs;
		
		private CustomerDto c;
		
		CsvLineParser(String line) throws CSVParseException{
			if(line==null) throw new CSVParseException("Null line");
			this.line=line;
			this.attrs = line.split(DELIMITER);
			if(this.attrs.length != ATTR_LENGTH) throw new CSVParseException("Invalid data format");
			this.c = new CustomerDto();
		}
		
		CustomerDto getCustomer() {
			return this.c;
		}
		
		
		void parseLine() throws CSVParseException{
			
			List<String> attList = Arrays.asList(this.attrs);
			for(String attr : attList) {
				
				switch(this.curPos) {
					case 0 : this.c.setName(attr);
					this.curPos++;
					break;
					case 1 : this.c.setMobile(attr);
					this.curPos++;
					break;
					case 2 : this.c.setCardNumber(attr);
					this.curPos++;
					break;
					case 3 : 
						if(validGender(attr)) {
							this.c.setGender(attr.charAt(0));
						}
					this.curPos++;
					break;
					case 4 : 
						if(isValidDoB(attr)) {
							this.c.setDob(Date.valueOf(DateUtil.fromString(attr)));
						}
					this.curPos++;
					break;
					case 5 : 
						this.c.setEmail(attr);
					this.curPos++;
					break;
				}
			}
			
		}




		private boolean isValidDoB(String attr) throws CSVParseException {
			try {
				Date.valueOf(DateUtil.fromString(attr));
			} catch (Exception e) {
				throw new CSVParseException("Invalid DoB");
			}
			return true;
		}




		private boolean validGender(String attr) throws CSVParseException {
			if(attr.length() > 1) {
				throw new CSVParseException("Invalid gender");
			}
			
			Gender gen = Gender.getGender(attr.charAt(0));
			if(gen== null) {
				throw new CSVParseException("Gender should be a single character");
			}
			return true;
			
		}
		
	}
          
}


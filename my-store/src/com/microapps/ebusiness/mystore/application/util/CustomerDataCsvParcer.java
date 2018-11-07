package com.microapps.ebusiness.mystore.application.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import com.microapps.ebusiness.mystore.application.dao.exception.DuplicateEntryException;
import com.microapps.ebusiness.mystore.application.dao.exception.DuplicateEntryField;
import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.exception.CSVParseException;
import com.microapps.ebusiness.mystore.application.service.CustomerService;

public class CustomerDataCsvParcer implements Parser{
	
	private static final int ATTR_LENGTH = 6;
	
	private static final String DELIMITER = ",";
	
	private LineNumberReader br;
	
	private CustomerService cs;
	
	private CsvLineParser lp;
	
	private int totalLineCount;
	
	List<String> errorList;
	
	private int successCount;
	
	private File erroFile;
	
	 private BiConsumer<String, String> progressMessage ;
	
	 private BiConsumer<Integer, Integer> progressUpdate ;
	 
	 public void setProgressUpdate(BiConsumer<Integer, Integer> progressUpdate) {
	        this.progressUpdate = progressUpdate ;
	 }
	 
	 public void setProgressMessage(BiConsumer<String, String> progressMessage) {
	        this.progressMessage = progressMessage ;
	 }
	
	public CustomerDataCsvParcer(File file, CustomerService cs) throws CSVParseException, IOException{
		if(null == file) throw new CSVParseException("File not found");
		//C:\Users\Ranjith\Desktop\new-customersw.csv\new-customersw.csv_error.txt 
		String errorFileName = "customer_data_import_error.txt";
		this.erroFile = new File(System.getProperty("user.home")+ "\\"+ errorFileName);
		try {
			this.br = new LineNumberReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new CSVParseException("File not found");
		}
		this.totalLineCount = countTotalLines(file);
		this.cs=cs;
		this.errorList=new ArrayList<>();
	}
	
	private int countTotalLines(File file) throws IOException {
		 InputStream is = new BufferedInputStream(new FileInputStream(file));
		    try {
		        byte[] c = new byte[1024];
		        int count = 0;
		        int readChars = 0;
		        boolean empty = true;
		        while ((readChars = is.read(c)) != -1) {
		            empty = false;
		            for (int i = 0; i < readChars; ++i) {
		                if (c[i] == '\n') {
		                    ++count;
		                }
		            }
		        }
		        return (count == 0 && !empty) ? 1 : count;
		    } finally {
		        is.close();
		    }
	}

	public void parseFile() throws CSVParseException {
		 String line = "";
		 int cnt = 1;
		 int sCount = 0;
		 try {
	            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
	            	this.lp = new CsvLineParser(line);
	            	try {
	            		cnt++;
	            		CustomerDto c = lp.parseLine();
						
						cs.saveCustomer(c);
						sCount++;
						
						if (progressMessage != null) {
							progressMessage.accept("Processing ", "Processing " +c.getName());
			            }
						
						if (progressUpdate != null) {
			                progressUpdate.accept(cnt, totalLineCount);
			            }
					} catch (DuplicateEntryException e) {

						if (progressMessage != null) {
							progressMessage.accept("Processing ", "Failed");
			            }
						
						if (progressUpdate != null) {
			                progressUpdate.accept(cnt, totalLineCount);
			            }
						this.errorList.add(generateErrorMessage(br.getLineNumber(), e.getKey(), e.getMessage()));
					}
	            	
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
		 this.successCount = sCount;
		 postProcess();
	}
	
	
	
	public int getSuccessCount() {
		return successCount;
	}

	private String generateErrorMessage(int lineNumber, int key, String message) {
		DuplicateEntryField def = DuplicateEntryField.findFieldByKey(key);
		return "Error parsing line "+lineNumber+" >> Duplicate record ("+def.toString()+")\n";
	}

	private void postProcess() {
		writeToFile();
	}



	private void writeToFile() {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(this.erroFile), "UTF8"));
			out.append("Error occured while processing following lines \n--------------------------------------------------------\n");
			
			errorList.forEach(e -> {
				try {
					out.append(e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			
		    out.flush();
		    out.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}



	class CsvLineParser{
		
		private String line;
		
		private int curPos;
		
		private String[] attrs;
		
		CsvLineParser(String line) throws CSVParseException{
			if(line==null) throw new CSVParseException("Null line");
			this.line=line;
			this.attrs = line.split(DELIMITER);
			if(this.attrs.length != ATTR_LENGTH) throw new CSVParseException("Invalid data format");
		}
		
		
		private CustomerDto parseLine() throws CSVParseException, DuplicateEntryException{
			CustomerDto c = new CustomerDto();
			List<String> attList = Arrays.asList(this.attrs);
			for(String attr : attList) {
				attr = attr.trim();
				switch(this.curPos) {
					case 0 : c.setName(attr);
					this.curPos++;
					break;
					case 1 : c.setMobile(attr);
					this.curPos++;
					break;
					case 2 : c.setCardNumber(attr);
					this.curPos++;
					break;
					case 3 : 
						if(validGender(attr)) {
							c.setGender(attr.charAt(0));
						}
					this.curPos++;
					break;
					case 4 : 
						if(isValidDoB(attr)) {
							c.setDob(Date.valueOf(DateUtil.fromString(attr)));
						}
					this.curPos++;
					break;
					case 5 : 
						c.setEmail(attr);
					this.curPos++;
					break;
				}
			}
			
			return c;
			
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



	public File getErroFile() {
		return erroFile;
	}

	public List<String> getErrorList() {
		return errorList;
	}
          
}


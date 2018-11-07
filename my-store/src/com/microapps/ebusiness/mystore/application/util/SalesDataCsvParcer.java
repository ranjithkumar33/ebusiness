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
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import com.microapps.ebusiness.mystore.application.dao.exception.RecordNotFoundException;
import com.microapps.ebusiness.mystore.application.domain.ActivityDto;
import com.microapps.ebusiness.mystore.application.exception.CSVParseException;
import com.microapps.ebusiness.mystore.application.exception.SettingNotFoundException;
import com.microapps.ebusiness.mystore.application.service.ActivityService;

public class SalesDataCsvParcer implements Parser{
	
	private static final int ATTR_LENGTH = 4;
	
	private static final String DELIMITER = ",";
	
	private LineNumberReader br;
	
	private ActivityService as;
	
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
	
	public SalesDataCsvParcer(File file, ActivityService as) throws CSVParseException, IOException{
		if(null == file) throw new CSVParseException("File not found");
		//C:\Users\Ranjith\Desktop\new-customersw.csv\new-customersw.csv_error.txt 
		String errorFileName = "customer_data_import_error.txt";
		this.erroFile = new File(System.getProperty("user.home")+ "\\"+ errorFileName);
		try {
			this.br = new LineNumberReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new CSVParseException("File not found");
		}
		this.totalLineCount = countTotalLines(file)*2;
		this.as=as;
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
		 
		 List<ActivityDto> activityList = new ArrayList<>();
		 try {
	            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
	            	this.lp = new CsvLineParser(line);
	            		cnt++;
	            		ActivityDto a = lp.parseLine();
	            		activityList.add(a);
						sCount++;
						
						if (progressMessage != null) {
							progressMessage.accept("Processing ", "Processing " +a.getCustomer().getMobile());
			            }
						
						if (progressUpdate != null) {
			                progressUpdate.accept(cnt, totalLineCount);
			            }
	            }
	            if (progressMessage != null) {
					progressMessage.accept("Processing ", "Parsing data file completed.");
	            }
	            
	            if (progressMessage != null) {
					progressMessage.accept("Processing ", "Start DB Syncing...");
	            }
	            
	            try {
	            	cnt = as.syncSalesData(activityList, progressUpdate, totalLineCount, cnt);
					
					if (progressUpdate != null) {
		                progressUpdate.accept(cnt, totalLineCount);
		            }
					
					if (progressMessage != null) {
						progressMessage.accept("Processing ", "Completed successfully.");
		            }
					
				} catch (SettingNotFoundException | RecordNotFoundException e) {
					  if (progressMessage != null) {
							progressMessage.accept("Processing ", "Start DB Syncing failed! "+e.getMessage());
			          }
				}
	            

	        } catch (FileNotFoundException e) {
	        	throw new CSVParseException("File not found");
	        } catch (IOException e) {
	        	throw new CSVParseException("Error");
	        }  catch (CSVParseException e) {
	        	throw e;
	        } catch (ParseException e) {
	        	throw new CSVParseException(e.getMessage());
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
			if(this.attrs.length != ATTR_LENGTH) throw new CSVParseException("Invalid data format At line : "+line);
		}
		
		
		private ActivityDto parseLine() throws ParseException{
			ActivityDto a = new ActivityDto();
			List<String> attList = Arrays.asList(this.attrs);
			for(String attr : attList) {
				attr = attr.trim();
				switch(this.curPos) {
					case 0 : a.getCustomer().setMobile(attr);
					this.curPos++;
					break;
					case 1 : a.setCreatedOn(Timestamp.valueOf(DateUtil.fromString(attr, true)));
					this.curPos++;
					break;
					case 2 : a.setItemGroup(attr);
					this.curPos++;
					break;
					case 3 : a.setAmount(Double.parseDouble(attr));
					this.curPos++;
					break;
				}
			}
			
			return a;
			
		}
	}



	public File getErroFile() {
		return erroFile;
	}

	public List<String> getErrorList() {
		return errorList;
	}
          
}


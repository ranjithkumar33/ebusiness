package com.microapps.ebusiness.mystore.application;
	
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.microapps.ebusiness.mystore.application.controller.BaseController;
import com.microapps.ebusiness.mystore.application.dao.PersistanceUtil;
import com.microapps.ebusiness.mystore.application.exception.BusinessNotRegisteredException;
import com.microapps.ebusiness.mystore.application.service.GeneralService;
import com.microapps.ebusiness.mystore.application.service.RegistrationService;
import com.microapps.ebusiness.mystore.application.util.HostServiceUtil;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	
	   private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	   
	    private Parent rootNode;
	    
	    private static RegistrationService regService;
	    
	    private static GeneralService gs;

	    public static void main(final String[] args) {
	    	LOGGER.info("Starting appplication...");
	    	
	    	//System.setProperty("java.util.logging.config.file", "application.properties");
	    	
	    	regService = new RegistrationService();
	    	gs = new GeneralService();
	        Application.launch(args);
	    }
	    
	    @Override
	    public void stop() throws Exception {
	    	clean();
	    	backUpDB();
	    	regService.uninstall();
	    }

	    
	    private void backUpDB() {
	    	LOGGER.log(Level.INFO, "Backup DB..");
	    	try {
				gs.backupDb();
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Failed backup > "+e.getMessage());
			}
	    	LOGGER.log(Level.INFO, "Backup DB completed.");
		}

		@Override
	    public void init() throws Exception {
	    	  FXMLLoader fxmlLoader = null;
	    	  if(!regService.isBusinessRegistered()) {
	    		  fxmlLoader = new FXMLLoader(BaseController.class.getResource(ViewTemplateConstants.BUSINESS_REGISTRATION_VIEW));
	    	  }else {
	    		  if(regService.isLicenseExpired()) {
	    			  //Show license update view
	    		  }else {
	    			  fxmlLoader = new FXMLLoader(BaseController.class.getResource(ViewTemplateConstants.LOGIN_VIEW));
	    		  }
	    	  }
	    	  
	          rootNode = fxmlLoader.load();
	        //  String css = Main.class.getResource("application.css").toExternalForm(); 
	         // rootNode.getStylesheets().add(css);
	          startUpDb();
	          HostServiceUtil.setHostServices(getHostServices());
	    }


		private void startUpDb() {
	    		LOGGER.log(Level.INFO, "Connecting DB...");
				gs.initializeDb();
				LOGGER.log(Level.INFO, "DB connected successfully!");
				
				LOGGER.log(Level.INFO, "Run DDL...");
				
				//DBUtils.runDDL();
				
				LOGGER.log(Level.INFO, "Ran DDL successfully");
		}
	    
	    private void clean() {
	    	LOGGER.info("Shutdown appplication...");
	    	/*try {
				DBUtils.closeDatabase();
			} catch (ClassNotFoundException | SQLException e) {
				LOGGER.log(Level.SEVERE, "Closing Database failed!", e);
				exitWithError();
			}*/
	    	LOGGER.info("Completed");
	    }

		private void exitWithError() {
	    	LOGGER.log(Level.SEVERE, "The application closes because of the error");
	    	Platform.exit();
	    }

		@Override
	    public void start(Stage stage) throws Exception {
	    	stage.setScene(new Scene(rootNode));
	    	stage.getIcons().add(new Image(getClass().getResourceAsStream("my-shop-ico-3.png")));
	    	stage.setTitle("MyStore-v1.0.0");
	        stage.show();
	    }
}

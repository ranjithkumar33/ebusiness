package com.microapps.ebusiness.mystore.application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.exception.CSVParseException;
import com.microapps.ebusiness.mystore.application.service.ActivityService;
import com.microapps.ebusiness.mystore.application.util.SalesDataCsvParcer;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SalesDataSyncController  extends CustomerDataImportController{
	
	public SalesDataSyncController(){
	}
	
	@Override
	public void showView(Stage stage) {
		Stage dialogStage = new Stage();
		
		FXMLLoader fxmlLoader = new FXMLLoader(BaseController.class.getResource(ViewTemplateConstants.SALES_DATA_SYNC_VIEW));
			try {
				Parent rootNode = fxmlLoader.load();
				dialogStage.setTitle("Import data");
		        dialogStage.initModality(Modality.WINDOW_MODAL);
		        dialogStage.initOwner(stage);
		        Scene scene = new Scene(rootNode);
		        dialogStage.setScene(scene);
		             
		        SalesDataSyncController ac = fxmlLoader.getController();
		        ac.setDialogStage(dialogStage);
		        
		        dialogStage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		createTask();
	}
	
	@Override
	protected void createTask() {
		setTask(new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                try {
					cp = new SalesDataCsvParcer(file, new ActivityService());
					cp.setProgressUpdate((workDone, totalWork) -> 
				      updateProgress(workDone, totalWork));
					
					cp.setProgressMessage((workDone, msg) -> 
						updateMessage(msg));
					
					cp.parseFile();
					
					
				} catch (CSVParseException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				}

                return null;
            }

            
        } );
	}

}

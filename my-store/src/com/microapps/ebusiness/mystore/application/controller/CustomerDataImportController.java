package com.microapps.ebusiness.mystore.application.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.exception.CSVParseException;
import com.microapps.ebusiness.mystore.application.service.CustomerService;
import com.microapps.ebusiness.mystore.application.util.CustomerDataCsvParcer;
import com.microapps.ebusiness.mystore.application.util.Parser;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerDataImportController extends BaseController implements Initializable, Routeable{
	
	public CustomerDataImportController(){
		
	}
	
	private Stage rootNode;
	
	@FXML
	private Button importButton;
	
	 FileChooser fileChooser;
	
	@FXML
	private TextField fileName;
	
	File file;
	
	@FXML
	private ProgressBar progressBar;
	
	@FXML
	private Label successMsg;
	
	private Stage dialogStage;
	
	protected Parser cp;
	
	private Task<Void> task;
	
	@FXML
	protected void handleImport(ActionEvent event) {
		
		((Button)event.getSource()).setDisable(true);
		
		successMsg.setVisible(true);
		
		progressBar.progressProperty().unbind();
		
		progressBar.progressProperty().bind(task.progressProperty());
		
	   new Thread(task).start();
	   
	   progressBar.setVisible(true);
	   
	   task.messageProperty().addListener((obs, oldMsg, newMsg) -> {
		   successMsg.setText(newMsg);
		});
	   
	   task.setOnSucceeded(e->{
			((Button)event.getSource()).setDisable(false);
			progressBar.progressProperty().unbind();
			successMsg.setText("Success : "+cp.getSuccessCount() + ", Error: " + cp.getErrorList().size() + ", Log : "+cp.getErroFile().getAbsolutePath());
	   });
	   
	   task.setOnFailed(e->{
		  // ((Button)event.getSource()).setDisable(false);
		   progressBar.progressProperty().unbind();
		   successMsg.setText("Error :  Can not parse the file");
		   progressBar.progressProperty().unbind();
		   progressBar.setProgress(0);
	   });
	}
	
	protected void createTask() {
		this.task = new Task<Void>() {
          @Override
            protected Void call() throws Exception {
                try {
					cp = new CustomerDataCsvParcer(file, new CustomerService());
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
        }; 
    }        

	
	@FXML
	public void close() {
		this.dialogStage.close();
	}
	
	@Override
	public void showView(Stage stage) {
		Stage dialogStage = new Stage();
		
		FXMLLoader fxmlLoader = new FXMLLoader(BaseController.class.getResource(ViewTemplateConstants.CUSTOMER_DATA_IMPORT_VIEW));
			try {
				Parent rootNode = fxmlLoader.load();
				dialogStage.setTitle("Import data");
		        dialogStage.initModality(Modality.WINDOW_MODAL);
		        dialogStage.initOwner(stage);
		        Scene scene = new Scene(rootNode);
		        dialogStage.setScene(scene);
		             
		        CustomerDataImportController ac = fxmlLoader.getController();
		        ac.setDialogStage(dialogStage);
		        
		        dialogStage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		fileChooser = new FileChooser();
		fileName.setPromptText("Click here to select a file");
		
		fileName.setOnMouseClicked(e -> {
			file = fileChooser.showOpenDialog(rootNode);
			  if(file != null) {
				  fileName.setText(file.getAbsolutePath());
	        }
        });

		createTask();
	}
	
	public void setDialogStage(Stage stage) {
		this.dialogStage=stage;
	}
	
	public Stage getDialogStage() {
		return this.dialogStage;
	}

	public Task<Void> getTask() {
		return task;
	}

	public void setTask(Task<Void> task) {
		this.task = task;
	}

}

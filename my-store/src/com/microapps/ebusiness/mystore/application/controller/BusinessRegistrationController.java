package com.microapps.ebusiness.mystore.application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.domain.Registration;
import com.microapps.ebusiness.mystore.application.exception.RegistrationFailedException;
import com.microapps.ebusiness.mystore.application.service.RegistrationService;
import com.microapps.ebusiness.mystore.application.util.UIValidationUtils;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BusinessRegistrationController extends BaseController implements Initializable, Routeable {

	private Parent rootNode;
	
	@FXML
	private TextField ownerName;
	
	@FXML
	private TextField companyName;
	
	@FXML
	private TextArea companyAddress;
	
	@FXML
	private TextField activationCode;
	
	@FXML
	private Label activationErrorMsg; 
	
	private RegistrationService regService;
	
	@FXML
	private Button registerAndActivateButton;
	
	@FXML
	private Button resetButton;
	
	@FXML
	private void handleRegistrationAndActivation(ActionEvent event) {
		
	}
	
	@FXML
	private Label nameError;
	
	@FXML
	private Label businessNameError;
	
	@FXML
	private Label activationCodeError;
	
	@FXML
	private void handleReset(ActionEvent event) {
		ownerName.setText("");;
		companyName.setText("");
		companyAddress.setText("");
		activationCode.setText("");
	}
	
	private boolean hasValidationError;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		super.initialize(location, resources);
		
		companyAddress.setPromptText("Enter company address");
		
		regService = new RegistrationService();
		
		registerAndActivateButton.setOnAction(e -> {
			registerAndActivateButton.setDisable(true);
			resetButton.setDisable(true);
			try {
				regService.registerAndActivateBusiness(new Registration(ownerName.getText(), companyName.getText(), companyAddress.getText(), activationCode.getText()));
				showSuccessAlert();
				registerUser(e);
			} catch (RegistrationFailedException e1) {
				registerAndActivateButton.setDisable(false);
				resetButton.setDisable(false);
				
				 hasValidationError = true;
           	  	activationCodeError.setVisible(true);
           	  activationCodeError.setTextFill(Color.web("#eb3144"));
           	  activationCodeError.setText(e1.getMessage());
				  registerAndActivateButton.setDisable(true);
			}
		});
		
		resetButton.setOnAction(e -> {
			ownerName.setText("");
			companyName.setText("");
			companyAddress.setText("");
			activationCode.setText("");
			activationCodeError.setVisible(false);
			businessNameError.setVisible(false);
			nameError.setVisible(false);
		});
		
		configureNameFieldValidation();
		configureBusinessNameFieldValidation();
		configureActivationCodeFieldValidation();
		
	}
	
	private void registerUser(ActionEvent e) {
		Router.getRouter().route("register-user").showView(getStage(e));		
	}
	
	private void configureNameFieldValidation() {
		ownerName.setPromptText("Enter your name");
		ownerName.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (ownerName.getText().length() > UIValidationUtils.MAX_LENGTH_OF_CUSTOMER_NAME) {
	                String s = ownerName.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_CUSTOMER_NAME);
	                ownerName.setText(s);
	            }
	            if(hasValidationError) {
	            	 nameError.setTextFill(Color.web("#948282"));
    				 nameError.setText("");
    				 nameError.setVisible(false);
    				 registerAndActivateButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	registerAndActivateButton.setDisable(false);
	            }
	        }
	    });
		
		ownerName.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            if(!ownerName.getText().matches(UIValidationUtils.REGEX_FOR_CUSTOMER_NAME)){
	            	  hasValidationError = true;
	            	  nameError.setVisible(true);
	            	  nameError.setTextFill(Color.web("#eb3144"));
    				  nameError.setText(UIValidationUtils.NAME_ERROR);
    				  registerAndActivateButton.setDisable(true);
	            }else {
	            	registerAndActivateButton.setDisable(false);
	            }
	        }

	    });
	}

	private void configureBusinessNameFieldValidation() {
		companyName.setPromptText("Enter company name");
		companyName.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (companyName.getText().length() > UIValidationUtils.MAX_LENGTH_OF_CUSTOMER_NAME) {
	                String s = companyName.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_CUSTOMER_NAME);
	                companyName.setText(s);
	            }
	            if(hasValidationError) {
	            	 businessNameError.setTextFill(Color.web("#948282"));
	            	 businessNameError.setText("");
	            	 businessNameError.setVisible(false);
    				 registerAndActivateButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	registerAndActivateButton.setDisable(false);
	            }
	        }
	    });
		
		companyName.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            if(!companyName.getText().matches(UIValidationUtils.REGEX_FOR_CUSTOMER_NAME)){
	            	  hasValidationError = true;
	            	  businessNameError.setVisible(true);
	            	  businessNameError.setTextFill(Color.web("#eb3144"));
	            	  businessNameError.setText(UIValidationUtils.NAME_ERROR);
    				  registerAndActivateButton.setDisable(true);
	            }else {
	            	registerAndActivateButton.setDisable(false);
	            }
	        }

	    });
	
	}

	private void configureActivationCodeFieldValidation() {

		activationCode.setPromptText("Enter activation code");
		
		activationCode.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (activationCode.getText().length() > UIValidationUtils.MAX_LENGTH_OF_CUSTOMER_NAME) {
	                String s = activationCode.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_CUSTOMER_NAME);
	                activationCode.setText(s);
	            }
	            if(hasValidationError) {
	            	activationCodeError.setTextFill(Color.web("#948282"));
	            	activationCodeError.setText("");
	            	activationCodeError.setVisible(false);
    				 registerAndActivateButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	registerAndActivateButton.setDisable(false);
	            }
	        }
	    });
		
		activationCode.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	           /* if(!activationCode.getText().matches(UIValidationUtils.REGEX_FOR_NUMBER)){
	            	  hasValidationError = true;
	            	  activationCodeError.setVisible(true);
	            	  activationCodeError.setTextFill(Color.web("#eb3144"));
	            	  activationCodeError.setText(UIValidationUtils.ACT_CODE_ERROR);
    				  registerAndActivateButton.setDisable(true);
	            }else {
	            	registerAndActivateButton.setDisable(false);
	            }*/
	        }

	    });
	
	
	}

	private Stage stage;

	private void showSuccessAlert() {
		    Alert alert = new Alert(AlertType.INFORMATION);
	        alert.initOwner(stage);
	        alert.setTitle("Registration - Success");
	        alert.setHeaderText(null);
	        alert.setContentText("Registered successfully!\nClick OK to continue...");
	        
	        alert.showAndWait();
	}

	@Override
	public void showView(Stage stage) {
		this.stage=stage;
		FXMLLoader fxmlLoader = new FXMLLoader(BusinessRegistrationController.class.getResource(ViewTemplateConstants.BUSINESS_REGISTRATION_VIEW));
        try {
			rootNode = fxmlLoader.load();
			stage.setScene(new Scene(rootNode));
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}

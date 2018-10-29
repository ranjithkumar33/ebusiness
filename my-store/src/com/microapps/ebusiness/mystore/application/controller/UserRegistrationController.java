package com.microapps.ebusiness.mystore.application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UserRegistrationController extends BaseController implements Initializable, Routeable {

	private Parent rootNode;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField userName;
	
	
	@FXML
	private PasswordField password;
	
	@FXML
	private PasswordField confirmPassword;
	
	
	private RegistrationService regService;
	
	@FXML
	private Button registerButton;
	
	@FXML
	private Button resetButton;
	
	@FXML
	private void handleRegistrationAndActivation(ActionEvent event) {
		
	}
	
	@FXML
	private Label nameError;
	
	@FXML
	private Label userNameError;
	
	
	@FXML
	private Label passwordError;
	
	@FXML
	private Label confirmPasswordError;
	
	
	@FXML
	private void handleReset(ActionEvent event) {
		name.setText("");;
		userName.setText("");
		password.setText("");
		confirmPassword.setText("");
	}
	
	private boolean hasValidationError;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		super.initialize(location, resources);
		
		regService = new RegistrationService();
		
		registerButton.setOnAction(e -> {
			registerButton.setDisable(true);
			resetButton.setDisable(true);
			
				
				try {
					regService.registerUser(name.getText(), userName.getText(), password.getText());
					showSuccessAlert();
					
					login(e);
				} catch (RegistrationFailedException e1) {
					  hasValidationError = true;
					  userNameError.setVisible(true);
					  userNameError.setTextFill(Color.web("#eb3144"));
					  userNameError.setText(UIValidationUtils.DUPLICATE_USERNAME);
					  registerButton.setDisable(false);
					  resetButton.setDisable(false);
				}
				
		});
		
		resetButton.setOnAction(e -> {
			name.setText("");
			userName.setText("");
			password.setText("");
			confirmPassword.setText("");
			nameError.setVisible(false);
			userNameError.setVisible(false);
			passwordError.setVisible(false);
			confirmPasswordError.setVisible(false);
		});
		
		configureNameFieldValidation();
		configureUserNameFieldValidation();
		configurePasswordFieldValidation();
		configureConfirmPasswordFieldValidation();
	}
	
	private void login(ActionEvent event) {
		Router.getRouter().route("login").showView(getStage(event));
	}

	private void configureConfirmPasswordFieldValidation() {
		confirmPassword.setPromptText("Enter password");
		confirmPassword.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (confirmPassword.getText().length() > UIValidationUtils.MAX_LENGTH_OF_PASSWORD) {
	                String s = confirmPassword.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_PASSWORD);
	                confirmPassword.setText(s);
	            }
	            if(hasValidationError) {
	            	confirmPasswordError.setTextFill(Color.web("#948282"));
	            	confirmPasswordError.setText("");
	            	confirmPasswordError.setVisible(false);
    				 registerButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	registerButton.setDisable(false);
	            }
	        }
	    });
		
		confirmPassword.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            if(!confirmPassword.getText().equals(password.getText())){
	            	  hasValidationError = true;
	            	  confirmPasswordError.setVisible(true);
	            	  confirmPasswordError.setTextFill(Color.web("#eb3144"));
	            	  confirmPasswordError.setText(UIValidationUtils.CONFIRM_PASSWORD_MISMATCH_ERROR);
    				  registerButton.setDisable(true);
	            }else {
	            	registerButton.setDisable(false);
	            }
	        }

	    });
	
	
	}

	private void configurePasswordFieldValidation() {

		password.setPromptText("Enter password");
		password.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (password.getText().length() > UIValidationUtils.MAX_LENGTH_OF_PASSWORD) {
	                String s = password.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_PASSWORD);
	                password.setText(s);
	            }
	            if(hasValidationError) {
	            	passwordError.setTextFill(Color.web("#948282"));
	            	passwordError.setText("");
	            	passwordError.setVisible(false);
    				 registerButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	registerButton.setDisable(false);
	            }
	        }
	    });
		
		password.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	           /* if(!password.getText().matches(UIValidationUtils.REGEX_FOR_CUSTOMER_NAME)){
	            	  hasValidationError = true;
	            	  passwordError.setVisible(true);
	            	  passwordError.setTextFill(Color.web("#eb3144"));
	            	  passwordError.setText(UIValidationUtils.NAME_ERROR);
    				  registerButton.setDisable(true);
	            }else {
	            	registerButton.setDisable(false);
	            }*/
	        }

	    });
	
	}

	private void configureNameFieldValidation() {
		name.setPromptText("Enter name");
		name.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (name.getText().length() > UIValidationUtils.MAX_LENGTH_OF_CUSTOMER_NAME) {
	                String s = name.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_CUSTOMER_NAME);
	                name.setText(s);
	            }
	            if(hasValidationError) {
	            	 nameError.setTextFill(Color.web("#948282"));
    				 nameError.setText("");
    				 nameError.setVisible(false);
    				 registerButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	registerButton.setDisable(false);
	            }
	        }
	    });
		
		name.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            if(!name.getText().matches(UIValidationUtils.REGEX_FOR_CUSTOMER_NAME)){
	            	  hasValidationError = true;
	            	  nameError.setVisible(true);
	            	  nameError.setTextFill(Color.web("#eb3144"));
    				  nameError.setText(UIValidationUtils.NAME_ERROR);
    				  registerButton.setDisable(true);
	            }else {
	            	registerButton.setDisable(false);
	            }
	        }

	    });
	}

	private void configureUserNameFieldValidation() {
		userName.setPromptText("Enter username");
		userName.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (userName.getText().length() > UIValidationUtils.MAX_LENGTH_OF_USER_NAME) {
	                String s = userName.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_USER_NAME);
	                userName.setText(s);
	            }
	            if(hasValidationError) {
	            	userNameError.setTextFill(Color.web("#948282"));
	            	userNameError.setText("");
	            	userNameError.setVisible(false);
    				 registerButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	registerButton.setDisable(false);
	            }
	        }
	    });
		
		userName.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            /*if(!userName.getText().matches(UIValidationUtils.REGEX_FOR_CUSTOMER_NAME)){
	            	  hasValidationError = true;
	            	  userNameError.setVisible(true);
	            	  userNameError.setTextFill(Color.web("#eb3144"));
	            	  userNameError.setText(UIValidationUtils.NAME_ERROR);
    				  registerButton.setDisable(true);
	            }else {
	            	registerButton.setDisable(false);
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
	        alert.setContentText("User registered successfully!\nClick OK to continue...");
	        
	        alert.showAndWait();
	}

	@Override
	public void showView(Stage stage) {
		this.stage=stage;
		FXMLLoader fxmlLoader = new FXMLLoader(UserRegistrationController.class.getResource(ViewTemplateConstants.USER_REGISTRATION_VIEW));
        try {
			rootNode = fxmlLoader.load();
			stage.setScene(new Scene(rootNode));
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}

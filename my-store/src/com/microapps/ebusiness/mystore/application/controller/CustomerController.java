package com.microapps.ebusiness.mystore.application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.microapps.ebusiness.mystore.application.dao.exception.DuplicateEntryException;
import com.microapps.ebusiness.mystore.application.dao.exception.DuplicateEntryField;
import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.exception.ValidationFailedException;
import com.microapps.ebusiness.mystore.application.service.CustomerService;
import com.microapps.ebusiness.mystore.application.service.SecurityContext;
import com.microapps.ebusiness.mystore.application.service.ValidationUtil;
import com.microapps.ebusiness.mystore.application.util.DateConvertor;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CustomerController extends BaseController implements Initializable, Routeable{
	
	private static final Logger LOGGER = Logger.getLogger(CustomerController.class.getName());

	private Parent rootNode;
	
	private CustomerService cs;
	
	public CustomerController() {
		cs = new CustomerService();
	}
	
	@FXML
	private Label userName;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField mobile;
	
	@FXML
	private TextField email;
	
	@FXML
	private TextField cardNumber;
	
	@FXML
	private DatePicker dob;
	
	@FXML
	private RadioButton male;
	
	@FXML
	private RadioButton female;
	
	private ToggleGroup radioGroup;
	
	@FXML
	private Button saveButton;
	
	@FXML
	private Label nameError;
	
	@FXML
	private Label mobileError;
	
	@FXML
	private Label emailError;
	
	@FXML
	private Label dobError;
	
	@FXML
	private Label genderError;
	
	@FXML
	private Label cardError;
	
	private boolean hasValidationError;
	
	
	@FXML
	private void handleLogout(ActionEvent event) {
		SecurityContext.getSecurityContext().removeUserFromContext();
		Router.getRouter().route("logout").showView(getStage(event));
	}
	
	@FXML
	private void goHome(ActionEvent event) {
		Router.getRouter().route("home").showView(getStage(event));
	}
	
	@FXML
	private void save(ActionEvent event) {
		RadioButton selectedGender =
		        (RadioButton) radioGroup.getSelectedToggle();
		
		if(selectedGender != null) {
			if(hasValidationError) {
            	genderError.setText("");
            	genderError.setVisible(false);
				 saveButton.setDisable(false);
				 hasValidationError=!hasValidationError;
            }
		}else {
			if(!hasValidationError) {
				genderError.setVisible(true);
				genderError.setTextFill(Color.web("#eb3144"));
				genderError.setText(UIValidationUtils.GENDER_ERROR);
				     saveButton.setDisable(true);
				 hasValidationError=!hasValidationError;
           }
		}
		
		if(null == selectedGender) {
			return;	
		}
		
		
		char gender = selectedGender.getText().charAt(0);
		
		LocalDate date = dob.getValue();
		
			try {
				
				CustomerDto c = new CustomerDto(name.getText(), mobile.getText(), email.getText(), cardNumber.getText(), gender, Date.valueOf(date));
				if(ValidationUtil.validate(c)) {
					
					cs.saveCustomer(c);
					
					Router.getRouter().route("view-customer").showView(getStage(event));
				}
			
				
			} catch (DuplicateEntryException | NullPointerException e) {
				if(e instanceof DuplicateEntryException) {
					DuplicateEntryField def = DuplicateEntryField.findFieldByKey(((DuplicateEntryException) e).getKey());
					switch(def) {
					  case mobile:
						  hasValidationError = true;
		            	  mobileError.setVisible(true);
		            	  mobileError.setTextFill(Color.web("#eb3144"));
		            	  mobileError.setText(UIValidationUtils.DUPLICATE_MOBILE_NUMBER_ERROR);
						break;
					  case cardNumber:
						  hasValidationError = true;
		            	  cardError.setVisible(true);
		            	  cardError.setTextFill(Color.web("#eb3144"));
		            	  cardError.setText(UIValidationUtils.DUPLICATE_CARD_NUMBER_ERROR);
						break;
					}
				}else if(e instanceof NullPointerException) {
					LOGGER.log(Level.SEVERE, "No values entered!");
				}
				
			}catch (ValidationFailedException e) {
				 LOGGER.log(Level.SEVERE, "No values entered!", e.getMessage());
				  hasValidationError = true;
				  saveButton.setDisable(true);
			}
			
	
	}
	
	
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		userName.setText(SecurityContext.getSecurityContext().getAuthenticatedUser().getName());
		radioGroup = new ToggleGroup();
		male.setToggleGroup(radioGroup);
		female.setToggleGroup(radioGroup);
		dob.setPromptText(DateConvertor.PATTERN.toLowerCase());
		dob.setConverter(new DateConvertor());

		
		configureNameFieldValidation();
		configureMobileFieldValidation();
		configureEmailFieldValidation();
		configureDoBFieldValidation();
		configureGenderFieldValidation();
		configureCardFieldValidation();
	}
	
	private void configureCardFieldValidation() {
		cardNumber.setPromptText("Enter card number");
		cardNumber.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (cardNumber.getText().length() > UIValidationUtils.MAX_LENGTH_OF_CARD_NUMBER) {
	                String s = cardNumber.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_CARD_NUMBER);
	                cardNumber.setText(s);
	            }
	            if(hasValidationError) {
	            	cardError.setText("");
	            	cardError.setVisible(false);
    				 saveButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	saveButton.setDisable(false);
	            }
	        }
	    });
		
		cardNumber.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            if(!cardNumber.getText().matches(UIValidationUtils.REGEX_FOR_CARD_NUMBER)){
	            	  hasValidationError = true;
	            	  cardError.setVisible(true);
	            	  cardError.setTextFill(Color.web("#eb3144"));
	            	  cardError.setText(UIValidationUtils.CARD_NUMBER_ERROR);
    				  saveButton.setDisable(true);
	            }else {
	            	saveButton.setDisable(false);
	            }
	        }

	    });
	}

	private void configureGenderFieldValidation() {
		
		radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		          Toggle old_toggle, Toggle new_toggle) {
		        if (radioGroup.getSelectedToggle() != null) {
		        	if(hasValidationError) {
		            	genderError.setText("");
		            	genderError.setVisible(false);
						 saveButton.setDisable(false);
						 hasValidationError=!hasValidationError;
		            }
		        }else {
		        	if(!hasValidationError) {
						genderError.setVisible(true);
						genderError.setTextFill(Color.web("#eb3144"));
						genderError.setText(UIValidationUtils.GENDER_ERROR);
						     saveButton.setDisable(true);
		           }
		        }
		      }
		 });
	}

	private void configureDoBFieldValidation() {
		
		dob.valueProperty().addListener((ov, oldValue, newValue) -> {
			
			if(!newValue.isAfter(LocalDate.now())) {
				if(hasValidationError) {
	            	dobError.setText("");
	            	dobError.setVisible(false);
					 saveButton.setDisable(false);
					 hasValidationError=!hasValidationError;
	            }
			}else {
				if(!hasValidationError) {
					 dobError.setVisible(true);
	            	  dobError.setTextFill(Color.web("#eb3144"));
	            	  dobError.setText(UIValidationUtils.DATE_ERROR);
   				     saveButton.setDisable(true);
	            }
			}
				
	    });
		
		dob.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            if(dob == null || dob.getValue()==null || dob.getValue().isAfter(LocalDate.now())){
	            	  hasValidationError = true;
	            	  dobError.setVisible(true);
	            	  dobError.setTextFill(Color.web("#eb3144"));
	            	  dobError.setText(UIValidationUtils.DATE_ERROR);
    				  saveButton.setDisable(true);
	            }else {
	            	saveButton.setDisable(false);
	            }
	        }

	    });
	
	}

	private void configureEmailFieldValidation() {
		email.setPromptText("Enter email");
		email.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (email.getText().length() > UIValidationUtils.MAX_LENGTH_OF_EMAIL) {
	                String s = email.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_EMAIL);
	                email.setText(s);
	            }
	            if(hasValidationError) {
	            	emailError.setText("");
	            	emailError.setVisible(false);
    				 saveButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	saveButton.setDisable(false);
	            }
	        }
	    });
		
		email.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            if(!email.getText().matches(UIValidationUtils.REGEX_FOR_EMAIL)){
	            	  hasValidationError = true;
	            	  emailError.setVisible(true);
	            	  emailError.setTextFill(Color.web("#eb3144"));
	            	  emailError.setText(UIValidationUtils.EMAIL_ERROR);
    				  saveButton.setDisable(true);
	            }else {
	            	saveButton.setDisable(false);
	            }
	        }

	    });
	}

	private void configureMobileFieldValidation() {
		mobile.setPromptText("Enter mobile number");
		mobile.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (mobile.getText().length() > UIValidationUtils.MAX_LENGTH_OF_MOBILE_NUMBER) {
	                String s = mobile.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_MOBILE_NUMBER);
	                mobile.setText(s);
	            }
	            if(hasValidationError) {
	            	 mobileError.setText("");
	            	 mobileError.setVisible(false);
    				 saveButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	saveButton.setDisable(false);
	            }
	        }
	    });
		
		mobile.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            if(!mobile.getText().matches(UIValidationUtils.REGEX_FOR_MOBBILE_NUMBER)){
	            	  hasValidationError = true;
	            	  mobileError.setVisible(true);
	            	  mobileError.setTextFill(Color.web("#eb3144"));
	            	  mobileError.setText(UIValidationUtils.MOBILE_NUMBER_ERROR);
    				  saveButton.setDisable(true);
	            }else {
	            	saveButton.setDisable(false);
	            }
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
    				 saveButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	saveButton.setDisable(false);
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
    				  saveButton.setDisable(true);
	            }else {
	            	saveButton.setDisable(false);
	            }
	        }

	    });
	}

	@Override
	public void showView(Stage stage) {
		FXMLLoader fxmlLoader = new FXMLLoader(CustomerController.class.getResource(ViewTemplateConstants.NEW_CUSTOMER_VIEW));
        try {
			rootNode = fxmlLoader.load();
			stage.setScene(new Scene(rootNode));
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

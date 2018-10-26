package com.microapps.ebusiness.mystore.application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.service.LoginService;
import com.microapps.ebusiness.mystore.application.util.UIValidationUtils;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController extends BaseController implements Initializable, Routeable {

	private Parent rootNode;
	
	@FXML
	private TextField userName;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Label loginErrorMsg; 
	
	private LoginService loginService;
	
	@FXML
	private Button loginButton;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private void handleLogin(ActionEvent event) {
		if(loginService.login(userName.getText(), password.getText())) {
			System.out.println("SUCCESS");
			 Node source = (Node) event.getSource();
		     Window theStage = source.getScene().getWindow();

			Router.getRouter().route("home").showView(((Stage)theStage));
			
		}else {
			System.out.println("FAILED");
			loginErrorMsg.setText(UIValidationUtils.LOGIN_ERROR);
		}
	}
	
	@FXML
	private void handleCancel(ActionEvent event) {
		userName.setText("");;
		password.setText("");
		loginErrorMsg.setText("");
	}
	
	
	@FXML
	private void createUser(ActionEvent event) {
		Router.getRouter().route("register-user").showView(getStage(event));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		super.initialize(location, resources);
		
		userName.setPromptText("Enter Username");
		password.setPromptText("Enter Password");
		
		loginService = new LoginService();
		
		loginButton.setOnAction(e -> {
			handleLogin(e);
		});
		
		cancelButton.setOnAction(e -> {
			handleCancel(e);
		});
		
		
		
	}

	@Override
	public void showView(Stage stage) {
		FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource(ViewTemplateConstants.LOGIN_VIEW));
        try {
			rootNode = fxmlLoader.load();
			stage.setScene(new Scene(rootNode));
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}

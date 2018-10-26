package com.microapps.ebusiness.mystore.application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.domain.ActivityDto;
import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.exception.SettingNotFoundException;
import com.microapps.ebusiness.mystore.application.service.ActivityService;
import com.microapps.ebusiness.mystore.application.service.ItemGroupService;
import com.microapps.ebusiness.mystore.application.service.Session;
import com.microapps.ebusiness.mystore.application.util.ActivityType;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ActivityController extends BaseController implements Initializable, Routeable {

	private Parent rootNode;

	@FXML
	private ComboBox<String> activityType;
	
	@FXML
	private ComboBox<String> itemGroup;
	
	@FXML
	private TextField amount;

	@FXML
	private Label redeemablePoints;

	@FXML
	private Label discoutableAmount;

	private Stage dialogStage;
	
	private ActivityService as;
	
	private ItemGroupService is;
	
	@FXML
	private void redeemPoints() {
		// Show the error message.
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Loyalty points redemption");
        alert.setHeaderText("Please confirm if you want to proceed with points redemption");
        alert.setContentText("Click OK to confirm");
        
        alert.showAndWait();
	}
	
	@FXML
	private void handleCancel() {
		this.dialogStage.close();
	}

	@FXML
	private void handleOk() {
		String selectedActivity = activityType.getValue();
		
		String selectedItemGroup = itemGroup.getValue();
		
		try {
			as.saveActivity(new ActivityDto(selectedActivity, selectedItemGroup, Double.parseDouble(amount.getText())));
		} catch (NumberFormatException | SettingNotFoundException e) {
			if(e instanceof NumberFormatException) {
				showErrorMessage("Invalid amount");
			}else if(e instanceof SettingNotFoundException) {
				showErrorMessage("The Application settings is not done!\\nThis is required to calculate the loyalty points.\\nPlease go to Menu Settings->App settings");
			}
			
		}
		
		this.dialogStage.close();
	}
	
	private void setDialogStage(Stage stage) {
		this.dialogStage=stage;
	}
	
	public Stage getDialogStage() {
		return this.dialogStage;
	}
	
	@Override
	public void showView(Stage stage) {
		Stage dialogStage = new Stage();
		
		FXMLLoader fxmlLoader = new FXMLLoader(ActivityController.class.getResource(ViewTemplateConstants.ADD_ACTIVITY_VIEW));
		try {
			rootNode = fxmlLoader.load();
			
			
			dialogStage.setTitle("Add activity");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(stage);
	        Scene scene = new Scene(rootNode);
	        dialogStage.setScene(scene);
	        
	        ActivityController ac = fxmlLoader.getController();
	        ac.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		as = new ActivityService();
		is = new ItemGroupService();
		
		amount.setPromptText("Enter amount");
		
		for(ActivityType at : ActivityType.getAllActivityTypes()) {
			activityType.getItems().add(at.getName());
		}
		activityType.getSelectionModel().selectFirst();
		
		for(String i : is.getAllItemGroups()) {
			itemGroup.getItems().add(i);
		}
		
		itemGroup.getSelectionModel().selectFirst();
		
		
		CustomerDto c= Session.getSession().getCustomerFromSession();
		setLoyaltyData(c);
		
	}

	private void setLoyaltyData(CustomerDto c) {
		long totalPoints = 0;
		
		if(c.getActivities() != null && !c.getActivities().isEmpty()) {
			for(ActivityDto a : c.getActivities()) {
				totalPoints = totalPoints + a.getEarnedPoints();
			}
		}
		//this.redeemablePoints.setText(totalPoints+"");
	}

}

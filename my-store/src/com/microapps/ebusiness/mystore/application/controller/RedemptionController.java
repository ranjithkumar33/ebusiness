package com.microapps.ebusiness.mystore.application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.domain.ActivityDto;
import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.domain.LoyaltyData;
import com.microapps.ebusiness.mystore.application.exception.SettingNotFoundException;
import com.microapps.ebusiness.mystore.application.service.ActivityService;
import com.microapps.ebusiness.mystore.application.service.LoyalyPointsService;
import com.microapps.ebusiness.mystore.application.service.Session;
import com.microapps.ebusiness.mystore.application.util.ActivityType;
import com.microapps.ebusiness.mystore.application.util.CurrencyUtil;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RedemptionController extends BaseController implements Initializable, Routeable{

	private Parent rootNode;
	
	private Stage dialogStage;
	
	@FXML
	private Label totalPointsAccrued;
	
	@FXML
	private Label eligiblePointsForRedemption;
	
	@FXML
	private Label pointsCarryForward;
	
	@FXML
	private Label equivalentAmountOfRedemption;
	
	private LoyalyPointsService ls;
	
	private ActivityService as;
	
	private double equivalentAmount;
	
	private long carryForwardPoints;
	
	private void setDialogStage(Stage stage) {
		this.dialogStage=stage;
	}
	
	public Stage getDialogStage() {
		return this.dialogStage;
	}
	
	@Override
	public void showView(Stage stage) {
		Stage dialogStage = new Stage();
		
		FXMLLoader fxmlLoader = new FXMLLoader(RedemptionController.class.getResource(ViewTemplateConstants.REDEEM_POINTS_VIEW));
		try {
			rootNode = fxmlLoader.load();
			
			dialogStage.setTitle("Redeem loyalty points");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(stage);
	        Scene scene = new Scene(rootNode);
	        dialogStage.setScene(scene);
	        
	        RedemptionController ac = fxmlLoader.getController();
	        ac.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		ls = new LoyalyPointsService();
		as = new ActivityService();
		setData();
	}
	
	private void setData() {
		CustomerDto c = Session.getSession().getCustomerFromSession();
		
		try {
			LoyaltyData ld = ls.computeLoyaltyData(c.getActivities());
			
			totalPointsAccrued.setText(ld.getEarnedPoints()+"");
			
			long eligiblePoints = ls.calculateEligiblePointsForRedemption();

			eligiblePointsForRedemption.setText(eligiblePoints+"");
			
			carryForwardPoints = ls.calculatePointsCarryForward(ld.getEarnedPoints());
			
			pointsCarryForward.setText(carryForwardPoints+"");
			
			equivalentAmount = ls.calculateEquivalentAmountForRedemption(eligiblePoints);
			
			equivalentAmountOfRedemption.setText(CurrencyUtil.getFormattedAmount(equivalentAmount));
			
		} catch (SettingNotFoundException e1) {
			showErrorMessage("The Application settings is not done!\nThis is required to calculate the loyalty points.\nPlease go to Menu Settings->App settings");
		}
	}

	@FXML
	private void handleCancel() {
		this.dialogStage.close();
	}

	@FXML
	private void handleRedeem() {
		try {
			ActivityDto a = new ActivityDto(ActivityType.REDEMPTION.getName(), "", equivalentAmount);
			a.setEarnedPoints(carryForwardPoints);
			as.saveActivity(a);
		} catch (SettingNotFoundException e) {
			showErrorMessage("The Application settings is not done!\\nThis is required to calculate the loyalty points.\\nPlease go to Menu Settings->App settings");
		}
		
		this.dialogStage.close();
	}
	

}

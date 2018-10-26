package com.microapps.ebusiness.mystore.application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.domain.AppSettingsDto;
import com.microapps.ebusiness.mystore.application.exception.SettingNotFoundException;
import com.microapps.ebusiness.mystore.application.service.GeneralService;
import com.microapps.ebusiness.mystore.application.util.LoyaltyPointFactors;
import com.microapps.ebusiness.mystore.application.util.UIValidationUtils;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AppSettingsController extends BaseController implements Initializable, Routeable{

	
	private Parent rootNode;
	
	@FXML
	private ComboBox<String> moneyToPointsRatio;
	
	@FXML
	private TextField redemptionThreshold;
	
	@FXML
	private ComboBox<String> pointsToMoneyRatio;
	
	@FXML
	private Button okButton;
	
	private Stage dialogStage;
	
	private AppSettingsDto as;
	
	private void setDialogStage(Stage stage) {
		this.dialogStage=stage;
	}
	
	public Stage getDialogStage() {
		return this.dialogStage;
	}

	
	@FXML
	private void handleCancel() {
		this.dialogStage.close();
	}
	
	@FXML
	private Label rtpError;
	
	private boolean hasValidationError;
	
	private GeneralService gs;

	@FXML
	private void handleOk() {
		String mpf = moneyToPointsRatio.getValue();
		String pmf = pointsToMoneyRatio.getValue();
		String rtp = redemptionThreshold.getText();
		int rtpInt = Integer.parseInt(rtp);
		
		this.as.setMpf(LoyaltyPointFactors.getRatioByViewText(mpf).getFactor());
		this.as.setPmf(LoyaltyPointFactors.getRatioByViewText(pmf).getFactor());
		this.as.setRtp(rtpInt);
		
			gs.saveSettings(this.as);
			this.dialogStage.close();
		
	}
	
	private void configureRTPFieldValidation() {
		redemptionThreshold.setPromptText("Enter threshold points");
		redemptionThreshold.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (redemptionThreshold.getText().length() > UIValidationUtils.MAX_LENGTH_OF_LOYALTY_POINTS) {
	                String s = redemptionThreshold.getText().substring(0, UIValidationUtils.MAX_LENGTH_OF_LOYALTY_POINTS);
	                redemptionThreshold.setText(s);
	            }
	            if(hasValidationError) {
	            	//rtpError.setText("");
	            	//rtpError.setVisible(false);
	            	okButton.setDisable(true);
    				 hasValidationError=!hasValidationError;
	            }else {
	            	okButton.setDisable(false);
	            }
	        }
	    });
		
		redemptionThreshold.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { //when focus lost
	            if(!redemptionThreshold.getText().matches(UIValidationUtils.REGEX_FOR_NUMBER)){
	            	  hasValidationError = true;
	            	  okButton.setDisable(true);
	            }else {
	            	okButton.setDisable(false);
	            }
	        }

	    });
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		gs = new GeneralService();

		for(LoyaltyPointFactors at : LoyaltyPointFactors.getAllRatios()) {
			moneyToPointsRatio.getItems().add(at.getViewText());
		}
		moneyToPointsRatio.getSelectionModel().selectFirst();
		

		for(LoyaltyPointFactors at : LoyaltyPointFactors.getAllRatios()) {
			pointsToMoneyRatio.getItems().add(at.getViewText());
		}
		pointsToMoneyRatio.getSelectionModel().selectFirst();
		
		configureRTPFieldValidation();
		
		setSettingsData();
		
	}
	
	private void setSettingsData() {
		
		try {
			this.as = gs.loadAppSettings();
			if(null != this.as) {
				pointsToMoneyRatio.getSelectionModel().select(LoyaltyPointFactors.getRatioByFactor(as.getPmf()).getViewText());
				moneyToPointsRatio.getSelectionModel().select(LoyaltyPointFactors.getRatioByFactor(as.getMpf()).getViewText());
				redemptionThreshold.setText(as.getRtp()+"");
			}
		} catch (SettingNotFoundException e) {
			this.as = new AppSettingsDto();
		}
	}

	@Override
	public void showView(Stage stage) {
		Stage dialogStage = new Stage();
		
		FXMLLoader fxmlLoader = new FXMLLoader(AppSettingsController.class.getResource(ViewTemplateConstants.APP_SETTINGS_VIEW));
		try {
			rootNode = fxmlLoader.load();
			
			
			dialogStage.setTitle("App settings");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(stage);
	        Scene scene = new Scene(rootNode);
	        dialogStage.setScene(scene);
	        
	        AppSettingsController ac = fxmlLoader.getController();
	        ac.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

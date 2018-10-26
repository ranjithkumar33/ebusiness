package com.microapps.ebusiness.mystore.application.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.domain.CustomerDto;
import com.microapps.ebusiness.mystore.application.domain.LoyaltyData;
import com.microapps.ebusiness.mystore.application.exception.SettingNotFoundException;
import com.microapps.ebusiness.mystore.application.service.LoyalyPointsService;
import com.microapps.ebusiness.mystore.application.service.SecurityContext;
import com.microapps.ebusiness.mystore.application.service.Session;
import com.microapps.ebusiness.mystore.application.util.CustomerNameUtils;
import com.microapps.ebusiness.mystore.application.util.DateConvertor;
import com.microapps.ebusiness.mystore.application.util.DateUtil;
import com.microapps.ebusiness.mystore.application.util.Gender;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;
import com.microapps.ebusiness.mystore.application.view.ActivityView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CustomerDetailsController extends BaseController implements Initializable, Routeable{

	private Parent rootNode;
	
	@FXML
	private Label userName;
	
	@FXML
	private Label name;
	
	@FXML
	private Label mobile;
	
	@FXML
	private Label email;
	
	@FXML
	private Label cardNumber;
	
	@FXML
	private Label gender;
	
	@FXML
	private Label dob;
	
	@FXML
    private TableView<ActivityView> activityTable;
	
    @FXML
    private TableColumn<ActivityView, LocalDateTime> dateColumn;
    
    @FXML
    private TableColumn<ActivityView, String> activityTypeColumn;
    
    @FXML
    private TableColumn<ActivityView, String> itemGroupColumn;

    
    @FXML
    private TableColumn<ActivityView, Double> amountColumn;
    
    private ObservableList<ActivityView> activities = FXCollections.observableArrayList();
    
    @FXML
    private Label lpAccrued;
    
    @FXML
    private Label lpRequiredForRedemption;
    
    @FXML
    private Label equivalentAmountForRedemption;
    
    @FXML
	private Label customerClassIcon;
    
    @FXML
    private Hyperlink redeemNowLink;
    
    private LoyalyPointsService ls;
    
    @FXML
	private void redeemLoyaltyPointsNow(ActionEvent event) {
    	showLoyaltyPointsRedemptionDialog(event);
	}
	
	@FXML
	private void handleLogout(ActionEvent event) {
		SecurityContext.getSecurityContext().removeUserFromContext();
		Router.getRouter().route("logout").showView(getStage(event));
	}
	
	@Override
	public void showView(Stage stage) {
		FXMLLoader fxmlLoader = new FXMLLoader(CustomerDetailsController.class.getResource(ViewTemplateConstants.CUSTOMER_DETAILS_VIEW));
        try {
			rootNode = fxmlLoader.load();
			stage.setScene(new Scene(rootNode));
			CustomerDetailsController hc = fxmlLoader.getController();
			hc.setCustomerData();
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		ls = new LoyalyPointsService();
		
		Image image = new Image(BaseController.class.getResourceAsStream("my-shop-king-ico.png"));
		ImageView im = new ImageView(image);
		im.setFitWidth(40);
		im.setFitHeight(40);
		//customerClassIcon.setGraphic(im);
		
		userName.setText(SecurityContext.getSecurityContext().getAuthenticatedUser().getName());
		redeemNowLink.setVisible(false);
		
		setCustomerData();
	}
	
	public void setCustomerData() {
		CustomerDto c= Session.getSession().getCustomerFromSession();
		this.name.setText(CustomerNameUtils.getNameWithTitle(c));
		this.cardNumber.setText(c.getCardNumber());
		this.mobile.setText(c.getMobile());
		this.email.setText(c.getEmail());
		this.gender.setText(Gender.getGender(c.getGender()).toString());
		this.dob.setText(new DateConvertor().toString(c.getDob().toLocalDate()));
		boolean res = setLoyaltyData(c);
		if(!res) return;
		setActivityData(c);
	}

	private boolean setLoyaltyData(CustomerDto c) {
		try {
			LoyaltyData ld = ls.computeLoyaltyData(c.getActivities());
			
			this.lpAccrued.setText(ld.getEarnedPoints()+"");
			this.equivalentAmountForRedemption.setText(ld.getEqvAmtOfPointsNeededForRedemption()+" INR");
			this.lpRequiredForRedemption.setText(ld.getPointsNeededForRedemption()+"");
			
			if(ld.isMaturedForRedemption()) {
				redeemNowLink.setVisible(true);
			}else {
				redeemNowLink.setVisible(false);
			}
			
		} catch (SettingNotFoundException e) {
			showErrorMessage("The Application settings is not done!\nThis is required to calculate the loyalty points.\nPlease go to Menu Settings->App settings");
			return false;
		}
		return true;
	}


	private void setActivityData(CustomerDto c) {
		if(c.getActivities() == null || c.getActivities().isEmpty()) {
			return;
		}
		activities.clear();
		//activity table
		c.getActivities().forEach(a ->{
			activities.add(new ActivityView(a.getName(), a.getItemGroup(), a.getAmount(), a.getCreatedOn().toLocalDateTime()));
		});
		
		activityTable.setItems(activities);
		
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDate());
		activityTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
		itemGroupColumn.setCellValueFactory(cellData -> cellData.getValue().getItemGroup());
		amountColumn.setCellValueFactory(cellData -> cellData.getValue().getAmount().asObject());
		
		dateColumn.setCellFactory(column -> {
			return new TableCell<ActivityView, LocalDateTime>() {
		        @Override
		        protected void updateItem(LocalDateTime item, boolean empty) {
		        	
		        	if (item == null || empty) {
		        		setText("");
		            }else {
		            	setText(DateUtil.toString(item));
		            }
		        }
		    };
		});
	}
	
	@FXML
	private void goHome(ActionEvent event) {
		Router.getRouter().route("home").showView(getStage(event));
	}

	
	@FXML
	private void addActivity(ActionEvent event) {
		showActivityDialog(event);
	}
	
	private void showActivityDialog(ActionEvent event) {
		ActivityController ac = (ActivityController) Router.getRouter().route("add-activity");
		ac.showView(getStage(event));
		setLoyaltyData(Session.getSession().getCustomerFromSession());
		setActivityData(Session.getSession().getCustomerFromSession());
		
	}
	
	private void showLoyaltyPointsRedemptionDialog(ActionEvent event) {
		RedemptionController ac = (RedemptionController) Router.getRouter().route("redeem-points");
		ac.showView(getStage(event));
		setLoyaltyData(Session.getSession().getCustomerFromSession());
	}

	@FXML
	private void editCustomer(ActionEvent event) {
		Router.getRouter().route("edit-customer").showView(getStage(event));
	}
	
}

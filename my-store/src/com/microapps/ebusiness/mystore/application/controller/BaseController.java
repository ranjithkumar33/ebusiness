package com.microapps.ebusiness.mystore.application.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.microapps.ebusiness.mystore.application.exception.CSVParseException;
import com.microapps.ebusiness.mystore.application.service.CustomerService;
import com.microapps.ebusiness.mystore.application.service.ItemGroupService;
import com.microapps.ebusiness.mystore.application.util.CustomerDataCsvParcer;
import com.microapps.ebusiness.mystore.application.util.HostServiceUtil;
import com.microapps.ebusiness.mystore.application.util.ItemMasterDataCsvParcer;
import com.microapps.ebusiness.mystore.application.util.ViewTemplateConstants;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class BaseController{
	
	public BaseController() {
		cs = new CustomerService();
		is = new ItemGroupService();
	}
	
	@FXML
	private Label imageLabel;
	
	@FXML
	private MenuBar menuBar;
	
	private CustomerService cs;
	
	private ItemGroupService is;
	
	@FXML
	private void showHelp(ActionEvent event) {
		HostServiceUtil.getHostServices().showDocument(this.getClass().getResource("MyShop-v1.0.0-help.docx").toString());
	}
	
	
	@FXML
	private void showSalesReport(ActionEvent event) {
		SalesReportDlgController ac = (SalesReportDlgController) Router.getRouter().route("sales-report");
		ac.showView(null);
	}
	
	@FXML
	private void handleAppSettings(ActionEvent event) {
		showAppSettingsDialog(event);
	}
	
	private void showAppSettingsDialog(ActionEvent event) {
		AppSettingsController ac = (AppSettingsController) Router.getRouter().route("app-settings");
		ac.showView(null);
	}

	@FXML
	private void showAboutApp() {
		Alert help = new Alert(AlertType.INFORMATION);
		help.setContentText("App name : MyShop\nVersion: v1.0.0\nCopyright @2018. All rights reserved");
		help.setTitle("About MyShop");
		help.setHeaderText(null);
		help.showAndWait();
	}
	
	@FXML
	private void exitApp() {
		Platform.exit();
	}
	
	private static void open(Stage stage, CompletableFuture<File> future) {
	    Platform.runLater(() -> {
	        FileChooser fileChooser = new FileChooser();
	        future.complete(fileChooser.showSaveDialog(stage)); // fill future with result
	    });
	}
	
	
	@FXML
	private void exportCustomerDataCsv(ActionEvent event) {
		
		new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                CompletableFuture<File> future = new CompletableFuture<>();
                open((Stage)this.menuBar.getScene().getWindow(), future);
                try {
                    File file = future.get(); // wait for future to be assigned a result and retrieve it
                    System.out.println(file == null ? "no file chosen" : file.toString());
                    if(file != null) {
                    	writeToFile(file);
                    }
                    break;
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
		
	}
	
	
	@FXML
	private void importCustomerDataCsv(ActionEvent event) {
		showModalWithProgressBar(event);
	}
	
	private void showModalWithProgressBar(ActionEvent event) {
		Router.getRouter().route("data-import").showView((Stage)this.menuBar.getScene().getWindow());
	}


	@FXML
	private void importItemMasterDataCsv(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog((Stage)this.menuBar.getScene().getWindow());
		  if(file != null) {
			try {
				ItemMasterDataCsvParcer cp = new ItemMasterDataCsvParcer(file);
				cp.parseFile();
				is.itemMasterDataImport(cp.getItemGroups());
			} catch (CSVParseException e) {
				e.printStackTrace();
			}
			 
          }
	}

	private void writeToFile(File filePath) {
		
      /*  Writer out;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath), "UTF8"));
			out.append("name,mobile,card_num,gender,dob,email,joined_on,activity_amt,activity_date,activity_type,activity_amt,activity_date,activity_type,activity_amt,activity_date,activity_type,activity_amt,activity_date,activity_type,activity_amt,activity_date,activity_type\n");
			
			cs.getAllCustomersForReport().forEach(c->{
				try {
					out.append(c.getName()+","+c.getMobile()+","+c.getCardNumber()+","+c.getGender()+","+c.getDob()+","+c.getEmail()+","+c.getCreatedOn().toLocalDateTime()+",");
					c.getActivities().forEach(a->{
						try {
							out.append(a.getAmount()+","+a.getCreatedOn().toLocalDateTime()+","+a.getName()+",");
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
					out.append("\n");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			});
			
		    out.flush();
		    out.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}

	@FXML
	private void runBackUpScript() {
		
	}

	public void initialize(URL location, ResourceBundle resources) {
		
		Image image = new Image(BaseController.class.getResourceAsStream("my-shop-king-ico.png"));
		ImageView im = new ImageView(image);
		im.setFitWidth(40);
		im.setFitHeight(40);
		//imageLabel.setGraphic(im);
	}
	
	protected Stage getStage(ActionEvent event) {
		Node source = (Node) event.getSource();
	    Window theStage = source.getScene().getWindow();
	    return (Stage)theStage;
	}
	
	protected void showErrorMessage(String message) {
		// Show the error message.
        Alert error = new Alert(AlertType.ERROR);
        error.setContentText(message);
        error.setTitle("Error");
        error.setHeaderText(null);
        error.showAndWait();
	}
	
	protected void showConfirmationMessage(String message) {
		// Show the error message.
        Alert error = new Alert(AlertType.CONFIRMATION);
        error.setContentText(message);
        error.setTitle("Confirm");
        error.setHeaderText(null);
        error.showAndWait();
	}

}

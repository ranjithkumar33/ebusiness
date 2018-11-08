package com.microapps.ebusiness.mystore.application.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.service.ReportService;
import com.microapps.ebusiness.mystore.application.util.DateUtil;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ItemsSaleDlgController extends BaseController implements Initializable, Routeable{
	
	public ItemsSaleDlgController() {
		rs = new ReportService();
	}

	private Parent rootNode;
	
	private Stage dialogStage;
	
	private ReportService rs;
	
	private void setDialogStage(Stage stage) {
		this.dialogStage=stage;
	}
	
	public Stage getDialogStage() {
		return this.dialogStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
	

	@Override
	public void showView(Stage stage) {
		Stage dialogStage = new Stage();
		
		dialogStage.setTitle("Items Sale");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(stage);
      
		 final CategoryAxis xAxis = new CategoryAxis();
	    final NumberAxis yAxis = new NumberAxis();
	    xAxis.setLabel("Day");    
	    yAxis.setLabel("Sale");

	    final BarChart<String,Number> bc = 
	            new BarChart<String,Number>(xAxis,yAxis);
                
        bc.setTitle("Items Sale");
        
        List<ReportService.DailyItemSale> sd =  rs.getMonthlyItemsSalesData(0);
        
        if(sd != null) {
        	sd.forEach(e->{
        		 XYChart.Series series = new XYChart.Series<>();
        	     series.setName(e.getItem());
        	     series.getData().add(new XYChart.Data(DateUtil.toString(e.getDate()), e.getSale()));
        	     bc.getData().add(series);
        	});
        	
        }

		VBox vbox = new VBox(bc);
		Scene scene = new Scene(vbox, 400, 200);
		dialogStage.setScene(scene);
		

		dialogStage.setScene(scene);
		dialogStage.setHeight(300);
		dialogStage.setWidth(1200);

		dialogStage.showAndWait();
	}

}

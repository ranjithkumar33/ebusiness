package com.microapps.ebusiness.mystore.application.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.microapps.ebusiness.mystore.application.service.ReportService;
import com.microapps.ebusiness.mystore.application.util.DateUtil;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerChurnReportDlgController extends BaseController implements Initializable, Routeable{
	
	public CustomerChurnReportDlgController() {
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
		
		dialogStage.setTitle("Sales report");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(stage);
      
		 final CategoryAxis xAxis = new CategoryAxis();
	    final NumberAxis yAxis = new NumberAxis();
	    xAxis.setLabel("Day");    
	    yAxis.setLabel("Revenue");

	    final LineChart<String,Number> lineChart = 
                new LineChart<String,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Daily sales report");
        
        XYChart.Series series = new XYChart.Series();
        series.setName("October");
        
        List<ReportService.DailyRevenue> sd =  rs.getMonthlySalesData(0);
        
        if(sd != null) {
        	sd.forEach(r -> {
        		series.getData().add(new XYChart.Data(DateUtil.toString(r.getDate()), r.getSale()));
        	});
        }
        lineChart.getData().add(series);

		VBox vbox = new VBox(lineChart);
		Scene scene = new Scene(vbox, 400, 200);
		dialogStage.setScene(scene);
		

		dialogStage.setScene(scene);
		dialogStage.setHeight(300);
		dialogStage.setWidth(1200);

		dialogStage.showAndWait();
	}

}

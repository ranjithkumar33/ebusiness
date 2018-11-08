package com.microapps.ebusiness.mystore.application.controller;

import java.util.HashMap;
import java.util.Map;

public class Router {
	
	private Map<String, Routeable> contrls = new HashMap<>();
	
	private Router() {
		contrls.put("login", new LoginController());
		contrls.put("home", new HomeController());
		contrls.put("logout", new LoginController());
		contrls.put("new-customer", new CustomerController());
		contrls.put("view-customer", new CustomerDetailsController());
		contrls.put("edit-customer", new EditCustomerController());
		contrls.put("add-activity", new ActivityController());
		contrls.put("register-user", new UserRegistrationController());
		contrls.put("app-settings", new AppSettingsController());
		contrls.put("redeem-points", new RedemptionController());
		contrls.put("sales-report", new SalesReportDlgController());
		contrls.put("data-import", new CustomerDataImportController());
		contrls.put("sales-data-sync", new SalesDataSyncController());
		//contrls.put("customer-churn-report", new CustomerChurnReportDlgController());
		contrls.put("items-sale", new ItemsSaleDlgController());
	}
	
	private static Router router;
	
	public static Router getRouter() {
		if(null == router) {
			router = new Router();
		}
		return router;
	}
	
	public Routeable route(String path) {
		return contrls.get(path);
	}
	
	

}

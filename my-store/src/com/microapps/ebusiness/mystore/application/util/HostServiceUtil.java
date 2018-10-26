package com.microapps.ebusiness.mystore.application.util;

import javafx.application.HostServices;

public class HostServiceUtil {
	
	private HostServiceUtil() {
		
	}
	
	private static HostServices intance;
	
	public static HostServices getHostServices() {
		return intance;
	}
	
	public static HostServices setHostServices(HostServices s) {
		return intance=s;
	}

}

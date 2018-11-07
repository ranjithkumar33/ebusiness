package com.microapps.ebusiness.mystore.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.microapps.ebusiness.mystore.application.dao.ActivityDao;
import com.microapps.ebusiness.mystore.application.dao.ActivityDaoImpl;
import com.microapps.ebusiness.mystore.application.entity.Activity;
import com.microapps.ebusiness.mystore.application.view.CustomerToTotalSaleView;

public class ReportService {
	
	public ReportService() {
		dao = new ActivityDaoImpl();
		as = new ActivityService();
	}
	
	private ActivityDao dao;
	
	private ActivityService as;
	
	public CustomerToTotalSaleView getCustomerToTotalSaleRevenue() {
		CustomerToTotalSaleView v = new CustomerToTotalSaleView();
		List<Activity> cal = dao.findActivitiesByCustomer(Session.getSession().getCustomerFromSession().getId());
		double saleSumAmt = 0;
		if(cal != null && !cal.isEmpty()) {
			saleSumAmt = cal.stream().filter(a-> a.getName().equalsIgnoreCase("New sale")).mapToDouble(a-> a.getAmount()).sum();
		}
		
		v.setCustomerTotalSaleAmount(saleSumAmt);
		v.setTotalSaleAmount(as.getTotalSaleAmount());
		return v;
	}
	
	public Map<String, Double> getCustomerTotalSaleByItemGroups() {
		List<Activity> activitiesByCustimer = dao.findActivitiesByCustomer(Session.getSession().getCustomerFromSession().getId());
		if(activitiesByCustimer != null && !activitiesByCustimer.isEmpty()) {
			return activitiesByCustimer.stream().filter(a-> a.getName().equalsIgnoreCase("New sale")).collect(Collectors.groupingBy(Activity:: getItemGroup, Collectors.summingDouble(Activity:: getAmount)));
		}
		return Collections.EMPTY_MAP;
	}
	
	public List<ReportService.DailyRevenue> getMonthlySalesData(int month){
		List<ReportService.DailyRevenue>  list = new ArrayList<>();
		
		List<Activity> ma = dao.findDailySaleRevenue();
		
		if(ma != null) {
			ma.forEach(a -> {
				ReportService.DailyRevenue dr = new ReportService.DailyRevenue(a.getCreatedOn().toLocalDateTime().toLocalDate(), a.getTotalPurchaseAmount());
				list.add(dr);
			});
		}
		return list;
	}
	
	
	public static class DailyRevenue{
		
		public DailyRevenue(LocalDate date, double revenue){
			this.date=date;
			this.sale=revenue;
		}
		
		private double sale;
		private LocalDate date;
		public LocalDate getDate() {
			return date;
		}
		public void setDate(LocalDate date) {
			this.date = date;
		}
		public double getSale() {
			return sale;
		}
		public void setSale(double sale) {
			this.sale = sale;
		}
		
		
	}

}

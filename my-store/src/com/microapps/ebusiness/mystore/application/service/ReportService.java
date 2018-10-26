package com.microapps.ebusiness.mystore.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.microapps.ebusiness.mystore.application.dao.ActivityDao;
import com.microapps.ebusiness.mystore.application.dao.ActivityDaoImpl;
import com.microapps.ebusiness.mystore.application.entity.Activity;

public class ReportService {
	
	public ReportService() {
		dao = new ActivityDaoImpl();
	}
	
	private ActivityDao dao;
	
	public List<ReportService.DailyRevenue> getMonthlySalesData(int month){
		List<ReportService.DailyRevenue>  list = new ArrayList<>();
		
		List<Activity> ma = dao.findMonthlyActivities();
		
		if(ma != null) {
			ma.forEach(a -> {
				ReportService.DailyRevenue dr = new ReportService.DailyRevenue(a.getCreatedOn().toLocalDateTime().toLocalDate(), a.getRevenue());
				list.add(dr);
			});
		}
		return list;
	}
	
	
	public static class DailyRevenue{
		
		public DailyRevenue(LocalDate date, float revenue){
			this.date=date;
			this.revenue=revenue;
		}
		
		private float revenue;
		private LocalDate date;
		public float getRevenue() {
			return revenue;
		}
		public void setRevenue(float revenue) {
			this.revenue = revenue;
		}
		public LocalDate getDate() {
			return date;
		}
		public void setDate(LocalDate date) {
			this.date = date;
		}
		
		
	}

}

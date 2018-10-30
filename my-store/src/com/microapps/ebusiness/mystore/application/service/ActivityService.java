package com.microapps.ebusiness.mystore.application.service;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import com.microapps.ebusiness.mystore.application.dao.ActivityDao;
import com.microapps.ebusiness.mystore.application.dao.ActivityDaoImpl;
import com.microapps.ebusiness.mystore.application.dao.CustomerDao;
import com.microapps.ebusiness.mystore.application.dao.CustomerDaoImpl;
import com.microapps.ebusiness.mystore.application.dao.exception.RecordNotFoundException;
import com.microapps.ebusiness.mystore.application.domain.ActivityDto;
import com.microapps.ebusiness.mystore.application.entity.Activity;
import com.microapps.ebusiness.mystore.application.exception.SettingNotFoundException;
import com.microapps.ebusiness.mystore.application.util.ActivityType;

public class ActivityService {
	
	private static final Logger LOGGER = Logger.getLogger(ActivityService.class.getName());

	private ActivityDao ad;
	
	private CustomerDao cd;
	
	private LoyalyPointsService lps;
	
	public ActivityService() {
		ad = new ActivityDaoImpl();
		lps = new LoyalyPointsService();
		cd = new CustomerDaoImpl();
	}
	
	public ActivityDto saveActivity(ActivityDto _a) throws SettingNotFoundException {
		if(!_a.getName().equalsIgnoreCase(ActivityType.REDEMPTION.getName())) {
			_a.setEarnedPoints(lps.calculateEarnedPointsForActivity(_a.getAmount(), ActivityType.NEW_SALE));
		}
		
		Activity a;
		try {
			a = ActivityAssembler.toEntity(_a);
			a.setCustomer(cd.findCustomerById(Session.getSession().getCustomerFromSession().getId()));
			a = ad.saveActivity(a);
			_a = ActivityAssembler.toDto(a);
			_a.setCustomer(Session.getSession().getCustomerFromSession());
			Session.getSession().getCustomerFromSession().getActivities().add(_a);
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
		}
	  
		return _a;
	}
	
	
	
	public int syncSalesData(List<ActivityDto> activityList, BiConsumer<Integer, Integer> progressUpdate, int totalWork, int completedWork) throws SettingNotFoundException, RecordNotFoundException {
		if(activityList != null) {
			for(ActivityDto a : activityList) {
				a.setName(ActivityType.NEW_SALE.getName());
				a.setEarnedPoints(lps.calculateEarnedPointsForActivity(a.getAmount(), ActivityType.NEW_SALE));
				Activity _a = ActivityAssembler.toEntity(a);
				
				_a.setCreatedBy("DATA_SYNC");
				_a.setCreatedOn(a.getCreatedOn());
				_a.setCustomer(cd.searchCustomerByMobile(a.getCustomer().getMobile()));
				_a = ad.saveActivity(_a);
				
				if (progressUpdate != null) {
	                progressUpdate.accept(completedWork, totalWork);
	            }
				completedWork++;
			}
		}
		return completedWork;
	}

}

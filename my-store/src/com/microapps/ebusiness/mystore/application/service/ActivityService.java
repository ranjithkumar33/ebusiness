package com.microapps.ebusiness.mystore.application.service;

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

}

package com.microapps.ebusiness.mystore.application.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import com.microapps.ebusiness.mystore.application.dao.SettingsDao;
import com.microapps.ebusiness.mystore.application.dao.SettingsDaoImpl;
import com.microapps.ebusiness.mystore.application.domain.ActivityDto;
import com.microapps.ebusiness.mystore.application.domain.LoyaltyData;
import com.microapps.ebusiness.mystore.application.entity.AppSettings;
import com.microapps.ebusiness.mystore.application.exception.SettingNotFoundException;
import com.microapps.ebusiness.mystore.application.util.ActivityType;

public class LoyalyPointsService {
	
	private static final Logger LOGGER = Logger.getLogger(LoyalyPointsService.class.getName());
	
	public LoyalyPointsService() {
		dao = new SettingsDaoImpl();
	}
	
	private SettingsDao dao;
	
	public LoyaltyData computeLoyaltyData(List<ActivityDto> activities) throws SettingNotFoundException {
		LoyaltyData ld = new LoyaltyData();
		 
		long earnedPoints = 0;
		double totalAmount = 0.0;
		if(activities != null && !activities.isEmpty()) {
			
			Collections.sort(activities, new Comparator<ActivityDto>() {
				@Override
				public int compare(ActivityDto arg0, ActivityDto arg1) {
					return arg0.getCreatedOn().compareTo(arg1.getCreatedOn());
				}
			});
			
			Collections.reverse(activities);
			
			for(ActivityDto a : activities) {
				if(a.getName().equals(ActivityType.NEW_SALE.getName())) {
					totalAmount = totalAmount + a.getAmount();	
				}
			}
			
			for(ActivityDto a : activities) {
				if(a.getName().equals(ActivityType.NEW_SALE.getName())) {
					earnedPoints = earnedPoints + a.getEarnedPoints();
				}
				if(a.getName().equals(ActivityType.REDEMPTION.getName())) {
					earnedPoints = earnedPoints + a.getEarnedPoints();
					break;
				}
			}
			
		}
		ld.setNetPurchaseAmount(totalAmount);
		ld.setEarnedPoints((int)earnedPoints);
		ld.setPointsNeededForRedemption((int)calculateRemainingPointsRequiredForRedemption(earnedPoints));
		ld.setEqvAmtOfPointsNeededForRedemption(calculateEquivalentAmountForRemainingPoints(ld.getPointsNeededForRedemption()));
		
		if(ld.getPointsNeededForRedemption() == 0) {
			ld.setMaturedForRedemption(true);
		}
		
		return ld;
	}
	
	private double calculateEquivalentAmountForRemainingPoints(float requiredPtsForRedemption) throws SettingNotFoundException {

		double res = 0;
		try {
			AppSettings as = dao.findSettings();
			res = (requiredPtsForRedemption / as.getMpf());
		} catch (Exception e) {
			throw new SettingNotFoundException("Application settings not found!");
		}
		return res;
	}
	
	private long calculateRemainingPointsRequiredForRedemption(long accruedPoints) throws SettingNotFoundException {

		long res = 0;
		try {
			AppSettings as = dao.findSettings();
			int thresholdPts = as.getRtp();
			if(accruedPoints < thresholdPts) {
				res = thresholdPts - accruedPoints;
			}
			
		} catch (Exception e) {
			throw new SettingNotFoundException("Application settings not found!");
		}
		return res;
	}
	
	
	public long calculateEligiblePointsForRedemption() throws SettingNotFoundException {
		try {
			AppSettings as = dao.findSettings();
			return as.getRtp();
		} catch (Exception e) {
			throw new SettingNotFoundException("Application settings not found!");
		}
	}
	
	
	public long calculatePointsCarryForward(long accruedPoints) throws SettingNotFoundException {

		long res = 0;
		try {
			AppSettings as = dao.findSettings();
			int thresholdPts = as.getRtp();
			if(accruedPoints >= thresholdPts) {
				res = accruedPoints - thresholdPts;
			}
			
		} catch (Exception e) {
			throw new SettingNotFoundException("Application settings not found!");
		}
		return res;
	}
	
	public double calculateEquivalentAmountForRedemption(long eligiblePoints) throws SettingNotFoundException {

		double res = 0;
		try {
			AppSettings as = dao.findSettings();
			float pmf = as.getPmf();
			res = eligiblePoints*pmf;
		} catch (Exception e) {
			throw new SettingNotFoundException("Application settings not found!");
		}
		return res;
	}
	
	public long calculateEarnedPointsForActivity(double activityAmount, ActivityType at) throws SettingNotFoundException {
		long res = 0;
		if(at.equals(ActivityType.NEW_SALE)) {
			try {
				AppSettings as = dao.findSettings();
				res = (long) (activityAmount * as.getMpf());
			} catch (Exception e) {
				throw new SettingNotFoundException("Application settings not found!");
			}
		}
		
		return res;
	}

}

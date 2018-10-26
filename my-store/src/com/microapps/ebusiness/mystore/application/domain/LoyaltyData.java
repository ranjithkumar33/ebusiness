package com.microapps.ebusiness.mystore.application.domain;

public class LoyaltyData {
	
	private int earnedPoints;
	
	private int pointsNeededForRedemption;
	
	private double eqvAmtOfPointsNeededForRedemption;
	
	private double netPurchaseAmount;
	
	private boolean isMaturedForRedemption;

	public int getEarnedPoints() {
		return earnedPoints;
	}

	public void setEarnedPoints(int earnedPoints) {
		this.earnedPoints = earnedPoints;
	}

	public int getPointsNeededForRedemption() {
		return pointsNeededForRedemption;
	}

	public void setPointsNeededForRedemption(int pointsNeededForRedemption) {
		this.pointsNeededForRedemption = pointsNeededForRedemption;
	}

	public double getEqvAmtOfPointsNeededForRedemption() {
		return eqvAmtOfPointsNeededForRedemption;
	}

	public void setEqvAmtOfPointsNeededForRedemption(double eqvAmtOfPointsNeededForRedemption) {
		this.eqvAmtOfPointsNeededForRedemption = eqvAmtOfPointsNeededForRedemption;
	}

	public double getNetPurchaseAmount() {
		return netPurchaseAmount;
	}

	public void setNetPurchaseAmount(double netPurchaseAmount) {
		this.netPurchaseAmount = netPurchaseAmount;
	}

	public boolean isMaturedForRedemption() {
		return isMaturedForRedemption;
	}

	public void setMaturedForRedemption(boolean isMaturedForRedemption) {
		this.isMaturedForRedemption = isMaturedForRedemption;
	}

}

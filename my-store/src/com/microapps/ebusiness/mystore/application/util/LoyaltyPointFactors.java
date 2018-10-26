package com.microapps.ebusiness.mystore.application.util;

public enum LoyaltyPointFactors {
	
	FULL ("1:1", 1f), HALF("2:1", 0.5f), QUARTER("4:1", 0.25f), THREE_QUARTERS("3:1",0.3f);
	
	LoyaltyPointFactors(String viewText, float factor){
		this.viewText=viewText;
		this.factor=factor;
	}
	
	private String viewText;
	
	private float factor;

	public String getViewText() {
		return viewText;
	}
	
	public float getFactor() {
		return factor;
	}

	public static LoyaltyPointFactors[] getAllRatios(){
		return values();
	}
	
	public static LoyaltyPointFactors getRatioByViewText(String viewText) {
		for(LoyaltyPointFactors r : values()) {
			if(viewText.equalsIgnoreCase(r.getViewText())) {
				return r;
			}
		}
		return null;
	}
	
	public static LoyaltyPointFactors getRatioByFactor(float factor) {
		for(LoyaltyPointFactors r : values()) {
			if(factor == r.getFactor()) {
				return r;
			}
		}
		return null;
	}

}

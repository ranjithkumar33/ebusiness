package com.microapps.ebusiness.mystore.application.domain;

public class AppSettingsDto {
	
	public AppSettingsDto() {
		
	}
	
	public AppSettingsDto(int id, float mpf, float pmf, int rtp) {
		this.id=id;
		this.mpf=mpf;
		this.pmf=pmf;
		this.rtp=rtp;
	}
	
	int id;
	
	float mpf, pmf;
	int rtp;
	
	public float getMpf() {
		return mpf;
	}
	public void setMpf(float mpf) {
		this.mpf = mpf;
	}
	public float getPmf() {
		return pmf;
	}
	public void setPmf(float pmf) {
		this.pmf = pmf;
	}
	public int getRtp() {
		return rtp;
	}
	public void setRtp(int rtp) {
		this.rtp = rtp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}

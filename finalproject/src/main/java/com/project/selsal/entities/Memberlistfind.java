package com.project.selsal.entities;

import org.springframework.stereotype.Component;

@Component
public class Memberlistfind {

	private String memfind;
	private int memstartrow;
	private int memendrow;
	
	
	
	public String getMemfind() {
		return memfind;
	}
	public void setMemfind(String memfind) {
		this.memfind = memfind;
	}
	public int getMemstartrow() {
		return memstartrow;
	}
	public void setMemstartrow(int memstartrow) {
		this.memstartrow = memstartrow;
	}
	public int getMemendrow() {
		return memendrow;
	}
	public void setMemendrow(int memendrow) {
		this.memendrow = memendrow;
	}

	
}

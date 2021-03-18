package com.project.selsal.entities;

import org.springframework.stereotype.Component;


@Component
public class Freeboardlike {
	private int f_seq;
	private String f_email;
	
	public int getF_seq() {
		return f_seq;
	}
	public void setF_seq(int f_seq) {
		this.f_seq = f_seq;
	}
	public String getF_email() {
		return f_email;
	}
	public void setF_email(String f_email) {
		this.f_email = f_email;
	}
}
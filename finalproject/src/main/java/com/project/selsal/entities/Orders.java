package com.project.selsal.entities;

import org.springframework.stereotype.Component;

@Component
public class Orders {
	private int ordernum;
	private String email;
	private String name;
	private String orderadd;
	private String date;
	private int totprice;
	private int orderconfirm;
	private String completedate;
	
	public int getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderadd() {
		return orderadd;
	}
	public void setOrderadd(String orderadd) {
		this.orderadd = orderadd;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getTotprice() {
		return totprice;
	}
	public void setTotprice(int totprice) {
		this.totprice = totprice;
	}
	public int getOrderconfirm() {
		return orderconfirm;
	}
	public void setOrderconfirm(int orderconfirm) {
		this.orderconfirm = orderconfirm;
	}
	public String getCompletedate() {
		return completedate;
	}
	public void setCompletedate(String completedate) {
		this.completedate = completedate;
	}
	
	
	
	

}

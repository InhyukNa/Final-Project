package com.project.selsal.entities;

import org.springframework.stereotype.Component;

@Component
public class Orderdetail {
	private int ordernum;
	private int seq;
	private int procode;
	private String proname;
	private int qty;
	private int proprice;
	
	
	
	
	public int getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getProcode() {
		return procode;
	}
	public void setProcode(int procode) {
		this.procode = procode;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public int getProprice() {
		return proprice;
	}
	public void setProprice(int proprice) {
		this.proprice = proprice;
	}
	public String getProname() {
		return proname;
	}
	public void setProname(String proname) {
		this.proname = proname;
	}


}

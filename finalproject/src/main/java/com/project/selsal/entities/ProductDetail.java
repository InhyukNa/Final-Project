package com.project.selsal.entities;

import org.springframework.stereotype.Component;

@Component
public class ProductDetail {
	private int seq;
	private String code;
	private String name;
	private int buystock;
	private int salestock;
	private int stock;
	private String transdate;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBuystock() {
		return buystock;
	}
	public void setBuystock(int buystock) {
		this.buystock = buystock;
	}
	public int getSalestock() {
		return salestock;
	}
	public void setSalestock(int salestock) {
		this.salestock = salestock;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	
}

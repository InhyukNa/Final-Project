package com.project.selsal.entities;

import org.springframework.stereotype.Component;

@Component
public class Product {
	private String code;
	private String name;
	private int buyprice;
	private int saleprice;
	private int saleqty;
	private int stock;
	private String image;
	private String productexp;
	private int calorie;
	private int orderstock;
	private int product_csf;
	
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
	public int getBuyprice() {
		return buyprice;
	}
	public void setBuyprice(int buyprice) {
		this.buyprice = buyprice;
	}
	public int getSaleprice() {
		return saleprice;
	}
	public void setSaleprice(int saleprice) {
		this.saleprice = saleprice;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getProductexp() {
		return productexp;
	}
	public void setProductexp(String productexp) {
		this.productexp = productexp;
	}
	public int getCalorie() {
		return calorie;
	}
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
	public int getSaleqty() {
		return saleqty;
	}
	public void setSaleqty(int saleqty) {
		this.saleqty = saleqty;
	}
	public int getOrderstock() {
		return orderstock;
	}
	public void setOrderstock(int orderstock) {
		this.orderstock = orderstock;
	}
	public int getProduct_csf() {
		return product_csf;
	}
	public void setProduct_csf(int product_csf) {
		this.product_csf = product_csf;
	}

	
}

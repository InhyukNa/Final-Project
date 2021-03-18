package com.project.selsal.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.project.selsal.entities.Product;

public interface ProductDao {
	public ArrayList<Product> selectAll() throws Exception;
	
	public ArrayList<Product> selectOutStock() throws Exception;
	
	public int deleteAjax1(String code);
	
	public int deleteAjax2(String code);
	
	public int insertRow1(Product product);
	
	public int insertRow2(Product product);
	
	public ArrayList<Product> selectdetailAll(String code) throws Exception;
	
	public int Stockadd1(String code);
	
	public int Stockadd2(HashMap hashmap);
	
	public ArrayList<Product> productChartData(); 
}

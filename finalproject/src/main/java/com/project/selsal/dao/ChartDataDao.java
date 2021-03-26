package com.project.selsal.dao;

import java.util.ArrayList;

import com.project.selsal.entities.ChartData;
import com.project.selsal.entities.Product;

public interface ChartDataDao {
	public ArrayList<Product> stockChartData(); 
	
	public ArrayList<ChartData> saleChartData();
	
	public ArrayList<ChartData> malesaleChartData(int clscode);
	
	public ArrayList<ChartData> femalesaleChartData(int clscode); 
	
	
}

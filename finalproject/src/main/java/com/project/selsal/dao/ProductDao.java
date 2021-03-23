package com.project.selsal.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.project.selsal.entities.Product;

public interface ProductDao {
	// 재고관리 리스트 담아오기
	public ArrayList<Product> selectAll() throws Exception;
	
	// 5개 이하 재고 정보 담아오기
	public ArrayList<Product> selectOutStock() throws Exception;
	
	// 삭제 재고 product의 내역 지우기
	public int deleteAjax1(String code);
	
	// 삭제 재고 productdetail의 내역 지우기
	public int deleteAjax2(String code);
	
	//등록 재고 product에 넣기
	public int insertRow(Product product);
	
	//등록 재고 productdetail에 수량 0으로 넣기
	public int insertRowMeat(Product product);
	
	public int insertRowVegetable(Product product);
	
	public int insertRowSauce(Product product);
	
	//재고 거래내역 상세보기
	public ArrayList<Product> selectdetailAll(String code) throws Exception;
	
	// 최근 거래 내역 반영하여 현재 수량 update 시키기
	public int Stockadd1(String code);
	
	// productdetail에 최근 거래내역 넣기
	public int Stockadd2(HashMap hashmap);
	
	// 재고 등록시 분류별 max code 가져오기
	public int meatMaxCode();
	
	public int vegetableMaxCode();
	
	public int sauceMaxCode();
	

}

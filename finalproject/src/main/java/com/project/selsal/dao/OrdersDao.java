package com.project.selsal.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.project.selsal.entities.Member;
import com.project.selsal.entities.Orderdetail;
import com.project.selsal.entities.Orders;
import com.project.selsal.entities.Product;

@Mapper
@Repository
public interface OrdersDao {
	public Orderdetail selectOne(int ordernum) throws Exception;
////
//	public int insertRow(Member member);
////
//	public int updateRow(Member member);
//	
//	public int updateAjax(Member member);
//
	public int insertRow(Orderdetail orderdetail);
	
	public int productCount();
	
	public int maxOrderNum();
	
	public int orderInsert(int ordernum,String email,String address);
	
	public int orderDelete(int ordernum);
	
	public Member selectAddress(String email);
	
	public int noConfirmCount();
	
	public ArrayList<Orders> noConfirmList();
	
	public ArrayList<Orders> selectAll();
	
	public int deleteOrderDetail(int ordernum);
	
	public int updateOrderConfirm(int ordernum);
	
	public int changeConfirm(int ordernum);
	
	public ArrayList<Orderdetail> selectSaleProduct(int ordernum);
	
	public int insertSaleProduct(String code,int saleqty);
	
	public int updateTotalOrderPrice(int ordernum,int totprice);
	
	public int AdminselectTotalOrder(String email);
	
	public int selectTotalOrder(int ordernum);
	
	public int updateOrderPoint(int ordernum,int point);
	
	public ArrayList<Product> selectNowStock(int ordernum);
	
	public int OrderTotalPrice(int ordernum);
	
	public int completedateUpdate(int ordernum);
	
	public int selectMemLevel(int ordernum);
	
	public int updateMemlevel(int ordernum,int level);
	
	public int AdminupdateMemlevel(String email,int level);
//
//	public int levelUpdate(Member member);
//	
////
//	public int deleteAjax(String email);
}
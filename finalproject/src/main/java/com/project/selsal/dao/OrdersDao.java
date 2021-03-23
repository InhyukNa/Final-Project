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
	// 온라인 주문 선택한 재료 담기 Dao
	public int insertRow(Orderdetail orderdetail);
	
	// 온라인 주문 위한 재교매진여부 확인 Dao
	public int productCount();
	
	// 온라인 주문 마지막 주문번호 +1 Dao
	public int maxOrderNum();
	
	// 온라인 주문 내역 최종 저장 Dao
	public int orderInsert(int ordernum,String email,String address);
	
	// 온라인 주문 내역 삭데 Dao
	public int orderDelete(int ordernum);
	
	// 온라인 주문 저장을 위한 member 테이블 내 주소 SELECT Dao
	public Member selectAddress(String email);
	
	// 관리자 페이지 내 미확인 주문 건 SELECT Dao
	public int noConfirmCount();
	
	// 미확인 주문 내역 Dao
	public ArrayList<Orders> noConfirmList();
	
	// 전체 주문 내역 Dao
	public ArrayList<Orders> selectAll();
	
	// 미확인 주문 내역에서 주문 접수 처리 -> 해당 주문번호 상세내역 삭제
	public int deleteOrderDetail(int ordernum);
	
	// Orders의 주문처리상태 취소처리로 업데이트
	public int updateOrderConfirm(int ordernum);
	
	// Orders의 주문처리상태 주문접수로 업데이트
	public int changeConfirm(int ordernum);
	
	// 판매 완료 재고 담아오기
	public ArrayList<Orderdetail> selectSaleProduct(int ordernum);
	
	// 판매 완료 재고 Productdetail에 추가
	public int insertSaleProduct(String code,int saleqty);
	
	// 회원 정보 내 총 주문 금액 add
	public int updateTotalOrderPrice(int ordernum,int totprice);
	
	// 관리자가 회원 리스트에서 회원 등급 반영을 위한 총 주문 금액 가져오기
	public int AdminselectTotalOrder(String email);
	
	//주문 후 회워 등급 반영 위한 총 주문 금액 가져오기
	public int selectTotalOrder(int ordernum);
	
	// 주문 후 회원 등급에 따른 Point 적립
	public int updateOrderPoint(int ordernum,int point);
	
	// 판매 가능 재고 담아오기
	public ArrayList<Product> selectNowStock(int ordernum);
	
	// 회원 총 주문 금액 누적을 위한 주문금액 가져오기
	public int OrderTotalPrice(int ordernum);
	// 주문 상태 처리 완료 시간 넣기
	public int completedateUpdate(int ordernum);
	
	// 포인트 적립을 위한 회원 등급 조회
	public int selectMemLevel(int ordernum);
	
	// 변동 총 주문금액에 따른 회원 등급 변경
	public int updateMemlevel(int ordernum,int level);
	
	// 관리자가 회원 리스트에서 회원 등급 수정시 반영 Query
	public int AdminupdateMemlevel(String email,int level);
}
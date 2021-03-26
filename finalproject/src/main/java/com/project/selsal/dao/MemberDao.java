package com.project.selsal.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.project.selsal.entities.Member;
import com.project.selsal.entities.Memberlistfind;
import com.project.selsal.entities.Orderdetail;
import com.project.selsal.entities.Orders;

@Mapper
@Repository
public interface MemberDao {
	public Member selectOne(String email) throws Exception;

	public int insertRow(Member member);

	public int updateRow(Member member);
	
	public int updateAjax(Member member);
	
	public ArrayList<Orderdetail> orderCart(int ordernum);
	
	public int memberlistfinder(Memberlistfind listfind);
	
	ArrayList<Member> findListMember(Memberlistfind listfind);
	
	public int deleteorders(int ordernum) throws Exception;
	
	public Orders pointsum(String email) throws Exception;

	public ArrayList<Member> selectAll();
	
	public Orders orderselectOne(int ordernum);
	
	ArrayList<Orderdetail> ordernumselect(int ordernum);

	public int levelUpdate(Member member);
	
	public String selectIdFind(String name,int gender,int birth);
	
	public int selectPWFind(String email,int gender,int birth);
	
	public int selectAllcount();
	
	public int couponcount(String email);
	
	public Member deleteAjax(String email);
	
	public int updatePW(String newPW, String email);
	
	public ArrayList<Orders> orderselectAll(String email);

	
}
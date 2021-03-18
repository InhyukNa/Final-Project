package com.project.selsal.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.project.selsal.entities.Member;

@Mapper
@Repository
public interface MemberDao {
	public Member selectOne(String email) throws Exception;
//
	public int insertRow(Member member);
//
	public int updateRow(Member member);
	
	public int updateAjax(Member member);
//
	public ArrayList<Member> selectAll();
//
	public int levelUpdate(Member member);
	
	public String selectIdFind(String name,int gender,int birth);
	
	public String selectPWFind(String email,int gender,int birth);
	
	
	public int selectAllcount();
	
//
	public int deleteAjax(String email);
}
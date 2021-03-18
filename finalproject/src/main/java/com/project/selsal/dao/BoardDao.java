package com.project.selsal.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.project.selsal.entities.BoardPaging;
import com.project.selsal.entities.Freeboard;
import com.project.selsal.entities.Freeboardcomment;

@Mapper
@Repository
public interface BoardDao {
	public int insertRowFreeBoard(Freeboard freeboard) throws Exception;
	
	public int selectCountFirstFreeBoard() throws Exception;
	
	public ArrayList<Freeboard> selectPageListFreeBoard(BoardPaging boardpaging) throws Exception;
	
	public ArrayList<Freeboard> selectPageListFreeBoardPopular(BoardPaging boardpaging) throws Exception;
	
	public Freeboard selectOneFreeBoard(int f_seq) throws Exception;
	
	public Freeboard selectOneFreeBoard2(Freeboard freeboard) throws Exception;
	
	public void addHitFreeBoard(int f_seq) throws Exception;
	
	public int updateRowFreeBoard(Freeboard freeboard) throws Exception;
	
	public int deleteRowFreeBoard(int f_seq) throws Exception;
	
	public ArrayList<Freeboard> findListFreeBoard(BoardPaging boardpaging) throws Exception;
	
	public int selectCountFreeBoard(BoardPaging boardpaging) throws Exception;
	
	public void freeBoardLike(int f_seq) throws Exception;
	
	public int selectLike(int f_seq) throws Exception;
	
	public void freeBoardLikeInsert(Freeboard freeboard) throws Exception;
	
	public int selectLikeCheck(Freeboard freeboard) throws Exception;
	
	public void freeBoardLikeDelete(Freeboard freeboard) throws Exception;
	
	public int insertRowFreeBoardComment(Freeboardcomment freeboardcomment) throws Exception;
	
	public ArrayList<Freeboardcomment> selectOneFreeBoardComment(int f_seq) throws Exception;
	
	public int deleteRowFreeBoardComment(int comment_num) throws Exception;
}
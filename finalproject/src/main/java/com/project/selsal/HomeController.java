package com.project.selsal;


import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.selsal.dao.ChartDataDao;
import com.project.selsal.entities.ChartData;



@Controller

public class HomeController {
	 
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "/")
	public String index(Model model) {	
	return "index";
 
	}
	
	// 메인페이지 bar그래프 안에 들어갈 남성 종류별 판매 베스트 3 데이터 ajax 연동
	@RequestMapping(value = "/maleBestSaleSelect", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<ChartData> maleBestSaleSelect(@RequestParam int clscode) {
		ChartDataDao chartDataDao = sqlSession.getMapper(ChartDataDao.class);
		ArrayList<ChartData> data = chartDataDao.malesaleChartData(clscode);
		return data;
	}
	
	// 메인페이지 bar그래프 안에 들어갈 여성 종류별 판매 베스트 3 데이터 ajax 연동
	@RequestMapping(value = "/femaleBestSaleSelect", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<ChartData> femaleBestSaleSelect(@RequestParam int clscode) {
		ChartDataDao chartDataDao = sqlSession.getMapper(ChartDataDao.class);
		ArrayList<ChartData> data = chartDataDao.femalesaleChartData(clscode);
		return data;
	}
}

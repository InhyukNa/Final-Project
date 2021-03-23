package com.project.selsal;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.selsal.dao.ChartDataDao;
import com.project.selsal.dao.MemberDao;
import com.project.selsal.dao.OrdersDao;
import com.project.selsal.dao.ProductDao;
import com.project.selsal.entities.ChartData;
import com.project.selsal.entities.Product;

@Controller
public class AdminController {
	
	
	@Autowired
	private SqlSession sqlSession;
	
	// 관리자 페이지 띄우기
	@RequestMapping(value = "/adminList", method = RequestMethod.GET)
	public String adminList(Locale locale, Model model) throws Exception {
		ProductDao productDao = sqlSession.getMapper(ProductDao.class);
		ArrayList<Product> stocks = productDao.selectOutStock();
		int outofstocks = stocks.size();
		model.addAttribute("outofstocks",outofstocks);
		model.addAttribute("stocks",stocks);
		
		MemberDao memberDao = sqlSession.getMapper(MemberDao.class);
		int membercnt = memberDao.selectAllcount();
		model.addAttribute("membercnt",membercnt);
		
		OrdersDao orderDao = sqlSession.getMapper(OrdersDao.class);
		int noconfirmorder = orderDao.noConfirmCount();
		model.addAttribute("noconfirmorder",noconfirmorder);
		
		

		return "admin/admin_list";
	}
	
	// 관리자 페이지 bar그래프 안에 들어갈 데이터 ajax 연동 컨트롤러
	@RequestMapping(value = "/productDataSelect", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<Product> productDataSelect() {
		ChartDataDao ChartDataDao = sqlSession.getMapper(ChartDataDao.class);
		ArrayList<Product> data = ChartDataDao.stockChartData();
		return data;
	}
	
	//관리자 페이지 pie그래프 안에 들어갈 데이터 ajax 연동 컨트롤러
	@RequestMapping(value = "/SaleProductDataSelect", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<ChartData> SaleProductDataSelect() {
		ChartDataDao ChartDataDao = sqlSession.getMapper(ChartDataDao.class);
		ArrayList<ChartData> chartdata = ChartDataDao.saleChartData();
		
		return chartdata;
	}
	
	// 관리자 페이지 내 5개 이하 재료 빠른 추가주문처리
	@RequestMapping(value = "/fastOrder", method = RequestMethod.GET)
	public String fastOrder(Locale locale, Model model,@RequestParam String code,@RequestParam String name) throws Exception {
		System.out.println(code+" "+name);
		HashMap codename = new HashMap();
		codename.put("code",code);
		codename.put("name",name);
		
		ProductDao productDao = sqlSession.getMapper(ProductDao.class);
		productDao.Stockadd2(codename);
		productDao.Stockadd1(code);
		
		return "redirect:adminList";
	}
	
	// 재고관리 재고 리스트
	@RequestMapping(value = "/productList", method = RequestMethod.GET)
	public String productList(Locale locale, Model model) throws Exception {
		ProductDao productDao = sqlSession.getMapper(ProductDao.class);
		ArrayList<Product> products = productDao.selectAll();
		model.addAttribute("products",products);
		
		return "admin/product_list";
	}
	
	// 재고관리 제품 삭제 ajax 연동 컨트롤러
	@RequestMapping(value = "/productDeleteAjax", method = RequestMethod.POST)
	@ResponseBody
	public String productDeleteAjax(@RequestParam String code) {
		ProductDao productDao = sqlSession.getMapper(ProductDao.class);
		int result = productDao.deleteAjax1(code);
		productDao.deleteAjax2(code);
		System.out.println(result);
		if (result > 0) {
			return "y";
		} else {
			return "n";
		}
	}
	// 재고관리 새로운 재고 등록페이지
	@RequestMapping(value = "/productInsert", method = RequestMethod.GET)
	public String productInsert(Locale locale, Model model) throws Exception {

		return "admin/product_insert";
	}
	
	//재고관리 새로운 재고 등록페이지 저장
	@RequestMapping(value = "/productInsertSave", method = RequestMethod.POST)
	public String productInsertSave(Model model,@ModelAttribute Product product, 
			@RequestParam("imagefile") MultipartFile imagefile) throws Exception {
		ProductDao productDao = sqlSession.getMapper(ProductDao.class);
		String path = "F:/SPRINGBOOTPROJECT/finalproject/src/main/resources/static/uploadproductimg/";
		String imgname = imagefile.getOriginalFilename();
		String realpath = "/uploadproductimg/";
		if (imgname.equals("")) {
			product.setImage("/uploadproductimg/noimage.png");
		} else {
			byte bytes[] = imagefile.getBytes();
			try {
				BufferedOutputStream output = new BufferedOutputStream(
					new FileOutputStream(path + imgname));
				output.write(bytes);
				output.flush();
				output.close();
				product.setImage(realpath+imgname);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		productDao.insertRow1(product);
		productDao.insertRow2(product);
		return "redirect:productList";
	}
	
	//재고관리 재고 상세 내역 보기
	@RequestMapping(value = "/productDetailList", method = RequestMethod.GET)
	public String productDetailList(Locale locale, Model model,@RequestParam String code) throws Exception {
		ProductDao productDao = sqlSession.getMapper(ProductDao.class);
		ArrayList<Product> productdetails = productDao.selectdetailAll(code);
		model.addAttribute("productdetails",productdetails);
		return "admin/productdetail_list";
	}
	
	
}

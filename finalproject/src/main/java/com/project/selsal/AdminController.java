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
	
	
	@RequestMapping(value = "/adminList", method = RequestMethod.GET)
	public String adminList(Locale locale, Model model) throws Exception {
		ProductDao dao = sqlSession.getMapper(ProductDao.class);
		ArrayList<Product> stocks = dao.selectOutStock();
		int outofstocks = stocks.size();
		model.addAttribute("outofstocks",outofstocks);
		model.addAttribute("stocks",stocks);
		
		MemberDao memberdao = sqlSession.getMapper(MemberDao.class);
		int membercnt = memberdao.selectAllcount();
		model.addAttribute("membercnt",membercnt);
		
		OrdersDao orderdao = sqlSession.getMapper(OrdersDao.class);
		int noconfirmorder = orderdao.noConfirmCount();
		model.addAttribute("noconfirmorder",noconfirmorder);
		
		

		return "admin/admin_list";
	}
	
	@RequestMapping(value = "/productDataSelect", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<Product> productDataSelect() {
		ChartDataDao dao = sqlSession.getMapper(ChartDataDao.class);
		ArrayList<Product> data = dao.stockChartData();
		return data;
	}
	
	@RequestMapping(value = "/SaleProductDataSelect", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<ChartData> SaleProductDataSelect() {
		ChartDataDao chartdao = sqlSession.getMapper(ChartDataDao.class);
		ArrayList<ChartData> chartdata = chartdao.saleChartData();
		
		return chartdata;
	}
	
	@RequestMapping(value = "/fastOrder", method = RequestMethod.GET)
	public String fastOrder(Locale locale, Model model,@RequestParam String code,@RequestParam String name) throws Exception {
		System.out.println(code+" "+name);
		HashMap codename = new HashMap();
		codename.put("code",code);
		codename.put("name",name);
		
		ProductDao dao = sqlSession.getMapper(ProductDao.class);
		dao.Stockadd2(codename);
		dao.Stockadd1(code);
		
		return "redirect:adminList";
	}
	
	
	@RequestMapping(value = "/productList", method = RequestMethod.GET)
	public String productList(Locale locale, Model model) throws Exception {
		ProductDao dao = sqlSession.getMapper(ProductDao.class);
		ArrayList<Product> products = dao.selectAll();
		model.addAttribute("products",products);
		
		return "admin/product_list";
	}
	
	@RequestMapping(value = "/productDeleteAjax", method = RequestMethod.POST)
	@ResponseBody
	public String productDeleteAjax(@RequestParam String code) {
		ProductDao dao = sqlSession.getMapper(ProductDao.class);
		int result = dao.deleteAjax1(code);
		dao.deleteAjax2(code);
		System.out.println(result);
		if (result > 0) {
			return "y";
		} else {
			return "n";
		}
	}
	
	@RequestMapping(value = "/productInsert", method = RequestMethod.GET)
	public String productInsert(Locale locale, Model model) throws Exception {

		return "admin/product_insert";
	}
	
	@RequestMapping(value = "/productInsertSave", method = RequestMethod.POST)
	public String productInsertSave(Model model,@ModelAttribute Product product, 
			@RequestParam("imagefile") MultipartFile imagefile) throws Exception {
		ProductDao dao = sqlSession.getMapper(ProductDao.class);
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
		
		dao.insertRow1(product);
		dao.insertRow2(product);
		return "redirect:productList";
	}
	

	@RequestMapping(value = "/productDetailList", method = RequestMethod.GET)
	public String productDetailList(Locale locale, Model model,@RequestParam String code) throws Exception {
		ProductDao dao = sqlSession.getMapper(ProductDao.class);
		ArrayList<Product> productdetails = dao.selectdetailAll(code);
		model.addAttribute("productdetails",productdetails);
		return "admin/productdetail_list";
	}
	
	
}

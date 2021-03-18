package com.project.selsal;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.selsal.dao.OrdersDao;
import com.project.selsal.dao.ProductDao;
import com.project.selsal.entities.Member;
import com.project.selsal.entities.Orderdetail;
import com.project.selsal.entities.Orders;
import com.project.selsal.entities.Product;




@Controller
public class OrderController {
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	Orders orders;
	@Autowired
	Orderdetail orderdetail;
	@Autowired
	Product product;
	
	
	@RequestMapping(value = "/sessionCheck", method = RequestMethod.POST)
	@ResponseBody
	public String sessionCheck(HttpSession session) {
		String loginchk = (String) session.getAttribute("sessionemail");
		String result;
		if(loginchk==null) {
			result= "n";
		}else {
			result="y";
		}
		return result;
	}
	
	@RequestMapping(value = "/Order", method = RequestMethod.GET)
	public String Order(Locale locale, Model model) throws Exception {
		ProductDao dao = sqlSession.getMapper(ProductDao.class);
		ArrayList<Product> products = dao.selectAll();
		model.addAttribute("products",products);
		OrdersDao orderdao = sqlSession.getMapper(OrdersDao.class);
		int orderNum = orderdao.maxOrderNum();
		model.addAttribute("ordernum",orderNum);
		return "order/order_insert";
	}
	
	@RequestMapping(value = "/orderInsert", method = RequestMethod.POST)
	@ResponseBody
	public String orderInsert(@RequestParam int procode,@RequestParam int qty,@RequestParam int ordernum) {
		OrdersDao dao = sqlSession.getMapper(OrdersDao.class);
		orderdetail.setOrdernum(ordernum);
		orderdetail.setQty(qty);
		orderdetail.setProcode(procode);
		dao.insertRow(orderdetail);
		return "";
	}
	@RequestMapping(value = "/productCount", method = RequestMethod.POST)
	@ResponseBody
	public int productCount() {
		OrdersDao dao = sqlSession.getMapper(OrdersDao.class);
		int result = dao.productCount();
		return result;
	}
	@RequestMapping(value = "/orderConfirm", method = RequestMethod.POST)
	@ResponseBody
	public String orderConfirm(@RequestParam int ordernum,HttpSession session) {
		OrdersDao dao = sqlSession.getMapper(OrdersDao.class);
		String email = (String) session.getAttribute("sessionemail");
		Member member = dao.selectAddress(email);
		String address = "�슦�렪踰덊샇:"+ member.getZipcode()+" / "+member.getAddress()+" , "+member.getDetailaddress(); 
		dao.orderInsert(ordernum, email,address);
		return "";
	}
	
	@RequestMapping(value = "/orderCancle", method = RequestMethod.POST)
	@ResponseBody
	public String orderCancle(@RequestParam int ordernum) {
		OrdersDao dao = sqlSession.getMapper(OrdersDao.class);
		dao.orderDelete(ordernum);
		return "";
	}
	
	@RequestMapping(value = "/noconfirmlist", method = RequestMethod.GET)
	public String noConfirmList(Locale locale, Model model) throws Exception {
		OrdersDao orderdao = sqlSession.getMapper(OrdersDao.class);
		ArrayList<Orders> noconfirmlist = orderdao.noConfirmList();
		model.addAttribute("noconfirmlist",noconfirmlist);
		
		return "order/noconfirm_order_list";
	}
	
	@RequestMapping(value = "/OrderList", method = RequestMethod.GET)
	public String OrderList(Locale locale, Model model) throws Exception {
		OrdersDao orderdao = sqlSession.getMapper(OrdersDao.class);
		ArrayList<Orders> orderlist = orderdao.selectAll();
		
		for(Orders order:orderlist) {
			if(order.getCompletedate()== null) {
				order.setCompletedate("처리 전");
			}
		}
		model.addAttribute("orderlist",orderlist);
		
		return "order/order_list";
	}
	
	@RequestMapping(value = "/orderConfirm", method = RequestMethod.GET)
	public String orderConfirm(@RequestParam int ordernum) throws Exception {
		OrdersDao orderdao = sqlSession.getMapper(OrdersDao.class);
		ArrayList<Orders> orderlist = orderdao.selectAll();
		
		return "redirect:order_list";
	}
	
	@RequestMapping(value = "/QuickorderConfirm", method = RequestMethod.GET)
	public String QuickorderConfirm(@RequestParam int ordernum,HttpSession session) throws Exception {
		OrdersDao orderdao = sqlSession.getMapper(OrdersDao.class);
		ProductDao productdao = sqlSession.getMapper(ProductDao.class);
		orderdao.changeConfirm(ordernum);
		orderdao.completedateUpdate(ordernum);
		ArrayList<Orderdetail> saleproduct = orderdao.selectSaleProduct(ordernum);
		for(Orderdetail salepro:saleproduct) {
			String code = String.valueOf(salepro.getProcode());
//			orderdao.selectNowStock
			orderdao.insertSaleProduct(code,salepro.getQty());
			productdao.Stockadd1(code);
		}
		int totprice = orderdao.OrderTotalPrice(ordernum);
		int point = (int) (totprice*0.05);
		orderdao.updateTotalOrderPrice((String) session.getAttribute("sessionemail"), totprice);
		orderdao.updateOrderPoint((String) session.getAttribute("sessionemail"), point);
		
		return "redirect:OrderList";
	}
	
	
}

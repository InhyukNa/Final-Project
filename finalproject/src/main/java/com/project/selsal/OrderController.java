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
   
   // 온라인 주문 들어가기 위한 session 체크 ajax 연동
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
   
   //온라인 주문 페이지 열기
   @RequestMapping(value = "/Order", method = RequestMethod.GET)
   public String Order(Locale locale, Model model) throws Exception {
      ProductDao productDao = sqlSession.getMapper(ProductDao.class);
      ArrayList<Product> products = productDao.selectAll();
      model.addAttribute("products",products);
      OrdersDao orderDao = sqlSession.getMapper(OrdersDao.class);
      int orderNum = orderDao.maxOrderNum();
      model.addAttribute("ordernum",orderNum);
      return "order/order_insert";
   }
   
   //온라인주문 선택한 재료 담기 ajax연동
   @RequestMapping(value = "/orderInsert", method = RequestMethod.POST)
   @ResponseBody
   public String orderInsert(@RequestParam int procode,@RequestParam int qty,@RequestParam int ordernum) {
      OrdersDao orderDao = sqlSession.getMapper(OrdersDao.class);
      orderdetail.setOrdernum(ordernum);
      orderdetail.setQty(qty);
      orderdetail.setProcode(procode);
      orderDao.insertRow(orderdetail);
      return "";
   }
   
   //온라인주문하기 위한 재고 매진 여부 ajax
   @RequestMapping(value = "/productCount", method = RequestMethod.POST)
   @ResponseBody
   public int productCount() {
      OrdersDao orderDao = sqlSession.getMapper(OrdersDao.class);
      int result = orderDao.productCount();
      return result;
   }
   
   //온라인주문 내역 최종 저장 ajax
   @RequestMapping(value = "/orderConfirm", method = RequestMethod.POST)
   @ResponseBody
   public String orderConfirm(@RequestParam int ordernum,HttpSession session) {
      OrdersDao orderDao = sqlSession.getMapper(OrdersDao.class);
      String email = (String) session.getAttribute("sessionemail");
      Member member = orderDao.selectAddress(email);
      String address = "우편번호:"+ member.getZipcode()+" / "+member.getAddress()+" , "+member.getDetailaddress(); 
      orderDao.orderInsert(ordernum, email,address);
      return "";
   }
   
   // 미확인 주문 리스트 띄우기
   @RequestMapping(value = "/noconfirmList", method = RequestMethod.GET)
   public String noconfirmList(Locale locale, Model model) throws Exception {
      OrdersDao orderDao = sqlSession.getMapper(OrdersDao.class);
      ArrayList<Orders> noconfirmlist = orderDao.noConfirmList();
      model.addAttribute("noconfirmlist",noconfirmlist);
      
      return "order/noconfirm_order_list";
   }
   
   // 전체 주문 리스트 띄우기
   @RequestMapping(value = "/OrderList", method = RequestMethod.GET)
   public String OrderList(Locale locale, Model model) throws Exception {
      OrdersDao orderDao = sqlSession.getMapper(OrdersDao.class);
      ArrayList<Orders> orderlist = orderDao.selectAll();
      
      for(Orders order:orderlist) {
         if(order.getCompletedate()== null) {
            order.setCompletedate("처리 전");
         }
      }
      model.addAttribute("orderlist",orderlist);
      
      return "order/order_list";
   }
   
   //orderConfim 중복
//   @RequestMapping(value = "/orderConfirm", method = RequestMethod.GET)
//   public String orderConfirm(@RequestParam int ordernum) throws Exception {
//      OrdersDao orderdao = sqlSession.getMapper(OrdersDao.class);
//      ArrayList<Orders> orderlist = orderdao.selectAll();
//      
//      return "redirect:order_list";
//   }
   
   // 주문 재고와 판매 가능한 남은 재고 비교하기 
   @RequestMapping(value = "/NowStockChk", method = RequestMethod.POST)
   @ResponseBody
   public String NowStockChk(@RequestParam int ordernum) {
      OrdersDao orderdao = sqlSession.getMapper(OrdersDao.class);
      ArrayList<Product> stockchk = orderdao.selectNowStock(ordernum);
      String result = "";
      for(Product chk : stockchk) {
         if(chk.getOrderstock()==chk.getStock()) {
            result = "end";
         }else if(chk.getOrderstock()>chk.getStock()){
            result = "n";
         }else {
            result ="y";
         }
      }
      return result;   
   }
   
   // 미확인주문 리스트 페이지 빠른 주문접수 처리
   @RequestMapping(value = "/QuickorderConfirm", method = RequestMethod.GET)
   public String QuickorderConfirm(@RequestParam int ordernum,HttpSession session) throws Exception {
      OrdersDao orderDao = sqlSession.getMapper(OrdersDao.class);
      ProductDao productDao = sqlSession.getMapper(ProductDao.class);
      orderDao.changeConfirm(ordernum);
      orderDao.completedateUpdate(ordernum);
      ArrayList<Orderdetail> saleproduct = orderDao.selectSaleProduct(ordernum);
      for(Orderdetail salepro:saleproduct) {
         String code = String.valueOf(salepro.getProcode());
         orderDao.insertSaleProduct(code,salepro.getQty());
         productDao.Stockadd1(code);
      }
      int totprice = orderDao.OrderTotalPrice(ordernum);
      orderDao.updateTotalOrderPrice(ordernum, totprice);
      int point = 0;
      int level = orderDao.selectMemLevel(ordernum);
      if(level==5) { // 브론즈
         point = (int) (totprice*0.05);
      }else if(level==4){ // 실버
         point = (int) (totprice*0.10);
      }else if(level==3){ // 골드
         point = (int) (totprice*0.15);
      }else if(level==2){ // VIP
         point = (int) (totprice*0.2);
      }
      orderDao.updateOrderPoint(ordernum, point);
      
      int changelevel=0;
      int totalorder = orderDao.selectTotalOrder(ordernum);
      if(0<=totalorder && totalorder<50000) {
         changelevel = 5;
      }else if(50000<=totalorder && totalorder<200000) {
         changelevel = 4;
      }else if(200000<=totalorder && totalorder<500000) {
         changelevel = 3;
      }else if(500000<=totalorder) {
         changelevel = 2;
      }
      orderDao.updateMemlevel(ordernum,changelevel);
      
      
      return "redirect:OrderList";
   }
   
   //미확인주문 리스트 페이지 빠른 주문접수취소 처리
   @RequestMapping(value = "/QuickOrderCancle", method = RequestMethod.POST)
   @ResponseBody
   public String QuickOrderCancle(@RequestParam int ordernum) {
      OrdersDao orderDao = sqlSession.getMapper(OrdersDao.class);
      orderDao.deleteOrderDetail(ordernum);
      orderDao.updateOrderConfirm(ordernum);
      orderDao.completedateUpdate(ordernum);
      return "";   
   }
   @RequestMapping(value = "/ingredientDetail", method = RequestMethod.GET)
   public String ingredientDetail(Locale locale, Model model) throws Exception {
      ProductDao productDao = sqlSession.getMapper(ProductDao.class);
      ArrayList<Product> products = productDao.selectAll();
      model.addAttribute("products",products);
      return "order/ingredient_detail";
   }

}
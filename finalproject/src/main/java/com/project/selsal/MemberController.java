package com.project.selsal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.selsal.dao.MemberDao;
import com.project.selsal.dao.OrdersDao;
import com.project.selsal.entities.Member;
import com.project.selsal.entities.Memberlistfind;
import com.project.selsal.entities.Orderdetail;
import com.project.selsal.entities.Orders;


@Controller
public class MemberController {
   
   
   @Autowired
   private SqlSession sqlSession;
   @Autowired
   Member member;
   @Autowired
   Orders orders;
   @Autowired
   Memberlistfind listfind;

   @RequestMapping(value = "/index", method = RequestMethod.GET)
   public String index(Locale locale, Model model) {
      return "index";
   }
   
   
 //---------------로그인페이지 접속
   @RequestMapping(value = "/memberLogin", method = RequestMethod.GET)
   public String memberLogin(Locale locale, Model model) {
      
      return "login/login";
   }
   
 //---------------회원로그인
   @RequestMapping(value = "/memberLoginUP", method = RequestMethod.POST)
   public String memberLoginUP(Locale locale, Model model, @ModelAttribute Member member, HttpSession session)throws Exception {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      Member data = dao.selectOne(member.getEmail());
      if (data == null) {
         return "/login/login_fail";
      } else {
         boolean passchk = BCrypt.checkpw(member.getPassword(), data.getPassword());
         if (passchk) {
            session.setAttribute("sessionemail", data.getEmail());
            session.setAttribute("sessionname", data.getName());
            session.setAttribute("sessionlevel", data.getLevel());
            return "index";
         } else {
            return "/login/login_fail";
         }
         
      }

   }
   
	//---------------회원찾기
	@RequestMapping(value = "/memberfind", method = RequestMethod.POST)
	public String memberfind(Locale locale, Model model,@RequestParam String memfind) throws Exception {
		MemberDao dao = sqlSession.getMapper(MemberDao.class);

		int pagesize = 10;
		int page = 1;
		int startrow = (page-1) * pagesize;
		int endrow = 10;
		listfind.setMemfind(memfind);
		if(listfind.getMemfind() == null) {
			listfind.setMemfind("");
		}
		listfind.setMemstartrow(startrow);
		listfind.setMemendrow(endrow);
		int memcount = dao.memberlistfinder(listfind);
		int countPage = 1;
		if(memcount % pagesize == 0){
			countPage = 0;
		}
		int membercount = memcount / pagesize + countPage;
		int pages[] = new int[membercount];
		for(int i = 0; i <membercount;i++) {
			pages[i] = i+1;
		}
		ArrayList<Member> members = dao.findListMember(listfind);
		model.addAttribute("members",members);
		model.addAttribute("pages",pages);
		model.addAttribute("find",memfind);
		return "member/member_list";
	}
   
   
	//---------------아이디찾기
   @RequestMapping(value = "/IdFindUP", method = RequestMethod.POST)
   @ResponseBody
   public String IdFindUP(Model model,@RequestParam String name,@RequestParam int birth,@RequestParam int gender) throws Exception {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      String email = dao.selectIdFind(name,gender,birth);
      return email;
   }
   //---------------로그아웃
   @RequestMapping(value = "/memberlogout", method = RequestMethod.GET)
   public String memberlogout(HttpSession session) {
      session.invalidate();
      return "index";
   }
   //---------------회원가입 페이지 접속
   @RequestMapping(value = "/memberInsert", method = RequestMethod.GET)
   public String memberInsert() {
      return "member/member_insert";
   }
   
	//---------------회원가입하기
   @RequestMapping(value = "/memberInsertSave", method = RequestMethod.POST)
   public String memberInsertSave(Model model,@ModelAttribute Member member ) {
      MemberDao daomember = sqlSession.getMapper(MemberDao.class);
      
      String encodepassword = hashPassword(member.getPassword());
      member.setPassword(encodepassword);
      
      daomember.insertRow(member);
      
      
      return "index";
   }
	//---------------마이페이지
   @RequestMapping(value = "/membermypage", method = RequestMethod.GET)
   public String membermypage(Locale locale, Model model,HttpSession session) throws Exception {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      String email = (String) session.getAttribute("sessionemail");
      ArrayList<Member> members = dao.selectAll();
      Member data = dao.selectOne(email);
      int couponcount = dao.couponcount(email);
      ArrayList<Orders> orders =  dao.orderselectAll(email);
      model.addAttribute("couponcount",couponcount);
      model.addAttribute("orders",orders);
      model.addAttribute("data",data);
      model.addAttribute("members", members);
      return "member/member_mypage";
   }
	//---------------주문취소 js연동
   @RequestMapping(value = "/deleteordersAjax", method = RequestMethod.POST)
   @ResponseBody
   public String deleteordersAjax(@RequestParam int ordernum) throws Exception {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      dao.deleteorders(ordernum);
      return "redirect:membermypage";
   }
	//---------------주문내역서 
   @RequestMapping(value = "/memberMypagedetail", method = RequestMethod.GET)
   public String memberMypagedetail(Model model,HttpSession session,@RequestParam int ordernum) throws Exception {
      MemberDao memberdao = sqlSession.getMapper(MemberDao.class);
      String email = (String) session.getAttribute("sessionemail");
      Member member = memberdao.selectOne(email);
      Orders orderpage = memberdao.orderselectOne(ordernum);
      ArrayList<Orderdetail> orderpageing = memberdao.ordernumselect(ordernum);
      int maxseq = orderpageing.size();
      model.addAttribute("member",member);
      model.addAttribute("maxseq",maxseq);
      model.addAttribute("orderpage",orderpage);
      model.addAttribute("orderpageing",orderpageing);
      return "member/member_mypagedetail";
   }
	//--------------- 주문내역서에서 마이페이지로 넘어가는 버튼
   @RequestMapping(value = "/detailmypage", method = RequestMethod.GET)
   public String detailmypage(){
      return "redirect:membermypage";
   }
   
   
   
	//---------------회원리스트
   @RequestMapping(value = "/memberList", method = RequestMethod.GET)
      public String memberList(Locale locale,Model model) {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      ArrayList<Member> members = dao.selectAll();
      model.addAttribute("members",members);
      return "member/member_list";
   }
	//---------------회원정보 수정페이지
   @RequestMapping(value = "/memberUpdate", method = RequestMethod.GET)
   public String memberUpdate(Locale locale, Model model, HttpSession session) throws Exception {
      String email = (String) session.getAttribute("sessionemail");
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      Member row = dao.selectOne(email);
      model.addAttribute("row",row);
      return "member/member_update";
   }
   //---------------회원정보 수정하기
   @RequestMapping(value = "/memberUpdateSave", method = RequestMethod.POST)
    public String memberUpdateSave( Model model,HttpSession session, @ModelAttribute Member member) throws IOException {  
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
       String encodepassword = hashPassword(member.getPassword());
       member.setPassword(encodepassword);
       dao.updateRow(member);       
       return "redirect:membermypage";
   }
   
	//---------------관리자에서 회원정보 들어가기
   @RequestMapping(value = "/adminUpdate", method = RequestMethod.GET)
   public String adminUpdate(Locale locale, Model model, @RequestParam String email, HttpSession session) throws Exception {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      Member row = dao.selectOne(email);
      model.addAttribute("row",row);
      return "member/member_updateAdmin";
   }
	//---------------관리자가 회원정보 수정하기
   @RequestMapping(value = "/memberUpdateAdminSave", method = RequestMethod.POST)
    public String memberUpdateAdminSave( Model model,HttpSession session, @ModelAttribute Member member) throws IOException {  
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      OrdersDao orderdao = sqlSession.getMapper(OrdersDao.class);
       String encodepassword = hashPassword(member.getPassword());
       member.setPassword(encodepassword);
       dao.updateRow(member);
       int changelevel=0;
      int totalorder = orderdao.AdminselectTotalOrder(member.getEmail());
      if(0<=totalorder && totalorder<50000) {
         changelevel = 5;
      }else if(50000<=totalorder && totalorder<200000) {
         changelevel = 4;
      }else if(200000<=totalorder && totalorder<500000) {
         changelevel = 3;
      }else if(500000<=totalorder) {
         changelevel = 2;
      }
      orderdao.AdminupdateMemlevel("sessionemail",changelevel);
      
       return "redirect:memberList";
   }
   
	//--------------- 이메일 중복확인
   @RequestMapping(value = "/emailConfirmAjax", method = RequestMethod.POST)
   @ResponseBody
   public String emailConfirmAjax(@RequestParam String email) throws Exception {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      Member member = dao.selectOne(email);
      
      String row ="y";
      if(member == null) {
         row = "n";
      }
      return row;
   }
	//---------------회원리스트에서정보 수정
   @RequestMapping(value = "/memberUpdateAjax", method = RequestMethod.POST)
   @ResponseBody
   public String memberUpdateAjax(@RequestParam String email,int level) throws Exception {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      member.setEmail(email);
      member.setLevel(level);
      int result = dao.updateAjax(member);
      if(result > 0) {
         return  "y";
      }
      else {
         return "n";
      }
   }
	//---------------회원삭제
   @RequestMapping(value = "/memberDeleteAjax", method = RequestMethod.POST)
   @ResponseBody
   public String memberDeleteAjax(@RequestParam String email) throws Exception {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      member.setEmail(email);
      int result = dao.updateAjax(member);
      if(result > 0) {
         return  "y";
      }
      else {
         return "n";
      }
   }
   
	//---------------비밀번호 암호화
   private String hashPassword(String plainTextPassword) {
      
      return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
   }
	//---------------비번번호 찾기버튼
   @RequestMapping(value = "/passwordFind", method = RequestMethod.GET)
   public String passwordFind(Locale locale, Model model) {
      return "login/password_find";
   }
	//---------------비밀번호 찾기
   @RequestMapping(value = "/passwordFindUP", method = RequestMethod.POST)
   @ResponseBody
   public String passwordFindUP(Model model,@RequestParam String email,@RequestParam int birth,@RequestParam int gender) throws Exception {
      MemberDao dao = sqlSession.getMapper(MemberDao.class);
      String result = "";
      int pwchk = dao.selectPWFind(email,gender,birth);
      if(pwchk == 0) {
         result = "n";
      }else {
         result = "y";
      }
      return result;
   }
 //---------------비밀번호 변경넘어가기
   @RequestMapping(value = "/passwordChange", method = RequestMethod.GET)
   public String passwordChange(Locale locale, Model model,@RequestParam String email) {
	   model.addAttribute("email",email);
	   return "login/password_change";
   }
 //---------------비밀번호 변경
   @RequestMapping(value = "/passwordChangeUP", method = RequestMethod.POST)
   @ResponseBody
   public String passwordChangeUP(Model model,HttpSession session,@RequestParam String email,@RequestParam String password) throws IOException {
	   MemberDao dao = sqlSession.getMapper(MemberDao.class);
      String encodepassword = hashPassword(password);
      dao.updatePW(encodepassword, email);
      String result = "y";
      return result;
  }
}
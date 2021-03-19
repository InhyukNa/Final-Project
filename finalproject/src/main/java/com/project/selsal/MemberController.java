package com.project.selsal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
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
import com.project.selsal.entities.Orders;


@Controller
public class MemberController {
	
	
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	Member member;
	@Autowired
	Orders orders;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Locale locale, Model model) {
		return "index";
	}
	
	
	
	@RequestMapping(value = "/memberLogin", method = RequestMethod.GET)
	public String memberLogin(Locale locale, Model model) {
		
		return "login/login";
	}
	
	
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
	@RequestMapping(value = "/IdFindUP", method = RequestMethod.POST)
	@ResponseBody
	public String IdFindUP(Model model,@RequestParam String name,@RequestParam int birth,@RequestParam int gender) throws Exception {
		MemberDao dao = sqlSession.getMapper(MemberDao.class);
		String email = dao.selectIdFind(name,gender,birth);
		return email;
	}
	
	@RequestMapping(value = "/memberlogout", method = RequestMethod.GET)
	public String memberlogout(HttpSession session) {
		session.invalidate();
		return "index";
	}
	
	@RequestMapping(value = "/memberInsert", method = RequestMethod.GET)
	public String memberInsert() {
		return "member/member_insert";
	}
	
	
	@RequestMapping(value = "/memberInsertSave", method = RequestMethod.POST)
	public String memberInsertSave(Model model,@ModelAttribute Member member ) {
		MemberDao daomember = sqlSession.getMapper(MemberDao.class);
		
		String encodepassword = hashPassword(member.getPassword());
		member.setPassword(encodepassword);
		String authNum = randomNum();
		String content = "회원가입을 환영합니다."+"인증번호 :"+authNum;
		sendEmail(member.getEmail(), content, authNum);
		
		daomember.insertRow(member);
		
		
		return "index";
	}
	@RequestMapping(value = "/membermypage", method = RequestMethod.GET)
	public String membermypage(Locale locale, Model model,HttpSession session) throws Exception {
		MemberDao dao = sqlSession.getMapper(MemberDao.class);
		String email = (String) session.getAttribute("sessionemail");
		ArrayList<Member> members = dao.selectAll();
		Member data = dao.selectOne(email);
		ArrayList<Orders> orders =  dao.orderselectAll(email);
	    model.addAttribute("orders",orders);
		model.addAttribute("data",data);
		model.addAttribute("members", members);
		return "member/member_mypage";
	}
	
	@RequestMapping(value = "/memberList", method = RequestMethod.GET)
		public String memberList(Locale locale,Model model) {
		MemberDao dao = sqlSession.getMapper(MemberDao.class);
		ArrayList<Member> members = dao.selectAll();
		model.addAttribute("members",members);
		return "member/member_list";
	}
	
	@RequestMapping(value = "/memberUpdate", method = RequestMethod.GET)
	public String memberUpdate(Locale locale, Model model, HttpSession session) throws Exception {
		String email = (String) session.getAttribute("sessionemail");
		MemberDao dao = sqlSession.getMapper(MemberDao.class);
		Member row = dao.selectOne(email);
		model.addAttribute("row",row);
		return "member/member_update";
	}
	
	@RequestMapping(value = "/memberUpdateSave", method = RequestMethod.POST)
    public String memberUpdateSave( Model model,HttpSession session, @ModelAttribute Member member) throws IOException {  
		MemberDao dao = sqlSession.getMapper(MemberDao.class);
    	String encodepassword = hashPassword(member.getPassword());
 		member.setPassword(encodepassword);
    	dao.updateRow(member);    	
    	return "redirect:membermypage";
	}
	

	@RequestMapping(value = "/adminUpdate", method = RequestMethod.GET)
	public String adminUpdate(Locale locale, Model model, @RequestParam String email, HttpSession session) throws Exception {
		MemberDao dao = sqlSession.getMapper(MemberDao.class);
		Member row = dao.selectOne(email);
		model.addAttribute("row",row);
		return "member/member_updateAdmin";
	}
	
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
	
	
	private String hashPassword(String plainTextPassword) {
		
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}

	private void sendEmail(String email, String content, String authNum) {
        String host = "smtp.gmail.com";
        String subject = "mysite 인증번호";
        String fromName = "관리자";
        String from = "testtest5129@gmail.com";
        String to1 = email;
        try {
            Properties props = new Properties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", host);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.port", "587"); // or 465
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.auth", "true");

            Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("testtest5129", "oucygfisetvhifnk"); // 위 gmail에서 생성된 비밀번호 넣는다
                }
            });
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from, MimeUtility.encodeText(fromName, "UTF-8", "B")));

            InternetAddress[] address1 = { new InternetAddress(to1) };
            msg.setRecipients(Message.RecipientType.TO, address1);
            msg.setSubject(subject);
            msg.setSentDate(new java.util.Date());
            msg.setContent(content, "text/html;charset=euc-kr");
            Transport.send(msg);
        } catch (Exception e) {
        }
    }
	
	String randomNum() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i <= 3; i++) {
            int n = (int) (Math.random() * 10);
            buffer.append(n);
        }
        return buffer.toString();
    }
	
	public static String getRandomPassword(int len) { 
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		int idx = 0; 
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) { 
			idx = (int) (charSet.length * Math.random()); // 36 * 생성된 난수를 Int로 추출 (소숫점제거) 
			sb.append(charSet[idx]); 
		}
		return sb.toString();
	}
	
	@RequestMapping(value = "/PWFindUP", method = RequestMethod.POST)
	@ResponseBody
	public String PWFindUP(Model model,@RequestParam String email,@RequestParam int birth,@RequestParam int gender) throws Exception {
		MemberDao dao = sqlSession.getMapper(MemberDao.class);
		String result = "";
		String newPW = "";
		int pwchk = dao.selectPWFind(email,gender,birth);
		if(pwchk == 0 ) {
			result = "n";
		}else {
			result = getRandomPassword(10);
			newPW = hashPassword(result);
			member.setPassword(newPW);
			dao.updatePW(newPW, email, gender, birth);
			
		}
		return result;
	}
}

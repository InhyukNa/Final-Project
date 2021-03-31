package com.project.selsal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.project.selsal.dao.BoardDao;
import com.project.selsal.entities.BoardPaging;
import com.project.selsal.entities.Freeboard;
import com.project.selsal.entities.Freeboardcomment;


@Controller
public class BoardController {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	Freeboard freeboard;
	
	@Autowired
	Freeboardcomment freeboardcomment;
	
	@Autowired
	BoardPaging boardpaging;
	
	public static String find;

	
	//게시글 작성 페이지
	@RequestMapping(value = "/freeBoardWrite", method = RequestMethod.GET)
	public String freeBoard(Locale locale, Model model) {
		return "board/freeboard_write";
	}
	
	//게시글 등록
	@RequestMapping(value = "/freeBoardWriteSave", method = RequestMethod.POST)
	public String freeboardWriteSave(Model model, @ModelAttribute Freeboard freeboard,
			@RequestParam("f_attachfile") MultipartFile f_attachfile, HttpServletRequest request) throws Exception {
		String filename = f_attachfile.getOriginalFilename();
		String path = "/home/team2/uploadattaches/";
		String realpath = "/uploadattaches/";
		if (!filename.equals("")) {
			byte bytes[] = f_attachfile.getBytes();
			try {
				BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(path + filename));
				output.write(bytes);
				output.flush();
				output.close();
				freeboard.setF_attach(realpath + filename);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm:ss");
		Date date = new Date();
		String today = df.format(date);
		freeboard.setF_inputtime(today);
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		dao.insertRowFreeBoard(freeboard);

		return "redirect:freeBoardList";
	}
	
	//게시글 목록
	@RequestMapping(value = "/freeBoardList", method = RequestMethod.GET)
	public String freeBoardList(Locale locale, Model model) throws Exception {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);

		int rowcount = dao.selectCountFirstFreeBoard();
		int pagesize = 10;
		int page = 1;
		int startrow = (page - 1) * pagesize;
		int endrow = 10;
		if (boardpaging.getFind() == null) {
			boardpaging.setFind("");
		}
		boardpaging.setStartrow(startrow);
		boardpaging.setEndrow(endrow);
		int absPage = 1;
		if (rowcount % pagesize == 0) {
			absPage = 0;
		}

		int pagecount = rowcount / pagesize + absPage;
		int pages[] = new int[pagecount];
		for (int i = 0; i < pagecount; i++) {
			pages[i] = i + 1;
		}

		ArrayList<Freeboard> boards = dao.selectPageListFreeBoard(boardpaging);
		model.addAttribute("rowcount",rowcount);
		model.addAttribute("boards", boards);
		model.addAttribute("pages", pages);
		return "board/freeboard_list";
	}
	
	//게시글 인기순
	@RequestMapping(value = "/freeBoardListPopular", method = RequestMethod.GET)
	public String freeBoardListPopular(Locale locale, Model model) throws Exception {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);

		int rowcount = dao.selectCountFirstFreeBoard();
		int pagesize = 10;
		int page = 1;
		int startrow = (page - 1) * pagesize;
		int endrow = 10;
		if (boardpaging.getFind() == null) {
			boardpaging.setFind("");
		}
		boardpaging.setStartrow(startrow);
		boardpaging.setEndrow(endrow);
		int absPage = 1;
		if (rowcount % pagesize == 0) {
			absPage = 0;
		}

		int pagecount = rowcount / pagesize + absPage;
		int pages[] = new int[pagecount];
		for (int i = 0; i < pagecount; i++) {
			pages[i] = i + 1;
		}

		ArrayList<Freeboard> boards = dao.selectPageListFreeBoardPopular(boardpaging);
		model.addAttribute("rowcount",rowcount);
		model.addAttribute("boards", boards);
		model.addAttribute("pages", pages);
		return "board/freeboard_list2";
	}
	
	//게시글 본문
	@RequestMapping(value = "/freeBoardDetail", method = RequestMethod.GET)
	public String freeBoardDetail(@RequestParam int f_seq, Model model, HttpSession session) throws Exception {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		freeboard = dao.selectOneFreeBoard(f_seq);
		String f_attach = freeboard.getF_attach(); 
		freeboard.setF_attach("home/team2"+f_attach);
		String cursession = (String) session.getAttribute("sessionemail");
		if (cursession == null || !cursession.equals(freeboard.getF_email())) {
			dao.addHitFreeBoard(f_seq);
		}
		int like = dao.selectLike(f_seq);
		freeboard.setF_sessionemail(cursession);
		int likecheck = dao.selectLikeCheck(freeboard);
		ArrayList<Freeboardcomment> freeboardcomments = dao.selectOneFreeBoardComment(f_seq);
		model.addAttribute("freeboard", freeboard);
		model.addAttribute("like",like);
		model.addAttribute("likecheck",likecheck);
		model.addAttribute("freeboardcomments",freeboardcomments);
		model.addAttribute("freeboardcomment",freeboardcomment);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String today = df.format(date);
		int todayYear = Integer.parseInt(today.substring(0,4));
		int todayMonth = Integer.parseInt(today.substring(5,7));
		int todayDay = Integer.parseInt(today.substring(8,10));
		int todayHour = Integer.parseInt(today.substring(11,13));
		int todayMinute = Integer.parseInt(today.substring(14,16));
		int todaySecond = Integer.parseInt(today.substring(17,19));
		
		for(Freeboardcomment a : freeboardcomments) {
			String commentdate = a.getComment_date();
			int commentYear = Integer.parseInt(commentdate.substring(0,4));
			int commentMonth = Integer.parseInt(commentdate.substring(5,7));
			int commentDay = Integer.parseInt(commentdate.substring(8,10));
			int commentHour = Integer.parseInt(commentdate.substring(11,13));
			int commentMinute = Integer.parseInt(commentdate.substring(14,16));
			int commentSecond = Integer.parseInt(commentdate.substring(17,19));
			if((todayYear-commentYear)==1) { //지난 년도
				if(todayMonth<commentMonth) {
					int month = 12 + todayMonth - commentMonth;
					a.setComment_date(month+"개월 전");
				}else {
					int year = todayYear-commentYear;
					a.setComment_date(year+"년 전");
				}
			}else if((todayYear-commentYear)>1) {
				int year = todayYear-commentYear;
				a.setComment_date(year+"년 전");
			}else { //같은 년도
				if((todayMonth-commentMonth)==1) { //지난 월
					if(todayDay<commentDay) {
						int day = 30 + todayDay - commentDay;
						a.setComment_date(day+"일 전");
					}else {
						int month = todayMonth-commentMonth;
						a.setComment_date(month+"개월 전");
					}
				}else if((todayMonth-commentMonth)>1) {
					int month = todayMonth-commentMonth;
					a.setComment_date(month+"개월 전");
				}else { //같은 월
					if((todayDay-commentDay)==1) { //지난 일
						if(todayHour<commentHour) {
							int hour = 24 + todayHour - commentHour;
							a.setComment_date(hour+"시간 전");
						}else {
							int day = todayDay-commentDay;
							a.setComment_date(day+"일 전");
						}
					}else if((todayDay-commentDay)>1) {
						int day = todayDay-commentDay;
						a.setComment_date(day+"일 전");
					}else { //같은 일
						if((todayHour-commentHour)==1) { //지난 시간
							if(todayMinute<commentMinute) {
								int minute = 60 + todayMinute - commentMinute;
								a.setComment_date(minute+"분 전");
							}else {
								int hour = todayHour-commentHour;
								a.setComment_date(hour+"시간 전");
							}
						}else if((todayHour-commentHour)>1) {
							int hour = todayHour-commentHour;
							a.setComment_date(hour+"시간 전");
						}else { 
							if((todayMinute-commentMinute)==1) { //1분 아래
								if(todaySecond<commentSecond) {
									a.setComment_date("방금 전");
								}else { 
									int minute = todayMinute-commentMinute;
									a.setComment_date(minute+"분 전");
								}
							}else if((todayMinute-commentMinute)>1) { //1분 이상
								int minute = todayMinute-commentMinute;
								a.setComment_date(minute+"분 전");
							}else { 
								a.setComment_date("방금 전");
							}
						}
					}
				}
			}
		}
		return "board/freeboard_detail";
	}
	
	//파일 다운로드
	@RequestMapping(value = "/fileDownloadFreeBoard")
	@ResponseBody
	public void fileDownloadFreeBoard(@RequestParam String f_attach, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		request.setCharacterEncoding("UTF-8");
		File file = new File("/home/team2/"+f_attach);
		System.out.println(file.length());
		String oriFileName = file.getName();
		String filePath = "/home/team2/uploadattaches/";
		InputStream in = null;
		OutputStream os = null;
		File newfile = null;
		boolean skip = false;
		String client = "";
		// 파일을 읽어 스트림에 담기
		try {
			newfile = new File(filePath, oriFileName);
			in = new FileInputStream(newfile);
		} catch (FileNotFoundException fe) {
			skip = true;
		}

		client = request.getHeader("User-Agent");
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Description", "HTML Generated Data");

		if (!skip) {
			// IE
			if (client.indexOf("MSIE") != -1) {
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
				// IE 11 이상.
			} else if (client.indexOf("Trident") != -1) {
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
			} else {
				// 한글 파일명 처리
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + new String(oriFileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
				response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
			}
			response.setHeader("Content-Length", "" + file.length());
			os = response.getOutputStream();
			byte b[] = new byte[(int) file.length()];
			int leng = 0;
			while ((leng = in.read(b)) > 0) {
				os.write(b, 0, leng);
			}
		} else {
			response.setContentType("text/html;charset=UTF-8");
			System.out.println("<script language='javascript'>alert('파일을 찾을 수 없습니다');history.back();</script>");
		}
		in.close();
		os.close();
	}
	
	//게시판 수정
	@RequestMapping(value = "/freeBoardUpdateSave", method = RequestMethod.POST)
	public String freeBoardUpdateSave(@ModelAttribute Freeboard freeboard) throws Exception {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		dao.updateRowFreeBoard(freeboard);
		return "redirect:freeBoardList";
	}
	
	//게시판 삭제
	@RequestMapping(value = "/freeBoardDelete", method = RequestMethod.GET)
	public String freeBoardDelete(@RequestParam int f_seq) throws Exception {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		dao.deleteRowFreeBoard(f_seq);
		return "redirect:freeBoardList";
	}
	
	//게시판 검색
	@RequestMapping(value = "/findListFreeBoard", method = RequestMethod.POST)
	public String findListFreeBoard(Locale locale, Model model, @RequestParam String find) throws Exception {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);

		int pagesize = 10;
		int page = 1;
		int startrow = (page - 1) * pagesize;
		int endrow = 10;
		boardpaging.setFind(find);
		if (boardpaging.getFind() == null) {
			boardpaging.setFind("");
		}
		boardpaging.setStartrow(startrow);
		boardpaging.setEndrow(endrow);
		int rowcount = dao.selectCountFreeBoard(boardpaging);
		int absPage = 1;
		if (rowcount % pagesize == 0) {
			absPage = 0;
		}

		int pagecount = rowcount / pagesize + absPage;
		int pages[] = new int[pagecount];
		for (int i = 0; i < pagecount; i++) {
			pages[i] = i + 1;
		}

		ArrayList<Freeboard> boards = dao.findListFreeBoard(boardpaging);
		model.addAttribute("boards", boards);
		model.addAttribute("pages", pages);
		model.addAttribute("find", find);

		return "board/freeboard_list";
	}
	
	//게시판 페이징
	@RequestMapping(value = "/freeBoardPageSelect", method = RequestMethod.GET)
	public String freeBoardPageSelect(Locale locale, Model model, @RequestParam int page) throws Exception {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);

		int pagesize = 10;
		int startrow = (page - 1) * pagesize;
		int endrow = 10;
		boardpaging.setFind(find);
		if (boardpaging.getFind() == null) {
			boardpaging.setFind("");
		}
		boardpaging.setStartrow(startrow);
		boardpaging.setEndrow(endrow);
		int rowcount = dao.selectCountFreeBoard(boardpaging);
		int absPage = 1;
		if (rowcount % pagesize == 0) {
			absPage = 0;
		}

		int pagecount = rowcount / pagesize + absPage;
		int pages[] = new int[pagecount];
		for (int i = 0; i < pagecount; i++) {
			pages[i] = i + 1;
		}

		ArrayList<Freeboard> boards = dao.findListFreeBoard(boardpaging);
		model.addAttribute("boards", boards);
		model.addAttribute("pages", pages);
		model.addAttribute("find", find);

		return "board/freeboard_list";
	}
	
	//게시글 좋아요
	@RequestMapping(value = "/freeBoardLike", method = RequestMethod.POST)
	@ResponseBody
	public HashMap freeBoardLike(@RequestParam int f_seq, HttpSession session) throws Exception {
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		freeboard = dao.selectOneFreeBoard(f_seq);
		String cursession = (String) session.getAttribute("sessionemail");
		String result = "";
		if(cursession == null) {
			result = "n";
		} else {
			result = "y";
		}
		freeboard.setF_sessionemail(cursession);
		freeboard.setF_seq(f_seq);
		int likecheck = dao.selectLikeCheck(freeboard);
		hashmap.put("likecheck", likecheck);
		if (likecheck == 0) {
			dao.freeBoardLikeInsert(freeboard);
		} else {
			dao.freeBoardLikeDelete(freeboard);
		}
		int selectlike = dao.selectLike(f_seq);
		hashmap.put("selectlike", selectlike);
		hashmap.put("result", result);
		return hashmap;
	}
	
	//게시글 수정
	@RequestMapping(value = "/freeBoardUpdate", method = RequestMethod.GET)
	public String freeBoardUpdate(Model model,@RequestParam int f_seq) throws Exception {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		freeboard = dao.selectOneFreeBoard(f_seq);
		model.addAttribute("freeboard", freeboard);
		return "board/freeboard_update";
	}
	
	//게시글 댓글
	@RequestMapping(value = "/freeBoardComment", method = RequestMethod.POST)
	public String freeBoardComment(Model model, @ModelAttribute Freeboardcomment freeboardcomment, HttpServletRequest request,@RequestParam int f_seq,HttpSession session) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String today = df.format(date);
		freeboardcomment.setComment_date(today);
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		dao.insertRowFreeBoardComment(freeboardcomment);
		freeboard = dao.selectOneFreeBoard(f_seq);
		String cursession = (String) session.getAttribute("sessionemail");
		if (!cursession.equals(freeboard.getF_email())) {
			dao.addHitFreeBoard(f_seq);
		}
		int like = dao.selectLike(f_seq);
		freeboard.setF_sessionemail(cursession);
		int likecheck = dao.selectLikeCheck(freeboard);
		ArrayList<Freeboardcomment> freeboardcomments = dao.selectOneFreeBoardComment(f_seq);
		model.addAttribute("freeboard", freeboard);
		model.addAttribute("like",like);
		model.addAttribute("likecheck",likecheck);
		model.addAttribute("freeboardcomments",freeboardcomments);
		
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date2 = new Date();
		String today2 = df2.format(date2);
		int todayYear = Integer.parseInt(today2.substring(0,4));
		int todayMonth = Integer.parseInt(today2.substring(5,7));
		int todayDay = Integer.parseInt(today2.substring(8,10));
		int todayHour = Integer.parseInt(today2.substring(11,13));
		int todayMinute = Integer.parseInt(today2.substring(14,16));
		int todaySecond = Integer.parseInt(today2.substring(17,19));
		
		for(Freeboardcomment a : freeboardcomments) {
			String commentdate = a.getComment_date();
			int commentYear = Integer.parseInt(commentdate.substring(0,4));
			int commentMonth = Integer.parseInt(commentdate.substring(5,7));
			int commentDay = Integer.parseInt(commentdate.substring(8,10));
			int commentHour = Integer.parseInt(commentdate.substring(11,13));
			int commentMinute = Integer.parseInt(commentdate.substring(14,16));
			int commentSecond = Integer.parseInt(commentdate.substring(17,19));
			if((todayYear-commentYear)==1) { //지난 년도
				if(todayMonth<commentMonth) {
					int month = 12 + todayMonth - commentMonth;
					a.setComment_date(month+"개월 전");
				}else {
					int year = todayYear-commentYear;
					a.setComment_date(year+"년 전");
				}
			}else if((todayYear-commentYear)>1) {
				int year = todayYear-commentYear;
				a.setComment_date(year+"년 전");
			}else { //같은 년도
				if((todayMonth-commentMonth)==1) { //지난 월
					if(todayDay<commentDay) {
						int day = 30 + todayDay - commentDay;
						a.setComment_date(day+"일 전");
					}else {
						int month = todayMonth-commentMonth;
						a.setComment_date(month+"개월 전");
					}
				}else if((todayMonth-commentMonth)>1) {
					int month = todayMonth-commentMonth;
					a.setComment_date(month+"개월 전");
				}else { //같은 월
					if((todayDay-commentDay)==1) { //지난 일
						if(todayHour<commentHour) {
							int hour = 24 + todayHour - commentHour;
							a.setComment_date(hour+"시간 전");
						}else {
							int day = todayDay-commentDay;
							a.setComment_date(day+"일 전");
						}
					}else if((todayDay-commentDay)>1) {
						int day = todayDay-commentDay;
						a.setComment_date(day+"일 전");
					}else { //같은 일
						if((todayHour-commentHour)==1) { //지난 시간
							if(todayMinute<commentMinute) {
								int minute = 60 + todayMinute - commentMinute;
								a.setComment_date(minute+"분 전");
							}else {
								int hour = todayHour-commentHour;
								a.setComment_date(hour+"시간 전");
							}
						}else if((todayHour-commentHour)>1) {
							int hour = todayHour-commentHour;
							a.setComment_date(hour+"시간 전");
						}else { 
							if((todayMinute-commentMinute)==1) { //1분 아래
								if(todaySecond<commentSecond) {
									a.setComment_date("방금 전");
								}else { 
									int minute = todayMinute-commentMinute;
									a.setComment_date(minute+"분 전");
								}
							}else if((todayMinute-commentMinute)>1) { //1분 이상
								int minute = todayMinute-commentMinute;
								a.setComment_date(minute+"분 전");
							}else { 
								a.setComment_date("방금 전");
							}
						}
					}
				}
			}
		}
		return "board/freeboard_detail";
	}

	//댓글 삭제
	@RequestMapping(value = "/freeBoardcommentDelete", method = RequestMethod.POST)
	@ResponseBody
	public String freeBoardcommentDelete(@RequestParam int comment_num, @RequestParam int f_seq) throws Exception {
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		int result = dao.deleteRowFreeBoardComment(comment_num);
		if (result > 0) {
			return "y";
		} else {
			return "n";
		}
	}
}

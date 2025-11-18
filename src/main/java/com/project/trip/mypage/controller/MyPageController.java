package com.project.trip.mypage.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.mapper.MyPageMapper;
import com.project.trip.mypage.model.AccomReservationViewDTO;
import com.project.trip.mypage.model.BoardDTO;
import com.project.trip.mypage.model.CarReservationViewDTO;
import com.project.trip.mypage.model.UserDTO;
import com.project.trip.mypage.model.UserRouteViewDTO;

import lombok.RequiredArgsConstructor;

/**
 * 마이페이지 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * 사용자 정보 조회 및 수정, 회원 탈퇴, 활동 내역 (게시글, 댓글, 좋아요, 스크랩),
 * 숙소 및 렌터카 예약 내역, 사용자 루트 조회 등의 기능을 제공합니다.
 */
@Controller
@RequiredArgsConstructor
public class MyPageController {

    @Autowired
    private final MyPageMapper pagemapper;
    private final MemberMapper membermapper;
    private final PasswordEncoder encoder;


	
	/**
	 * 마이페이지 메인 화면을 반환합니다.
	 *
	 * @return 마이페이지 메인 화면의 뷰 이름
	 */
	@GetMapping("/member/mypage")
	public String mypage() {
		
		return "mypage.member.mypage";
	}
	

	
	/**
	 * 현재 로그인한 사용자의 상세 정보를 조회하여 뷰에 전달합니다.
	 *
	 * @param authentication Spring Security의 Authentication 객체
	 * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	 * @return 사용자 정보 페이지의 뷰 이름
	 */
	@GetMapping("/member/userinfo")
	    public String userInfo(Authentication authentication, Model model) {
		
	    String username = authentication.getName();
	    
	    UserDTO dto = membermapper.get(username);
	    model.addAttribute("dto", dto);

			return "mypage.member.userinfo";
	    }
	
	
	   /**
	    * 현재 로그인한 사용자의 정보 수정 폼 페이지를 반환합니다.
	    *
	    * @param authentication Spring Security의 Authentication 객체
	    * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	    * @return 사용자 정보 수정 폼 페이지의 뷰 이름
	    */
	   @GetMapping("/member/useredit")
	    public String userEdit(Authentication authentication, Model model) {
	        
		    String username = authentication.getName();
	        
		    UserDTO dto = membermapper.get(username);
	        
	        model.addAttribute("dto", dto);

	        return "mypage.member.useredit";
	    }
	    
	   /**
	    * 사용자 정보 수정 요청을 처리합니다.
	    * 비밀번호가 입력된 경우 암호화하여 업데이트하고, 수정 결과를 사용자에게 알립니다.
	    *
	    * @param dto 수정할 사용자 정보를 담은 {@link UserDTO} 객체
	    * @param authentication Spring Security의 Authentication 객체
	    * @param resp HTTP 응답 객체
	    * @throws IOException 응답 작성 중 발생할 수 있는 예외
	    */
	   @PostMapping("/member/useredit")
	    public void userEditOk(UserDTO dto, Authentication authentication, HttpServletResponse resp) throws IOException {
	        
	        // 인증된 사용자 ID 가져오기
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);

	        dto.setSeq(userInfo.getSeq());
	        
	        // 비밀번호 암호화
	        if (dto.getPw() != null && !dto.getPw().isEmpty()) {
	            dto.setPw(encoder.encode(dto.getPw()));
	        }
	        
	        System.out.println(dto);
	        
	        int result = membermapper.userEdit(dto);
	        
	        resp.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = resp.getWriter();
	        
	        if (result > 0) {
	            out.print("<html><meta charset='UTF-8'><script>");
	            out.print("alert('수정이 완료되었습니다.');");
	            out.print("location.href='/trip/member/userinfo.do';");
	            out.print("</script></html>");
	        } else {
	            out.print("<html><meta charset='UTF-8'><script>");
	            out.print("alert('수정에 실패했습니다.');");
	            out.print("history.back();");
	            out.print("</script></html>");
	        }
	        
	        out.close();
	    }
	
	   
	   /**
	    * 회원 탈퇴 폼 페이지를 반환합니다.
	    *
	    * @param authentication Spring Security의 Authentication 객체
	    * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	    * @return 회원 탈퇴 폼 페이지의 뷰 이름
	    */
	   @GetMapping("/member/userdel")
	    public String userDel(Authentication authentication, Model model) {
	        
	        String username = authentication.getName();
	        
	        // 사용자 정보 조회
	        UserDTO userInfo = membermapper.get(username);
	        
	        model.addAttribute("seq", userInfo.getSeq());

	        return "mypage.member.userdel";
	    }
	   
	   /**
	    * 회원 탈퇴 요청을 처리합니다.
	    * 입력된 비밀번호를 확인하고, 일치하는 경우 회원 정보를 삭제하고 세션을 무효화합니다.
	    *
	    * @param pw 사용자가 입력한 비밀번호
	    * @param reason 탈퇴 사유
	    * @param authentication Spring Security의 Authentication 객체
	    * @param session HTTP 세션 객체
	    * @param resp HTTP 응답 객체
	    * @throws IOException 응답 작성 중 발생할 수 있는 예외
	    */
	   @PostMapping("/member/userdel")
	    public void userDelOk(@RequestParam("pw") String pw,
	                          @RequestParam("name") String reason,
	                          Authentication authentication, 
	                          HttpSession session,
	                          HttpServletResponse resp) throws IOException {
	        
	        String username = authentication.getName();
	        
	        // 사용자 정보 조회
	        UserDTO userInfo = membermapper.get(username);
	        
	        System.out.println("입력한 비밀번호: " + pw);
	        System.out.println("탈퇴 사유: " + reason);
	        
	        // 비밀번호 확인 (암호화된 비밀번호와 비교)
	        boolean isPasswordMatch = encoder.matches(pw, userInfo.getPw());
	        
	        System.out.println("비밀번호 일치 여부: " + isPasswordMatch);
	        
	        resp.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = resp.getWriter();
	        
	        if (isPasswordMatch) {
	            // 회원 탈퇴
	            membermapper.userDel(userInfo.getSeq());
	            
	            // 세션 무효화 (로그아웃)
	            session.invalidate();
	            
	            out.print("<html><meta charset='UTF-8'><script>");
	            out.print("alert('탈퇴가 완료되었습니다.');");
	            out.print("location.href='/trip/';");
	            out.print("</script></html>");
	        } else {
	            out.print("<html><meta charset='UTF-8'><script>");
	            out.print("alert('비밀번호를 다시 확인해주세요.');");
	            out.print("history.back();");
	            out.print("</script></html>");
	        }
	        
	        out.close();
	    }
	   
	   
	   /**
	    * 현재 로그인한 사용자의 게시글 활동 내역을 조회하여 뷰에 전달합니다.
	    * 검색 및 페이징 기능을 지원합니다.
	    *
	    * @param column 검색할 컬럼 (예: "title")
	    * @param word 검색 키워드
	    * @param page 현재 페이지 번호 (기본값: 1)
	    * @param authentication Spring Security의 Authentication 객체
	    * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	    * @return 게시글 활동 내역 페이지의 뷰 이름
	    */
	   @GetMapping("/member/boardactivities")
	    public String boardActivities(
	            @RequestParam(value = "column", required = false) String column,
	            @RequestParam(value = "word", required = false) String word,
	            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	            Authentication authentication,
	            Model model) {
	        
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);
	        String seq = String.valueOf(userInfo.getSeq());
	        
	        // 검색 여부 판단
	        String search = "n";
	        if (column != null && word != null && !word.trim().equals("")) {
	            search = "y";
	        }
	        
	        // 검색 파라미터 설정
	        Map<String, String> map = new HashMap<>();
	        
	        if ("y".equals(search)) {
	            map.put("column", "title"); // 공통 컬럼명
	        } else {
	            map.put("column", column);
	        }
	        map.put("word", word);
	        map.put("search", search);
	        map.put("seq", seq);
	        
	        // 페이징 설정
	        int pageSize = 10;
	        int begin = ((page - 1) * pageSize) + 1;
	        int end = begin + pageSize - 1;
	        
	        map.put("begin", String.valueOf(begin));
	        map.put("end", String.valueOf(end));
	        map.put("nowPage", String.valueOf(page));
	        
	        System.out.println(map);
	        
	        // 총 게시물 수 조회
	        int totalCount = pagemapper.getAllBoardTotalCount(map);
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	        
	        map.put("totalCount", String.valueOf(totalCount));
	        map.put("totalPage", String.valueOf(totalPage));
	        
	        // 게시물 목록 조회
	        List<BoardDTO> list = pagemapper.totalBoardList(map);
	        
	        // 데이터 가공
	        Calendar now = Calendar.getInstance();
	        String nowDate = String.format("%tF", now);
	        
	        for (BoardDTO dto : list) {
	            // 제목 자르기
	            String subject = dto.getSubject();
	            if (subject.length() > 15) {
	                subject = subject.substring(0, 15) + "..";
	            }
	            
	            // 태그 비활성화
	            subject = subject.replace("<", "&lt;").replace(">", "&gt;");
	            dto.setSubject(subject);
	        }
	        
	        // 페이지바 생성
	        String pagebar = generatePageBar(page, totalPage, 10);
	        
	        model.addAttribute("list", list);
	        model.addAttribute("map", map);
	        model.addAttribute("pagebar", pagebar);
	        
	        return "mypage.member.boardactivities";
	    }
	    
	    /**
	     * 게시글 목록의 페이지 바 HTML을 생성합니다.
	     *
	     * @param nowPage 현재 페이지 번호
	     * @param totalPage 전체 페이지 수
	     * @param blockSize 페이지 블록 크기
	     * @return 생성된 페이지 바 HTML 문자열
	     */
	    private String generatePageBar(int nowPage, int totalPage, int blockSize) {
	        StringBuilder pagebar = new StringBuilder();
	        
	        int loop = 1;
	        int n = ((nowPage - 1) / blockSize) * blockSize + 1;
	        
	        // 이전 버튼
	        if (n == 1) {
	            pagebar.append(" <a href='#!'>이전</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/boardactivities?page=%d'>이전</a> ", n - 1));
	        }
	        
	        // 페이지 번호
	        while (!(loop > blockSize || n > totalPage)) {
	            if (n == nowPage) {
	                pagebar.append(String.format(" <a href='#!' style='color:tomato;' class='page'>%d</a> ", n));
	            } else {
	                pagebar.append(String.format(" <a href='/trip/member/boardactivities?page=%d' class='page'>%d</a> ", n, n));
	            }
	            loop++;
	            n++;
	        }
	        
	        // 다음 버튼
	        if (n > totalPage) {
	            pagebar.append(" <a href='#!'>다음</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/boardactivities?page=%d'>다음</a> ", n));
	        }
	        
	        return pagebar.toString();
	    }
	   
	    
	    
	    /**
	     * 현재 로그인한 사용자의 댓글 활동 내역을 조회하여 뷰에 전달합니다.
	     * 검색 및 페이징 기능을 지원합니다.
	     *
	     * @param column 검색할 컬럼 (예: "title")
	     * @param word 검색 키워드
	     * @param page 현재 페이지 번호 (기본값: 1)
	     * @param authentication Spring Security의 Authentication 객체
	     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	     * @return 댓글 활동 내역 페이지의 뷰 이름
	     */
	    @GetMapping("/member/commentactivities")
	    public String commentActivities(
	            @RequestParam(value = "column", required = false) String column,
	            @RequestParam(value = "word", required = false) String word,
	            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	            Authentication authentication,
	            Model model) {
	        
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);
	        String seq = String.valueOf(userInfo.getSeq());
	        
	        // 검색 여부 판단
	        String search = "n";
	        if (column != null && word != null && !word.trim().equals("")) {
	            search = "y";
	        }
	        
	        // 검색 파라미터 설정
	        Map<String, String> map = new HashMap<>();
	        
	        if ("y".equals(search)) {
	            map.put("column", "title"); // 공통 컬럼명
	        } else {
	            map.put("column", column);
	        }
	        map.put("word", word);
	        map.put("search", search);
	        map.put("seq", seq);
	        
	        // 페이징 설정
	        int pageSize = 10;
	        int begin = ((page - 1) * pageSize) + 1;
	        int end = begin + pageSize - 1;
	        
	        map.put("begin", String.valueOf(begin));
	        map.put("end", String.valueOf(end));
	        map.put("nowPage", String.valueOf(page));
	        
	        System.out.println(map);
	        
	        // 총 댓글 수 조회
	        int totalCount = pagemapper.getAllCommentTotalCount(map);
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	        
	        map.put("totalCount", String.valueOf(totalCount));
	        map.put("totalPage", String.valueOf(totalPage));
	        
	        // 댓글 목록 조회
	        List<BoardDTO> list = pagemapper.totalCommentList(map);
	        System.out.println(list);
	        
	        // 데이터 가공
	        for (BoardDTO dto : list) {
	            // 제목 자르기
	            String subject = dto.getSubject();
	            if (subject != null && subject.length() > 15) {
	                subject = subject.substring(0, 15) + "..";
	            }
	            
	            // 태그 비활성화
	            if (subject != null) {
	                subject = subject.replace("<", "&lt;").replace(">", "&gt;");
	            }
	            dto.setSubject(subject);
	        }
	        
	        // 페이지바 생성
	        String pagebar = generateCommentPageBar(page, totalPage, 10);
	        
	        model.addAttribute("list", list);
	        model.addAttribute("map", map);
	        model.addAttribute("pagebar", pagebar);
	        
	        return "mypage.member.commentactivities";
	    }

	    /**
	     * 댓글 목록의 페이지 바 HTML을 생성합니다.
	     *
	     * @param nowPage 현재 페이지 번호
	     * @param totalPage 전체 페이지 수
	     * @param blockSize 페이지 블록 크기
	     * @return 생성된 페이지 바 HTML 문자열
	     */
	    private String generateCommentPageBar(int nowPage, int totalPage, int blockSize) {
	        StringBuilder pagebar = new StringBuilder();
	        
	        int loop = 1;
	        int n = ((nowPage - 1) / blockSize) * blockSize + 1;
	        
	        // 이전 버튼
	        if (n == 1) {
	            pagebar.append(" <a href='#!'>이전</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/commentactivities?page=%d'>이전</a> ", n - 1));
	        }
	        
	        // 페이지 번호
	        while (!(loop > blockSize || n > totalPage)) {
	            if (n == nowPage) {
	                pagebar.append(String.format(" <a href='#!' style='color:tomato;' class='page'>%d</a> ", n));
	            } else {
	                pagebar.append(String.format(" <a href='/trip/member/commentactivities?page=%d' class='page'>%d</a> ", n, n));
	            }
	            loop++;
	            n++;
	        }
	        
	        // 다음 버튼
	        if (n > totalPage) {
	            pagebar.append(" <a href='#!'>다음</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/commentactivities?page=%d'>다음</a> ", n));
	        }
	        
	        return pagebar.toString();
	    }
	    
	    
	    /**
	     * 현재 로그인한 사용자의 좋아요 활동 내역을 조회하여 뷰에 전달합니다.
	     * 검색 및 페이징 기능을 지원합니다.
	     *
	     * @param column 검색할 컬럼 (예: "title")
	     * @param word 검색 키워드
	     * @param page 현재 페이지 번호 (기본값: 1)
	     * @param authentication Spring Security의 Authentication 객체
	     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	     * @return 좋아요 활동 내역 페이지의 뷰 이름
	     */
	    @GetMapping("/member/likeactivities")
	    public String likeActivities(
	            @RequestParam(value = "column", required = false) String column,
	            @RequestParam(value = "word", required = false) String word,
	            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	            Authentication authentication,
	            Model model) {
	        
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);
	        String seq = String.valueOf(userInfo.getSeq());
	        
	        // 검색 여부 판단
	        String search = "n";
	        if (column != null && word != null && !word.trim().equals("")) {
	            search = "y";
	        }
	        
	        // 검색 파라미터 설정
	        Map<String, String> map = new HashMap<>();
	        
	        if ("y".equals(search)) {
	            map.put("column", "title"); // 공통 컬럼명
	        } else {
	            map.put("column", column);
	        }
	        map.put("word", word);
	        map.put("search", search);
	        map.put("seq", seq);
	        
	        // 페이징 설정
	        int pageSize = 10;
	        int begin = ((page - 1) * pageSize) + 1;
	        int end = begin + pageSize - 1;
	        
	        map.put("begin", String.valueOf(begin));
	        map.put("end", String.valueOf(end));
	        map.put("nowPage", String.valueOf(page));
	        
	        System.out.println(map);
	        
	        // 총 좋아요 수 조회
	        int totalCount = pagemapper.getAllLikeTotalCount(map);
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	        
	        map.put("totalCount", String.valueOf(totalCount));
	        map.put("totalPage", String.valueOf(totalPage));
	        
	        // 좋아요 목록 조회
	        List<BoardDTO> list = pagemapper.totalLikeList(map);
	        
	        // 데이터 가공
	        for (BoardDTO dto : list) {
	            // 제목 자르기
	            String subject = dto.getSubject();
	            if (subject != null && subject.length() > 15) {
	                subject = subject.substring(0, 15) + "..";
	            }
	            
	            // 태그 비활성화
	            if (subject != null) {
	                subject = subject.replace("<", "&lt;").replace(">", "&gt;");
	            }
	            dto.setSubject(subject);
	        }
	        
	        // 페이지바 생성
	        String pagebar = generateLikePageBar(page, totalPage, 10);
	        
	        model.addAttribute("list", list);
	        model.addAttribute("map", map);
	        model.addAttribute("pagebar", pagebar);
	        
	        return "mypage.member.likeactivities";
	    }

	    /**
	     * 좋아요 활동 내역 목록의 페이지 바 HTML을 생성합니다.
	     *
	     * @param nowPage 현재 페이지 번호
	     * @param totalPage 전체 페이지 수
	     * @param blockSize 페이지 블록 크기
	     * @return 생성된 페이지 바 HTML 문자열
	     */
	    private String generateLikePageBar(int nowPage, int totalPage, int blockSize) {
	        StringBuilder pagebar = new StringBuilder();
	        
	        int loop = 1;
	        int n = ((nowPage - 1) / blockSize) * blockSize + 1;
	        
	        // 이전 버튼
	        if (n == 1) {
	            pagebar.append(" <a href='#!'>이전</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/likeactivities?page=%d'>이전</a> ", n - 1));
	        }
	        
	        // 페이지 번호
	        while (!(loop > blockSize || n > totalPage)) {
	            if (n == nowPage) {
	                pagebar.append(String.format(" <a href='#!' style='color:tomato;' class='page'>%d</a> ", n));
	            } else {
	                pagebar.append(String.format(" <a href='/trip/member/likeactivities?page=%d' class='page'>%d</a> ", n, n));
	            }
	            loop++;
	            n++;
	        }
	        
	        // 다음 버튼
	        if (n > totalPage) {
	            pagebar.append(" <a href='#!'>다음</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/likeactivities?page=%d'>다음</a> ", n));
	        }
	        
	        return pagebar.toString();
	    }
	    
	    
	    /**
	     * 현재 로그인한 사용자의 스크랩 활동 내역을 조회하여 뷰에 전달합니다.
	     * 검색 및 페이징 기능을 지원합니다.
	     *
	     * @param column 검색할 컬럼 (예: "title")
	     * @param word 검색 키워드
	     * @param page 현재 페이지 번호 (기본값: 1)
	     * @param authentication Spring Security의 Authentication 객체
	     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	     * @return 스크랩 활동 내역 페이지의 뷰 이름
	     */
	    @GetMapping("/member/scrapactivities")
	    public String scrapActivities(
	            @RequestParam(value = "column", required = false) String column,
	            @RequestParam(value = "word", required = false) String word,
	            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	            Authentication authentication,
	            Model model) {
	        
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);
	        String seq = String.valueOf(userInfo.getSeq());
	        
	        // 검색 여부 판단
	        String search = "n";
	        if (column != null && word != null && !word.trim().equals("")) {
	            search = "y";
	        }
	        
	        // 검색 파라미터 설정
	        Map<String, String> map = new HashMap<>();
	        
	        if ("y".equals(search)) {
	            map.put("column", "title"); // 공통 컬럼명
	        } else {
	            map.put("column", column);
	        }
	        map.put("word", word);
	        map.put("search", search);
	        map.put("seq", seq);
	        
	        // 페이징 설정
	        int pageSize = 10;
	        int begin = ((page - 1) * pageSize) + 1;
	        int end = begin + pageSize - 1;
	        
	        map.put("begin", String.valueOf(begin));
	        map.put("end", String.valueOf(end));
	        map.put("nowPage", String.valueOf(page));
	        
	        System.out.println(map);
	        
	        // 총 스크랩 수 조회
	        int totalCount = pagemapper.getAllScrapTotalCount(map);
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	        
	        map.put("totalCount", String.valueOf(totalCount));
	        map.put("totalPage", String.valueOf(totalPage));
	        
	        // 스크랩 목록 조회
	        List<BoardDTO> list = pagemapper.totalScrapList(map);
	        
	        // 데이터 가공
	        for (BoardDTO dto : list) {
	            // 제목 자르기
	            String subject = dto.getSubject();
	            if (subject != null && subject.length() > 15) {
	                subject = subject.substring(0, 15) + "..";
	            }
	            
	            // 태그 비활성화
	            if (subject != null) {
	                subject = subject.replace("<", "&lt;").replace(">", "&gt;");
	            }
	            dto.setSubject(subject);
	        }
	        
	        // 페이지바 생성
	        String pagebar = generateScrapPageBar(page, totalPage, 10);
	        
	        model.addAttribute("list", list);
	        model.addAttribute("map", map);
	        model.addAttribute("pagebar", pagebar);
	        
	        return "mypage.member.scrapactivities";
	    }

	    /**
	     * 스크랩 활동 내역 목록의 페이지 바 HTML을 생성합니다.
	     *
	     * @param nowPage 현재 페이지 번호
	     * @param totalPage 전체 페이지 수
	     * @param blockSize 페이지 블록 크기
	     * @return 생성된 페이지 바 HTML 문자열
	     */
	    private String generateScrapPageBar(int nowPage, int totalPage, int blockSize) {
	        StringBuilder pagebar = new StringBuilder();
	        
	        int loop = 1;
	        int n = ((nowPage - 1) / blockSize) * blockSize + 1;
	        
	        // 이전 버튼
	        if (n == 1) {
	            pagebar.append(" <a href='#!'>이전</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/scrapactivities?page=%d'>이전</a> ", n - 1));
	        }
	        
	        // 페이지 번호
	        while (!(loop > blockSize || n > totalPage)) {
	            if (n == nowPage) {
	                pagebar.append(String.format(" <a href='#!' style='color:tomato;' class='page'>%d</a> ", n));
	            } else {
	                pagebar.append(String.format(" <a href='/trip/member/scrapactivities?page=%d' class='page'>%d</a> ", n, n));
	            }
	            loop++;
	            n++;
	        }
	        
	        // 다음 버튼
	        if (n > totalPage) {
	            pagebar.append(" <a href='#!'>다음</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/scrapactivities?page=%d'>다음</a> ", n));
	        }
	        
	        return pagebar.toString();
	    }
	    
	    
	    /**
	     * 현재 로그인한 사용자의 숙소 예약 내역을 조회하여 뷰에 전달합니다.
	     * 검색 및 페이징 기능을 지원합니다.
	     *
	     * @param column 검색할 컬럼 (예: "title")
	     * @param word 검색 키워드
	     * @param page 현재 페이지 번호 (기본값: 1)
	     * @param authentication Spring Security의 Authentication 객체
	     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	     * @return 숙소 예약 내역 페이지의 뷰 이름
	     */
	    @GetMapping("/member/accomreservation")
	    public String accomReservation(
	            @RequestParam(value = "column", required = false) String column,
	            @RequestParam(value = "word", required = false) String word,
	            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	            Authentication authentication,
	            Model model) {
	        
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);
	        String seq = String.valueOf(userInfo.getSeq());
	        
	        // 검색 여부 판단
	        String search = "n";
	        if (column != null && word != null && !word.trim().equals("")) {
	            search = "y";
	        }
	        
	        // 검색 파라미터 설정
	        Map<String, String> map = new HashMap<>();
	        
	        if ("y".equals(search)) {
	            map.put("column", "title"); // 공통 컬럼명
	        } else {
	            map.put("column", column);
	        }
	        map.put("word", word);
	        map.put("search", search);
	        map.put("seq", seq);
	        
	        // 페이징 설정
	        int pageSize = 10;
	        int begin = ((page - 1) * pageSize) + 1;
	        int end = begin + pageSize - 1;
	        
	        map.put("begin", String.valueOf(begin));
	        map.put("end", String.valueOf(end));
	        map.put("nowPage", String.valueOf(page));
	        
	        System.out.println(map);
	        
	        // 총 예약 수 조회
	        int totalCount = pagemapper.getAccomReservationTotalCount(map);
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	        
	        map.put("totalCount", String.valueOf(totalCount));
	        map.put("totalPage", String.valueOf(totalPage));
	        
	        // 예약 목록 조회
	        List<AccomReservationViewDTO> list = pagemapper.totalAccomList(map);
	        
	        // 페이지바 생성
	        String pagebar = generateAccomPageBar(page, totalPage, 10);
	        
	        model.addAttribute("list", list);
	        model.addAttribute("map", map);
	        model.addAttribute("pagebar", pagebar);
	        
	        return "mypage.member.accomreservation";
	    }

	    /**
	     * 숙소 예약 내역 목록의 페이지 바 HTML을 생성합니다.
	     *
	     * @param nowPage 현재 페이지 번호
	     * @param totalPage 전체 페이지 수
	     * @param blockSize 페이지 블록 크기
	     * @return 생성된 페이지 바 HTML 문자열
	     */
	    private String generateAccomPageBar(int nowPage, int totalPage, int blockSize) {
	        StringBuilder pagebar = new StringBuilder();
	        
	        int loop = 1;
	        int n = ((nowPage - 1) / blockSize) * blockSize + 1;
	        
	        // 이전 버튼
	        if (n == 1) {
	            pagebar.append(" <a href='#!'>이전</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/accomreservation?page=%d'>이전</a> ", n - 1));
	        }
	        
	        // 페이지 번호
	        while (!(loop > blockSize || n > totalPage)) {
	            if (n == nowPage) {
	                pagebar.append(String.format(" <a href='#!' style='color:tomato;' class='page'>%d</a> ", n));
	            } else {
	                pagebar.append(String.format(" <a href='/trip/member/accomreservation?page=%d' class='page'>%d</a> ", n, n));
	            }
	            loop++;
	            n++;
	        }
	        
	        // 다음 버튼
	        if (n > totalPage) {
	            pagebar.append(" <a href='#!'>다음</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/accomreservation?page=%d'>다음</a> ", n));
	        }
	        
	        return pagebar.toString();
	    }
	    
	    /**
	     * 특정 숙소 예약의 상세 정보를 조회하여 뷰에 전달합니다.
	     *
	     * @param seq 예약 고유 번호
	     * @param accomseq 숙소 고유 번호
	     * @param authentication Spring Security의 Authentication 객체
	     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	     * @return 숙소 예약 상세 정보 페이지의 뷰 이름
	     */
	    @GetMapping("/member/accomreservationview")
	    public String accomReservationView(
	            @RequestParam("seq") String seq,
	            @RequestParam("accomseq") String accomseq,
	            Authentication authentication,
	            Model model) {
	        
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);
	        String useq = String.valueOf(userInfo.getSeq());
	        
	        // 예약 상세 정보 조회
	        AccomReservationViewDTO dto = pagemapper.getAccomReservation(seq, accomseq);
	        
	        model.addAttribute("dto", dto);
	        
	        return "mypage.member.accomreservationview";
	    }
	    
	    
	    /**
	     * 숙소 예약을 취소 처리합니다.
	     *
	     * @param accomseq 취소할 숙소 예약의 고유 번호
	     * @return "success" 또는 "error" 문자열
	     */
	    @PostMapping("/member/accomcancel")
	    @ResponseBody
	    public String accomCancel(@RequestParam("accomseq") String accomseq) {
	        
	        try {
	            // 예약 취소 처리
	            pagemapper.addAccomCancel(accomseq);
	            
	            return "success";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "error";
	        }
	    }
	
	
	    /**
	     * 현재 로그인한 사용자의 렌터카 예약 내역을 조회하여 뷰에 전달합니다.
	     * 검색 및 페이징 기능을 지원합니다.
	     *
	     * @param column 검색할 컬럼 (예: "title")
	     * @param word 검색 키워드
	     * @param page 현재 페이지 번호 (기본값: 1)
	     * @param authentication Spring Security의 Authentication 객체
	     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	     * @return 렌터카 예약 내역 페이지의 뷰 이름
	     */
	    @GetMapping("/member/carreservation")
	    public String carReservation(
	            @RequestParam(value = "column", required = false) String column,
	            @RequestParam(value = "word", required = false) String word,
	            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	            Authentication authentication,
	            Model model) {
	        
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);
	        String seq = String.valueOf(userInfo.getSeq());
	        
	        // 검색 여부 판단
	        String search = "n";
	        if (column != null && word != null && !word.trim().equals("")) {
	            search = "y";
	        }
	        
	        // 검색 파라미터 설정
	        Map<String, String> map = new HashMap<>();
	        
	        if ("y".equals(search)) {
	            map.put("column", "title");
	        } else {
	            map.put("column", column);
	        }
	        map.put("word", word);
	        map.put("search", search);
	        map.put("seq", seq);
	        
	        // 페이징 설정
	        int pageSize = 10;
	        int begin = ((page - 1) * pageSize) + 1;
	        int end = begin + pageSize - 1;
	        
	        map.put("begin", String.valueOf(begin));
	        map.put("end", String.valueOf(end));
	        map.put("nowPage", String.valueOf(page));
	        
	        System.out.println(map);
	        
	        // 총 예약 수 조회
	        int totalCount = pagemapper.getCarReservationTotalCount(map);
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	        
	        map.put("totalCount", String.valueOf(totalCount));
	        map.put("totalPage", String.valueOf(totalPage));
	        
	        // 예약 목록 조회
	        List<CarReservationViewDTO> list = pagemapper.totalCarList(map);
	        
	        // 페이지바 생성
	        String pagebar = generateCarPageBar(page, totalPage, 10);
	        
	        model.addAttribute("list", list);
	        model.addAttribute("map", map);
	        model.addAttribute("pagebar", pagebar);
	        
	        return "mypage.member.carreservation";
	    }

	    /**
	     * 렌터카 예약 내역 목록의 페이지 바 HTML을 생성합니다.
	     *
	     * @param nowPage 현재 페이지 번호
	     * @param totalPage 전체 페이지 수
	     * @param blockSize 페이지 블록 크기
	     * @return 생성된 페이지 바 HTML 문자열
	     */
	    private String generateCarPageBar(int nowPage, int totalPage, int blockSize) {
	        StringBuilder pagebar = new StringBuilder();
	        
	        int loop = 1;
	        int n = ((nowPage - 1) / blockSize) * blockSize + 1;
	        
	        // 이전 버튼
	        if (n == 1) {
	            pagebar.append(" <a href='#!'>이전</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/carreservation?page=%d'>이전</a> ", n - 1));
	        }
	        
	        // 페이지 번호
	        while (!(loop > blockSize || n > totalPage)) {
	            if (n == nowPage) {
	                pagebar.append(String.format(" <a href='#!' style='color:tomato;' class='page'>%d</a> ", n));
	            } else {
	                pagebar.append(String.format(" <a href='/trip/member/carreservation?page=%d' class='page'>%d</a> ", n, n));
	            }
	            loop++;
	            n++;
	        }
	        
	        // 다음 버튼
	        if (n > totalPage) {
	            pagebar.append(" <a href='#!'>다음</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/member/carreservation?page=%d'>다음</a> ", n));
	        }
	        
	        return pagebar.toString();
	    }
	    
	    
	    /**
	     * 특정 렌터카 예약의 상세 정보를 조회하여 뷰에 전달합니다.
	     *
	     * @param seq 예약 고유 번호
	     * @param carseq 렌터카 고유 번호
	     * @param authentication Spring Security의 Authentication 객체
	     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	     * @return 렌터카 예약 상세 정보 페이지의 뷰 이름
	     */
	    @GetMapping("/member/carreservationview")
	    public String carReservationView(
	            @RequestParam("seq") String seq,
	            @RequestParam("carseq") String carseq,
	            Authentication authentication,
	            Model model) {
	        
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);
	        
	        System.out.println("carseq 테스트:" + carseq);
	        
	        // 예약 상세 정보 조회
	        CarReservationViewDTO dto = pagemapper.getCarReservation(seq, carseq);
	        
	        model.addAttribute("dto", dto);
	        
	        return "mypage.member.carreservationview";
	    }

	    // 렌트카 예약 취소
	    /**
	     * 렌터카 예약을 취소 처리합니다.
	     *
	     * @param carseq 취소할 렌터카 예약의 고유 번호
	     * @return "success" 또는 "error" 문자열
	     */
	    @PostMapping("/member/carcancel")
	    @ResponseBody
	    public String carCancel(@RequestParam("carseq") String carseq) {
	        
	        try {
	            // 예약 취소 처리
	            pagemapper.addCarCancel(carseq);
	            
	            return "success";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "error";
	        }
	    }
	    
	    /**
	     * 현재 로그인한 사용자의 루트 목록을 조회하여 뷰에 전달합니다.
	     * 페이징 기능을 지원하며, AJAX 요청 시 JSON 형태로 데이터를 반환합니다.
	     *
	     * @param page 현재 페이지 번호 (기본값: 1)
	     * @param ajax AJAX 요청 여부 ("true"인 경우 JSON 반환)
	     * @param authentication Spring Security의 Authentication 객체
	     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
	     * @param response HTTP 응답 객체
	     * @return 사용자 루트 목록 페이지의 뷰 이름 또는 JSON 데이터
	     * @throws Exception JSON 변환 또는 응답 작성 중 발생할 수 있는 예외
	     */
	    @GetMapping("/member/userroute")
	    public String userRoute(
	            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	            @RequestParam(value = "ajax", required = false) String ajax,
	            Authentication authentication,
	            Model model,
	            HttpServletResponse response) throws Exception {
	        
	        String username = authentication.getName();
	        UserDTO userInfo = membermapper.get(username);
	        String seq = String.valueOf(userInfo.getSeq());
	        
	        // 페이징 설정
	        int pageSize = 5; // 한 페이지에 5개
	        int begin = ((page - 1) * pageSize) + 1;
	        int end = begin + pageSize - 1;
	        
	        // Map 설정
	        Map<String, String> map = new HashMap<>();
	        map.put("seq", seq);
	        map.put("begin", String.valueOf(begin));
	        map.put("end", String.valueOf(end));
	        map.put("search", "n");
	        
	        // 루트 목록 조회
	        List<UserRouteViewDTO> list = pagemapper.UserRouteList(map);
	        
	        // AJAX 요청인 경우 JSON으로 반환
	        if ("true".equals(ajax)) {
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            
	            // Jackson 라이브러리로 JSON 변환
	            ObjectMapper mapper = new ObjectMapper();
	            String jsonResult = mapper.writeValueAsString(list);
	            
	            response.getWriter().print(jsonResult);
	            return null; // View를 반환하지 않음
	        }
	        
	        // 일반 요청인 경우
	        int totalCount = pagemapper.getUserRouteTotalCount(map);
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	        
	        model.addAttribute("list", list);
	        model.addAttribute("nowPage", page);
	        model.addAttribute("totalPage", totalPage);
	        
	        return "mypage.member.userroute";
	    }
}

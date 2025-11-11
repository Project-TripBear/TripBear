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

import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.mapper.MyPageMapper;
import com.project.trip.mypage.model.BoardDTO;
import com.project.trip.mypage.model.UserDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    @Autowired
    private final MyPageMapper pagemapper;
    private final MemberMapper membermapper;
    private final PasswordEncoder encoder;


	
	@GetMapping("/member/mypage")
	public String mypage() {
		
		return "mypage.member.mypage";
	}
	

	
	@GetMapping("/member/userinfo")
	    public String userInfo(Authentication authentication, Model model) {
		
	    String username = authentication.getName();
	    
	    UserDTO dto = membermapper.get(username);
	    model.addAttribute("dto", dto);

			return "mypage.member.userinfo";
	    }
	
	
	   @GetMapping("/member/useredit")
	    public String userEdit(Authentication authentication, Model model) {
	        
		    String username = authentication.getName();
	        
		    UserDTO dto = membermapper.get(username);
	        
	        model.addAttribute("dto", dto);

	        return "mypage.member.useredit";
	    }
	    
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
	
	   
	   @GetMapping("/member/userdel")
	    public String userDel(Authentication authentication, Model model) {
	        
	        String username = authentication.getName();
	        
	        // 사용자 정보 조회
	        UserDTO userInfo = membermapper.get(username);
	        
	        model.addAttribute("seq", userInfo.getSeq());

	        return "mypage.member.userdel";
	    }
	   
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
	
	
	
}

package com.project.trip.board.hotdeal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.trip.board.hotdeal.mapper.HotDealLikeMapper;
import com.project.trip.board.hotdeal.mapper.HotDealMapper;
import com.project.trip.board.hotdeal.model.HotDealCommentDTO;
import com.project.trip.board.hotdeal.model.HotDealDTO;
import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.model.UserDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HotDealController {
	 private final HotDealMapper mapper;
	 private final MemberMapper membermapper;
	 private final HotDealLikeMapper likemapper;
	 
	    @GetMapping("/hotdeal/list")
	    public String list(
	            @RequestParam(value = "column", required = false) String column,
	            @RequestParam(value = "word", required = false) String word,
	            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	            Authentication auth,
	            Model model) {

	        // 검색 여부 판단
	        String search = "n";
	        if (column != null && word != null && !word.trim().equals("")) {
	            search = "y";
	        }

	        // 파라미터 맵 설정
	        Map<String, String> map = new HashMap<>();
	        map.put("column", column);
	        map.put("word", word);
	        map.put("search", search);

	        // 페이징 설정
	        int pageSize = 10;
	        int begin = ((page - 1) * pageSize) + 1;
	        int end = begin + pageSize - 1;

	        map.put("begin", String.valueOf(begin));
	        map.put("end", String.valueOf(end));
	        map.put("nowPage", String.valueOf(page));

	        // 총 게시물 수 조회
	        int totalCount = mapper.getBoardTotalCount(map);
	        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	        map.put("totalCount", String.valueOf(totalCount));
	        map.put("totalPage", String.valueOf(totalPage));

	        // 게시글 목록 조회
	        List<HotDealDTO> list = mapper.list(map);

	        // 제목 자르기 및 HTML 태그 비활성화
	        for (HotDealDTO dto : list) {
	            String subject = dto.getSubject();
	            if (subject.length() > 15) {
	                subject = subject.substring(0, 15) + "..";
	            }
	            subject = subject.replace("<", "&lt;").replace(">", "&gt;");
	            dto.setSubject(subject);
	        }

	        // 페이지바 생성
	        String pagebar = generatePageBar(page, totalPage, 10);

	        // 모델에 데이터 추가
	        model.addAttribute("list", list);
	        model.addAttribute("map", map);
	        model.addAttribute("pagebar", pagebar);

	        // 로그인한 사용자 정보
	        if (auth != null) {
	            model.addAttribute("id", auth.getName());
	        }

	        return "board.hotdeal.list";
	    }

	    private String generatePageBar(int nowPage, int totalPage, int blockSize) {
	        StringBuilder pagebar = new StringBuilder();

	        int loop = 1;
	        int n = ((nowPage - 1) / blockSize) * blockSize + 1;

	        // 이전 버튼
	        if (n == 1) {
	            pagebar.append(" <a href='#!'>이전</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/hotdeal/list?page=%d'>이전</a> ", n - 1));
	        }

	        // 페이지 번호
	        while (!(loop > blockSize || n > totalPage)) {
	            if (n == nowPage) {
	                pagebar.append(String.format(" <a href='#!' style='color:tomato;' class='page'>%d</a> ", n));
	            } else {
	                pagebar.append(String.format(" <a href='/trip/hotdeal/list?page=%d' class='page'>%d</a> ", n, n));
	            }
	            loop++;
	            n++;
	        }

	        // 다음 버튼
	        if (n > totalPage) {
	            pagebar.append(" <a href='#!'>다음</a> ");
	        } else {
	            pagebar.append(String.format(" <a href='/trip/hotdeal/list?page=%d'>다음</a> ", n));
	        }

	        return pagebar.toString();
	    }
	    
	
	    
	    
	    
	    @GetMapping("/hotdeal/view")
	    public String view(
	            @RequestParam("seq") String seq,
	            @RequestParam(value = "column", required = false) String column,
	            @RequestParam(value = "word", required = false) String word,
	            Authentication auth,
	            HttpSession session,
	            Model model) {

	        boolean isLiked = false;
	        boolean isScrapped = false;
	        String userSeq = null;
	        String userId = null;

	        try {
	            if (auth != null && auth.isAuthenticated()
	                    && !(auth instanceof AnonymousAuthenticationToken)) {  // 익명사용자 제외
	                userId = auth.getName();
	                System.out.println("userId: " + userId + seq);
	                
	                UserDTO userDto = membermapper.get(userId);

	                if (userDto == null) {
	                    System.out.println("membermapper.get(userId) returned null for userId: " + userId);
	                    model.addAttribute("loginId", null);
	                } else {
	                    model.addAttribute("loginId", userId);
	                    userSeq = userDto.getSeq();

	                    if (userSeq != null) {
	                        isLiked = likemapper.likeCheck(userSeq, seq) == 1;
	                        isScrapped = likemapper.scrapCheck(userSeq, seq) == 1;
	                    }
	                }
	            } else {
	                model.addAttribute("loginId", null);
	            }

	            // 조회수 증가 처리
	            if ("n".equals(session.getAttribute("read"))) {
	                mapper.updateReadcount(seq);
	                session.setAttribute("read", "y");
	            }

	            // 게시글 조회
	            HotDealDTO dto = mapper.get(seq);

	            if (dto == null) {
	                throw new RuntimeException("게시글이 존재하지 않습니다. seq: " + seq);
	            }

	            // HTML 태그 변환 (XSS 방지)
	            String subject = dto.getSubject() != null ? dto.getSubject().replace("<", "&lt;").replace(">", "&gt;") : "";
	            dto.setSubject(subject);

	            String content = dto.getContent() != null ? dto.getContent().replace("<", "&lt;").replace(">", "&gt;") : "";
	            dto.setContent(content);

	            // 댓글 조회
	            List<HotDealCommentDTO> clist = mapper.listComment(seq);
	            System.out.println("댓글리스트:" + clist);
	            if (clist == null) {
	                clist = new ArrayList<>();
	            }
	            System.out.println("dto: " + dto);
	            // 모델 데이터 설정
	            model.addAttribute("dto", dto);
	            model.addAttribute("column", column);
	            model.addAttribute("word", word);
	            model.addAttribute("clist", clist);
	            model.addAttribute("isLiked", isLiked);
	            model.addAttribute("isScrapped", isScrapped);
	            model.addAttribute("id", userId);

	            return "board.hotdeal.view";
	        } catch (Exception e) {
	            // 예외 발생 로그 및 처리
	            System.err.println("Exception in view method: " + e.getMessage());
	            e.printStackTrace();

	            // 에러 페이지나 리다이렉트 처리 선택 가능
	            return "error.page"; // 필요시 적절한 에러 페이지로 변경
	        }
	    }
	 
	}

	


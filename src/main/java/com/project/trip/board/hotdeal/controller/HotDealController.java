package com.project.trip.board.hotdeal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.trip.board.hotdeal.mapper.HotDealLikeMapper;
import com.project.trip.board.hotdeal.mapper.HotDealMapper;
import com.project.trip.board.hotdeal.model.HotDealCommentDTO;
import com.project.trip.board.hotdeal.model.HotDealDTO;
import com.project.trip.board.hotdeal.model.HotDealImageDTO;
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
	        
	        List<HotDealImageDTO> images = mapper.selectImages(seq);
	        model.addAttribute("images", images);

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
	            model.addAttribute("useq", userSeq);

	            return "board.hotdeal.view";
	        } catch (Exception e) {
	            // 예외 발생 로그 및 처리
	            System.err.println("Exception in view method: " + e.getMessage());
	            e.printStackTrace();

	            // 에러 페이지나 리다이렉트 처리 선택 가능
	            return "error.page"; // 필요시 적절한 에러 페이지로 변경
	        }
	    }
	    
	    
	    @GetMapping("/hotdeal/add")
	    public String addForm() {
	        return "board.hotdeal.add"; // src/main/webapp/WEB-INF/views/board/add.jsp 와 매칭
	    }

	    @PostMapping("/hotdeal/add")
	    public String addPost(
	            @RequestParam("subject") String subject,
	            @RequestParam("content") String content,
	            @RequestParam("imgs") MultipartFile[] imgFiles,
	            @RequestParam("status") String status,
	            @RequestParam("category") String category,
	            @RequestParam("itemname") String itemname,
	            @RequestParam("price") String price,
	            @RequestParam("url") String url,
	            Authentication auth,
	            Model model) throws IOException {

	        String userId = auth.getName();
	        UserDTO userdto = membermapper.get(userId);
	        HotDealDTO dto = new HotDealDTO();
	        dto.setSubject(subject);
	        dto.setContent(content);
	        dto.setStatus(status);
	        dto.setCategory(category);
	        dto.setItemName(itemname);
	        dto.setPrice(price);
	        dto.setUrl(url);
	        dto.setUseq(userdto.getSeq());
	        
	        System.out.println("test123:"+ dto);
	        	
	        
	        // 게시글은 반드시 한 번만 등록
	        int result = mapper.insertBoard(dto);
	        // insertBoard()가 useGeneratedKeys="true", keyProperty="seq"로 설정되어야 DTO에 시퀀스가 바로 들어감

	        if (result > 0) {
	        	Long hotdealId = Long.parseLong(mapper.selectRecentSeq(dto)); 
	            int imgResultSum = 0;
	            int imageSeq = 1; // 이미지 순서 컬럼 값(필요시)
	            for (MultipartFile imgFile : imgFiles) {
	            	 if (imgFile != null && !imgFile.isEmpty()) {
	                     String savedFileName = imgFile.getOriginalFilename();
	                     
	                     Map<String, Object> param = new HashMap<>();
	                     param.put("hotdealId", hotdealId);
	                     param.put("img", savedFileName);
	                     param.put("hotdealImageSeq", imageSeq++);
	                     
	                     mapper.insertBoardImage(param);
	                 }
	             }
	             return "redirect:/hotdeal/list";
	         }
	         
	         model.addAttribute("error", "게시물 등록 실패");
	         return "board.hotdeal.add";
	    }
	    
	 // GET: 수정 폼 보기
	    @GetMapping("/hotdeal/edit")
	    public String editForm(@RequestParam("seq") String seq, Model model, Authentication auth) {
	        String userId = auth.getName();
	        
	        // 게시글 정보 조회 (기존 get 메서드 사용)
	        HotDealDTO dto = mapper.get(seq);
	        
	        // 권한 체크
	        if (!dto.getId().equals(userId)) {
	            model.addAttribute("error", "수정 권한이 없습니다.");
	            return "redirect:/hotdeal/list";
	        }
	        
	        // 기존 이미지 목록 조회
	        List<HotDealImageDTO> images = mapper.selectImages(seq);
	        
	        model.addAttribute("dto", dto);
	        model.addAttribute("images", images);
	        
	        return "board.hotdeal.edit";
	    }

	    // POST: 수정 처리
	    @PostMapping("/hotdeal/edit")
	    public String editPost(
	            @RequestParam("seq") String seq,
	            @RequestParam("subject") String subject,
	            @RequestParam("content") String content,
	            @RequestParam(value = "imgs", required = false) MultipartFile[] imgFiles,
	            @RequestParam("status") String status,
	            @RequestParam("category") String category,
	            @RequestParam("itemname") String itemname,
	            @RequestParam("price") String price,
	            @RequestParam("url") String url,
	            @RequestParam(value = "deleteImages", required = false) String[] deleteImages,
	            Authentication auth,
	            Model model) throws IOException {

	        String userId = auth.getName();
	        
	        // 권한 체크
	        HotDealDTO existingDto = mapper.get(seq);
	        if (!existingDto.getId().equals(userId)) {
	            model.addAttribute("error", "수정 권한이 없습니다.");
	            return "redirect:/hotdeal/list";
	        }
	        
	        // 게시글 수정
	        HotDealDTO dto = new HotDealDTO();
	        dto.setSeq(seq);
	        dto.setSubject(subject);
	        dto.setContent(content);
	        dto.setStatus(status);
	        dto.setCategory(category);
	        dto.setItemName(itemname);
	        dto.setPrice(price);
	        dto.setUrl(url);
	        
	        int result = mapper.updateBoard(dto);
	        
	        if (result > 0) {
	            // 삭제할 이미지 처리
	            if (deleteImages != null && deleteImages.length > 0) {
	                for (String imageId : deleteImages) {
	                    mapper.deleteImage(imageId);
	                }
	            }
	            
	            // 새 이미지 추가
	            if (imgFiles != null && imgFiles.length > 0) {
	                int maxSeq = mapper.selectMaxImageSeq(seq);
	                int imageSeq = maxSeq + 1;
	                
	                for (MultipartFile imgFile : imgFiles) {
	                    if (imgFile != null && !imgFile.isEmpty()) {
	                        String savedFileName = imgFile.getOriginalFilename();
	                        
	                        Map<String, Object> param = new HashMap<>();
	                        param.put("hotdealId", seq);
	                        param.put("img", savedFileName);
	                        param.put("hotdealImageSeq", imageSeq++);
	                        
	                        mapper.insertBoardImage(param);
	                    }
	                }
	            }
	            
	            return "redirect:/hotdeal/view?seq=" + seq;
	        }
	        
	        model.addAttribute("error", "수정 실패");
	        return "board.hotdeal.edit";
	    }
	    
	    
	 // GET: 삭제 확인 페이지
	    @GetMapping("/hotdeal/del")
	    public String delForm(@RequestParam("seq") String seq, Model model, Authentication auth) {
	        String userId = auth.getName();
	        
	        // 게시글 정보 조회
	        HotDealDTO dto = mapper.get(seq);
	        
	        // 권한 체크
	        if (!dto.getId().equals(userId)) {
	            model.addAttribute("error", "삭제 권한이 없습니다.");
	            return "redirect:/hotdeal/list";
	        }
	        
	        model.addAttribute("seq", seq);
	        return "board.hotdeal.del";
	    }

	    // POST: 삭제 처리
	    @PostMapping("/hotdeal/del")
	    public String delPost(
	            @RequestParam("seq") String seq,
	            Authentication auth,
	            Model model) {
	        
	        String userId = auth.getName();
	        
	        // 권한 체크
	        HotDealDTO dto = mapper.get(seq);
	        if (!dto.getId().equals(userId)) {
	            model.addAttribute("error", "삭제 권한이 없습니다.");
	            return "redirect:/hotdeal/list";
	        }
	        
	        // 1. 이미지 먼저 삭제 (외래키 제약조건 때문에)
	        mapper.deleteAllImages(seq);
	        
	        // 2. 게시글 삭제
	        int result = mapper.deleteBoard(seq);
	        
	        if (result > 0) {
	            return "redirect:/hotdeal/list";
	        } else {
	            model.addAttribute("error", "삭제 실패");
	            model.addAttribute("seq", seq);
	            return "board.hotdeal.del";
	        }
	    }
	 
	}

	


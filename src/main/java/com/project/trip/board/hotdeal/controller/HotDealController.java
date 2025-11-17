package com.project.trip.board.hotdeal.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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
	 
	        /**
	         * 핫딜 게시글 목록을 조회하고 페이징 및 검색 기능을 제공하여 뷰에 전달합니다.
	         *
	         * @param column 검색할 컬럼 (예: "subject", "content")
	         * @param word   검색어
	         * @param page   현재 페이지 번호 (기본값: 1)
	         * @param auth   Spring Security의 Authentication 객체 (로그인 여부 확인용)
	         * @param model  뷰에 데이터를 전달하기 위한 Model 객체
	         * @return "board.hotdeal.list" 핫딜 게시글 목록 뷰 이름
	         */
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
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", totalPage);

                // 로그인한 사용자 정보
                if (auth != null) {
                    model.addAttribute("id", auth.getName());
                }

	        return "board.hotdeal.list";
	    }

	    /**
     * 페이지네이션을 위한 페이지 바 HTML 문자열을 생성하는 헬퍼 함수입니다.
     *
     * @param nowPage   현재 페이지 번호
     * @param totalPage 총 페이지 수
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
	    
	
	    
	    
	    
	        /**
	         * 특정 핫딜 게시글의 상세 내용을 조회하고 뷰에 전달합니다.
	         * 조회수 증가 처리, 로그인한 사용자의 좋아요/스크랩 여부 확인, 댓글 목록 조회 등을 포함합니다.
	         *
	         * @param seq     조회할 게시글의 고유 번호
	         * @param column  검색 컬럼 (이전 검색 조건 유지를 위함)
	         * @param word    검색어 (이전 검색 조건 유지를 위함)
	         * @param auth    Spring Security의 Authentication 객체
	         * @param session HttpSession 객체 (조회수 중복 방지용)
	         * @param model   뷰에 데이터를 전달하기 위한 Model 객체
	         * @return "board.hotdeal.view" 핫딜 게시글 상세 뷰 이름, 또는 에러 발생 시 "error.page"
	         */
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
	        
	        int likeCount = likemapper.getLikeCount(seq);
	        model.addAttribute("likeCount", likeCount);

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
//	            if ("n".equals(session.getAttribute("read"))) {
//	                mapper.updateReadcount(seq);
//	                session.setAttribute("read", "y");
//	            }
	            
	            try {
	                // 1. 세션에서 "viewedPosts"라는 이름의 Set을 가져옵니다.
	                @SuppressWarnings("unchecked") // 타입 변환 경고 무시
	                Set<String> viewedPosts = (Set<String>) session.getAttribute("viewedPosts");

	                // 2. Set이 세션에 없으면(null), 새로 만듭니다.
	                if (viewedPosts == null) {
	                    viewedPosts = new HashSet<>();
	                }

	                // 3. 이 Set에 현재 게시물 번호(seq)가 포함되어 있지 *않다면*
	                if (!viewedPosts.contains(seq)) {
	                    mapper.updateReadcount(seq);      // DB 조회수 증가
	                    viewedPosts.add(seq);               // Set에 현재 게시물 번호 추가
	                    session.setAttribute("viewedPosts", viewedPosts); // Set을 세션에 다시 저장
	                }
	                // 4. Set에 이미 seq가 있다면 (새로고침 등) 아무것도 하지 않습니다.

	            } catch (Exception e) {
	                // 조회수 처리 중 오류가 발생해도 페이지 로드는 계속되어야 하므로 로그만 남깁니다.
	                System.err.println("조회수 증가 처리 중 오류 발생: " + e.getMessage());
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
	    
	    
	        /**
	         * 핫딜 게시글 등록 폼 페이지를 표시합니다.
	         *
	         * @return "board.hotdeal.add" 핫딜 게시글 등록 폼 뷰 이름
	         */
	        @GetMapping("/hotdeal/add")
	        public String addForm() {	        return "board.hotdeal.add"; // src/main/webapp/WEB-INF/views/board/add.jsp 와 매칭
	    }
	        /**
	         * 새로운 핫딜 게시글을 등록하는 요청을 처리합니다.
	         * 게시글 정보와 함께 업로드된 이미지 파일을 처리하고 데이터베이스에 저장합니다.
	         *
	         * @param subject    게시글 제목
	         * @param content    게시글 내용
	         * @param imgFiles   첨부 이미지 파일 배열
	         * @param status     핫딜 상태
	         * @param category   핫딜 카테고리
	         * @param itemname   상품명
	         * @param price      가격
	         * @param url        상품 URL
	         * @param request    HttpServletRequest 객체 (파일 저장 경로 획득용)
	         * @param auth       Spring Security의 Authentication 객체
	         * @param model      에러 발생 시 뷰에 데이터를 전달하기 위한 Model 객체
	         * @return "redirect:/hotdeal/list" 게시글 목록 페이지로 리다이렉트, 또는 "board.hotdeal.add" (등록 실패 시)
	         * @throws IOException 파일 처리 중 발생할 수 있는 예외
	         */
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
	                HttpServletRequest request, // <-- 1. (추가) 파일 경로를 얻기 위해 추가
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

	            String realPath = "C:/tripbear";
	            File uploadDir = new File(realPath);
	            if (!uploadDir.exists()) {
	                uploadDir.mkdirs(); // 폴더가 없으면 생성
	            }
	            int imageSeq = 1; // 이미지 순서 컬럼 값(필요시)
	            for (MultipartFile imgFile : imgFiles) {
	            	 if (imgFile != null && !imgFile.isEmpty()) {
	                     //String savedFileName = imgFile.getOriginalFilename();
	                     
	                  // 3-1. 고유한 파일명 생성 (예: 1678886400000_image.jpg)
	                        String originalFilename = imgFile.getOriginalFilename();
	                        String savedFileName = System.currentTimeMillis() + "_" + originalFilename; 

	                        // 3-2. 위에서 설정한 경로(uploadDir)에 실제 파일 저장
	                        File dest = new File(uploadDir, savedFileName);
	                        imgFile.transferTo(dest);
	                     
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
	    
    /**
     * 핫딜 게시글 수정 폼 페이지를 표시합니다.
     * 로그인한 사용자의 게시글 수정 권한을 확인하고, 권한이 없는 경우 목록 페이지로 리다이렉트합니다.
     *
     * @param seq   수정할 게시글의 고유 번호
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @param auth  Spring Security의 Authentication 객체
     * @return "board.hotdeal.edit" 핫딜 게시글 수정 폼 뷰 이름, 또는 "redirect:/hotdeal/list" (권한 없음)
     */
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

    /**
     * 핫딜 게시글 수정 요청을 처리합니다.
     * 로그인한 사용자의 게시글 수정 권한을 확인하고, 게시글 정보 및 이미지 파일을 업데이트합니다.
     *
     * @param seq          수정할 게시글의 고유 번호
     * @param subject      게시글 제목
     * @param content      게시글 내용
     * @param imgFiles     새로 업로드된 이미지 파일 배열
     * @param status       핫딜 상태
     * @param category     핫딜 카테고리
     * @param itemname     상품명
     * @param price        가격
     * @param url          상품 URL
     * @param deleteImages 삭제할 이미지 ID 배열
     * @param auth         Spring Security의 Authentication 객체
     * @param model        에러 발생 시 뷰에 데이터를 전달하기 위한 Model 객체
     * @return "redirect:/hotdeal/view?seq={seq}" 게시글 상세 페이지로 리다이렉트, 또는 "board.hotdeal.edit" (수정 실패 시)
     * @throws IOException 파일 처리 중 발생할 수 있는 예외
     */
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
            Model model) throws IOException { // throws IOException 확인

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
	            
	            // 새 이미지 추가 (이 부분이 수정되었습니다)
	            if (imgFiles != null && imgFiles.length > 0) {
	                int maxSeq = mapper.selectMaxImageSeq(seq);
	                int imageSeq = maxSeq + 1;

	                // --- (추가) 파일 저장 경로 설정 (addPost와 동일하게) ---
	                String realPath = "C:/tripbear";
	                File uploadDir = new File(realPath);
	                if (!uploadDir.exists()) {
	                    uploadDir.mkdirs(); // 폴더가 없으면 생성
	                }
	                // ----------------------------------------------------
	                
	                for (MultipartFile imgFile : imgFiles) {
	                    if (imgFile != null && !imgFile.isEmpty()) {
	                        
	                        // --- (수정) 고유한 파일명 생성 (addPost와 동일하게) ---
	                        String originalFilename = imgFile.getOriginalFilename();
	                        String savedFileName = System.currentTimeMillis() + "_" + originalFilename; 

	                        // --- (추가) 실제 파일 저장 (addPost와 동일하게) ---
	                        File dest = new File(uploadDir, savedFileName);
	                        imgFile.transferTo(dest);
	                        // -------------------------------------------------
	                        
	                        Map<String, Object> param = new HashMap<>();
	                        param.put("hotdealId", seq);
	                        param.put("img", savedFileName); // (수정) 고유 파일명으로 DB에 저장
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
	    
	    
    /**
     * 핫딜 게시글 삭제 확인 페이지를 표시합니다.
     * 로그인한 사용자의 게시글 삭제 권한을 확인하고, 권한이 없는 경우 목록 페이지로 리다이렉트합니다.
     *
     * @param seq   삭제할 게시글의 고유 번호
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @param auth  Spring Security의 Authentication 객체
     * @return "board.hotdeal.del" 핫딜 게시글 삭제 확인 뷰 이름, 또는 "redirect:/hotdeal/list" (권한 없음)
     */
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

    /**
     * 핫딜 게시글 삭제 요청을 처리합니다.
     * 로그인한 사용자의 게시글 삭제 권한을 확인하고, 게시글과 관련된 모든 데이터(댓글, 좋아요, 스크랩, 이미지)를
     * 먼저 삭제한 후 게시글을 삭제합니다.
     *
     * @param seq   삭제할 게시글의 고유 번호
     * @param auth  Spring Security의 Authentication 객체
     * @param model 에러 발생 시 뷰에 데이터를 전달하기 위한 Model 객체
     * @return "redirect:/hotdeal/list" 게시글 목록 페이지로 리다이렉트, 또는 "board.hotdeal.del" (삭제 실패 시)
     */
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
	        mapper.deleteComment(seq);
	        mapper.deleteLike(seq);
	        mapper.deleteScrap(seq);
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
	    
	        /**
	         * 핫딜 게시글에 대한 좋아요 상태를 토글하는 REST API입니다.
	         * 로그인한 사용자만 좋아요를 누를 수 있으며, 처리 후 좋아요 상태와 개수를 반환합니다.
	         *
	         * @param request 게시글 ID("bseq")를 포함하는 {@code Map<String, String>}
	         * @param auth    Spring Security의 Authentication 객체
	         * @return 처리 결과(result), 좋아요 상태(action), 좋아요 개수(likeCount)를 담은 {@code ResponseEntity<Map<String, Object>>}
	         */
	        @PostMapping("/hotdeal/like")
	        public ResponseEntity<Map<String, Object>> toggleLike(
	                @RequestBody Map<String, String> request, 
	                Authentication auth) {	        
	        Map<String, Object> response = new HashMap<>();
	        
	        if (auth == null || !auth.isAuthenticated()) {
	            response.put("result", "login_required");
	            return ResponseEntity.ok(response);
	        }
	        
	        String userId = auth.getName();
	        String bseq = request.get("bseq");
	        
	        UserDTO userDto = membermapper.get(userId);
	        String userSeq = userDto.getSeq();
	        
	        // 좋아요 체크
	        int isLiked = likemapper.likeCheck(userSeq, bseq);
	        
	        if (isLiked == 1) {
	            // 좋아요 취소
	        	likemapper.likeDel(userSeq, bseq);
	            response.put("action", "unliked");
	        } else {
	            // 좋아요 추가
	        	likemapper.likeAdd(userSeq, bseq);
	            response.put("action", "liked");
	        }
	        
	        // 좋아요 개수 조회
	        int likeCount = likemapper.getLikeCount(bseq);
	        
	        response.put("result", "success");
	        response.put("likeCount", likeCount);
	        
	        return ResponseEntity.ok(response);
	    }

	        /**
	         * 핫딜 게시글에 대한 스크랩 상태를 토글하는 REST API입니다.
	         * 로그인한 사용자만 스크랩할 수 있으며, 처리 후 스크랩 상태를 반환합니다.
	         *
	         * @param request 게시글 ID("bseq")를 포함하는 {@code Map<String, String>}
	         * @param auth    Spring Security의 Authentication 객체
	         * @return 처리 결과(result), 스크랩 상태(action)를 담은 {@code ResponseEntity<Map<String, Object>>}
	         */
	        @PostMapping("/hotdeal/scrap")
	        public ResponseEntity<Map<String, Object>> toggleScrap(
	                @RequestBody Map<String, String> request, 
	                Authentication auth) {	        
	        Map<String, Object> response = new HashMap<>();
	        
	        if (auth == null || !auth.isAuthenticated()) {
	            response.put("result", "login_required");
	            return ResponseEntity.ok(response);
	        }
	        
	        String userId = auth.getName();
	        String bseq = request.get("bseq");
	        
	        UserDTO userDto = membermapper.get(userId);
	        String userSeq = userDto.getSeq();
	        
	        // 스크랩 체크
	        int isScrapped = likemapper.scrapCheck(userSeq, bseq);
	        
	        if (isScrapped == 1) {
	            // 스크랩 취소
	        	likemapper.scrapDel(userSeq, bseq);
	            response.put("action", "unscrapped");
	        } else {
	            // 스크랩 추가
	        	likemapper.scrapAdd(userSeq, bseq);
	            response.put("action", "scrapped");
	        }
	        
	        response.put("result", "success");
	        
	        return ResponseEntity.ok(response);
	    }
	}
	 
	

	


package com.project.trip.board.find.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
// ★★★ 관리자 계정 처리용 Spring Security 기본 User 객체 import ★★★
import org.springframework.security.core.userdetails.User; 

import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;
import com.project.trip.board.find.service.FindBoardService;
import com.project.trip.mypage.model.CustomUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; 

@Controller
@RequiredArgsConstructor
@RequestMapping("/findboard")
@Slf4j 
public class FindBoardController {

    private final FindBoardService findBoardService;

    /**
     * Spring Security의 {@link Authentication} 객체로부터 현재 로그인한 사용자의 ID를 추출하는 헬퍼 함수입니다.
     * <p>
     * 일반 사용자({@link CustomUser})와 관리자({@link User})의 경우를 모두 처리하며,
     * 로그인 정보가 없거나 ID를 파싱할 수 없는 경우 null을 반환합니다.
     * </p>
     * @param authentication Spring Security의 Authentication 객체
     * @return 로그인한 사용자의 ID (Integer), 로그인 정보가 없거나 파싱 실패 시 null
     */
    private Integer getLoggedInUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null; // 비로그인 시
        }
        
        Object principal = authentication.getPrincipal();
        String seqStr = null; // ★★★ [수정] 변수 한 번만 선언 ★★★
        
        // 1. 일반 사용자 (CustomUser) 타입인 경우
        if (principal instanceof CustomUser) {
            CustomUser customUser = (CustomUser) principal;
            seqStr = customUser.getUdto().getSeq(); // ★★★ [수정] 재할당 (String 타입 제거) ★★★
            
        // 2. 관리자 (Spring Security 기본 User) 타입인 경우
        } else if (principal instanceof User) {
            User user = (User) principal;
            // 관리자 ID(seq)가 user.getUsername()에 저장되어 있다고 가정
            seqStr = user.getUsername(); 
        } else {
            // 기타 타입 (익명 사용자 등)
            return null;
        }
        
        if (seqStr == null || seqStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            // 추출된 String ID를 Integer로 변환
            return Integer.parseInt(seqStr.trim());
        } catch (NumberFormatException e) {
            log.error("Failed to parse user ID (seq) '{}' to Integer. Check Admin UserDetails Service.", seqStr, e);
            return null; 
        }
    }
    
    /**
     * 게시글 목록을 조회하여 뷰에 전달합니다.
     * 페이지네이션, 검색 타입 및 키워드를 지원합니다.
     *
     * @param model         뷰에 데이터를 전달하기 위한 Model 객체
     * @param currentPage   현재 페이지 번호 (기본값: 1)
     * @param searchType    검색 타입 (예: "title", "content", "writer")
     * @param searchKeyword 검색 키워드
     * @return "find.list" 게시글 목록 뷰 이름
     */
    @GetMapping("/list")
    public String getFindBoardList(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword
    ) {
        Map<String, Object> resultMap = findBoardService.getPostList(currentPage, searchType, searchKeyword);
        
        model.addAttribute("list", resultMap.get("list"));
        model.addAttribute("totalCount", resultMap.get("totalCount"));
        model.addAttribute("paging", resultMap.get("paging")); 
        model.addAttribute("searchMap", resultMap.get("map"));
        
        return "find.list"; 
    }
    
    /**
     * 게시글 등록 폼 페이지를 표시합니다.
     *
     * @return "find.add" 게시글 등록 폼 뷰 이름
     */
    @GetMapping("/add")
    public String addFindBoardForm() {
        return "find.add";
    }

    /**
     * 게시글 등록 요청을 처리합니다.
     * 로그인한 사용자만 게시글을 등록할 수 있으며, 등록 후 목록 페이지로 리다이렉트합니다.
     *
     * @param dto            등록할 게시글 정보를 담은 {@link findboardDTO}
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr           리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/findboard/list" 게시글 목록 페이지로 리다이렉트, 로그인 정보가 유효하지 않으면 "/login"으로 리다이렉트
     */
    @PostMapping("/add")
    public String addFindBoardProcess(
            findboardDTO dto, 
            Authentication authentication, 
            RedirectAttributes rttr) {
        
        Integer userId = getLoggedInUserId(authentication);
        
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인 정보가 유효하지 않아 게시글을 등록할 수 없습니다.");
            return "redirect:/login"; 
        }
        
        dto.setUser_id(String.valueOf(userId)); 
        
        findBoardService.addPost(dto); 
        rttr.addFlashAttribute("msg", "게시글이 등록되었습니다.");
        return "redirect:/findboard/list";
    }

    /**
     * 특정 게시글의 상세 내용을 조회하고, 해당 게시글의 댓글 목록과 함께 뷰에 전달합니다.
     *
     * @param boardSeq       조회할 게시글의 고유 번호
     * @param model          뷰에 데이터를 전달하기 위한 Model 객체
     * @param authentication Spring Security의 Authentication 객체
     * @return "find.view" 게시글 상세 뷰 이름
     */
    @GetMapping("/view")
    public String viewFindBoard(@RequestParam("seq") int boardSeq, Model model, Authentication authentication) {
        
        Integer userId = getLoggedInUserId(authentication); 
        
        findboardDTO dto = findBoardService.getPostDetail(boardSeq, userId);
        List<findcommentDTO> commentList = findBoardService.getCommentList(boardSeq);
        
        model.addAttribute("dto", dto);
        model.addAttribute("commentList", commentList);
        
        return "find.view";
    }

    /**
     * 게시글 수정 폼 페이지를 표시합니다.
     * 로그인 여부 및 수정 권한을 확인하여, 권한이 없는 경우 상세 페이지로 리다이렉트합니다.
     *
     * @param boardSeq       수정할 게시글의 고유 번호
     * @param model          뷰에 데이터를 전달하기 위한 Model 객체
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr           리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "find.edit" 게시글 수정 폼 뷰 이름, 또는 권한이 없는 경우 상세 페이지로 리다이렉트
     */
    @GetMapping("/edit")
    public String editFindBoardForm(@RequestParam("seq") int boardSeq, 
                                    Model model, 
                                    Authentication authentication, 
                                    RedirectAttributes rttr) {
        
        Integer userId = getLoggedInUserId(authentication);

        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }

        findboardDTO dto = findBoardService.getPostById(boardSeq);

        if (dto == null) {
            rttr.addFlashAttribute("msg", "존재하지 않는 게시글입니다.");
            return "redirect:/findboard/list";
        }

        if (dto.getUser_id() == null || !dto.getUser_id().equals(String.valueOf(userId))) { 
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }
        
        model.addAttribute("dto", dto);
        return "find.edit";
    }
    
    /**
     * 게시글 수정 요청을 처리합니다.
     * 로그인 여부 및 수정 권한을 확인하여, 권한이 없는 경우 상세 페이지로 리다이렉트합니다.
     *
     * @param dto            수정할 게시글 정보를 담은 {@link findboardDTO}
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr           리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/findboard/view?seq={boardSeq}" 게시글 상세 페이지로 리다이렉트
     */
    @PostMapping("/edit")
    public String editFindBoardProcess(
            findboardDTO dto, 
            Authentication authentication,
            RedirectAttributes rttr) {
        
        if (dto == null || dto.getUser_id() == null || dto.getFind_board_id() == 0) {
            rttr.addFlashAttribute("msg", "수정 정보가 누락되었습니다.");
            return "redirect:/findboard/list"; 
        }
        
        Integer sessionUserId = getLoggedInUserId(authentication);
        
        if (sessionUserId == null || !dto.getUser_id().equals(String.valueOf(sessionUserId))) {
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
            return "redirect:/findboard/view?seq=" + dto.getFind_board_id();
        }
        
        findBoardService.updatePost(dto); 
        rttr.addFlashAttribute("msg", "게시글이 수정되었습니다.");
        return "redirect:/findboard/view?seq=" + dto.getFind_board_id(); 
    }
    
    /**
     * 게시글 삭제 요청을 처리합니다.
     * 로그인 여부 및 삭제 권한을 확인하여, 권한이 없는 경우 상세 페이지로 리다이렉트합니다.
     *
     * @param boardSeq       삭제할 게시글의 고유 번호
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr           리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/findboard/list" 게시글 목록 페이지로 리다이렉트
     */
    @GetMapping("/delete")
    public String deleteFindBoard(@RequestParam("seq") int boardSeq, Authentication authentication, RedirectAttributes rttr) {

        Integer userId = getLoggedInUserId(authentication);
        
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }

        findboardDTO dto = findBoardService.getPostById(boardSeq);
        
        if (dto.getUser_id() == null || !dto.getUser_id().equals(String.valueOf(userId))) { 
            rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }

        findBoardService.deletePost(boardSeq);
        rttr.addFlashAttribute("msg", "게시글이 삭제되었습니다.");
        return "redirect:/findboard/list";
    }

    /**
     * 게시글에 대한 좋아요 상태를 토글합니다.
     * 로그인한 사용자만 좋아요를 누를 수 있으며, 처리 후 게시글 상세 페이지로 리다이렉트합니다.
     *
     * @param boardSeq       좋아요를 토글할 게시글의 고유 번호
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr           리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/findboard/view?seq={boardSeq}" 게시글 상세 페이지로 리다이렉트
     */
    @GetMapping("/like")
    public String toggleLike(@RequestParam("seq") int boardSeq, Authentication authentication, RedirectAttributes rttr) {

        Integer userId = getLoggedInUserId(authentication);
        
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }
        
        findBoardService.toggleLike(boardSeq, userId);
        return "redirect:/findboard/view?seq=" + boardSeq;
    }

    /**
     * 게시글에 대한 스크랩 상태를 토글합니다.
     * 로그인한 사용자만 스크랩할 수 있으며, 처리 후 게시글 상세 페이지로 리다이렉트합니다.
     *
     * @param boardSeq       스크랩을 토글할 게시글의 고유 번호
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr           리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/findboard/view?seq={boardSeq}" 게시글 상세 페이지로 리다이렉트
     */
    @GetMapping("/scrap")
    public String toggleScrap(@RequestParam("seq") int boardSeq, Authentication authentication, RedirectAttributes rttr) {

        Integer userId = getLoggedInUserId(authentication);
        
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }
        
        findBoardService.toggleScrap(boardSeq, userId);
        return "redirect:/findboard/view?seq=" + boardSeq;
    }
    
    /**
     * 게시글 신고 폼 페이지를 표시합니다.
     * 신고할 게시글 ID와 신고 대상 사용자 ID를 뷰에 전달합니다.
     *
     * @param boardSeq       신고할 게시글의 고유 번호
     * @param reportedUserId 신고 대상 사용자의 ID
     * @param model          뷰에 데이터를 전달하기 위한 Model 객체
     * @return "find.report" 신고 폼 뷰 이름
     */
    @GetMapping("/report") 
    public String reportForm(@RequestParam("boardSeq") int boardSeq, @RequestParam("reportedUserId") int reportedUserId, Model model) {
        model.addAttribute("boardSeq", boardSeq);
        model.addAttribute("reportedUserId", reportedUserId);
        return "find.report"; 
    }

    /**
     * 게시글 신고 요청을 처리합니다.
     * 로그인한 사용자만 신고할 수 있으며, 신고 처리 후 성공 또는 실패 알림 페이지로 포워드합니다.
     *
     * @param boardSeq       신고할 게시글의 고유 번호
     * @param reportedUserId 신고 대상 사용자의 ID
     * @param reason         신고 사유
     * @param authentication Spring Security의 Authentication 객체
     * @return "forward:/WEB-INF/views/inc/report_success_alert.jsp" 신고 성공 시,
     *         "forward:/WEB-INF/views/inc/report_failure_alert.jsp" 신고 실패 시
     */
    @PostMapping("/report")
    public String reportProcess(@RequestParam int boardSeq, 
                                @RequestParam int reportedUserId, 
                                @RequestParam String reason, 
                                Authentication authentication) { 
        
        Integer reporterId = getLoggedInUserId(authentication);

        if (reporterId == null) {
            return "forward:/WEB-INF/views/inc/report_failure_alert.jsp"; 
        }

        try {
            findBoardService.addReport(boardSeq, reporterId, reportedUserId, reason);
            return "forward:/WEB-INF/views/inc/report_success_alert.jsp"; 
        } catch (Exception e) {
            log.error("Error during reporting process: {}", e.getMessage(), e);
            return "forward:/WEB-INF/views/inc/report_failure_alert.jsp"; 
        }
    }

    /**
     * 게시글에 댓글을 등록하는 요청을 처리합니다.
     * 로그인한 사용자만 댓글을 등록할 수 있으며, 등록 후 게시글 상세 페이지로 리다이렉트합니다.
     *
     * @param dto            등록할 댓글 정보를 담은 {@link findcommentDTO}
     * @param authentication Spring Security의 Authentication 객체
     * @return "redirect:/findboard/view?seq={boardSeq}" 게시글 상세 페이지로 리다이렉트
     */
    @PostMapping("/addcomment")
    public String addCommentProcess(findcommentDTO dto, Authentication authentication) {

        Integer userId = getLoggedInUserId(authentication);
        
        if (userId == null) {
            return "redirect:/findboard/view?seq=" + dto.getFind_board_id();
        }
        
        dto.setUser_id(userId); 
        findBoardService.addComment(dto);
        
        return "redirect:/findboard/view?seq=" + dto.getFind_board_id();
    }
    
    /**
     * 댓글 수정 요청을 처리합니다.
     * 로그인 여부 및 수정 권한을 확인하여, 권한이 없는 경우 메시지와 함께 게시글 상세 페이지로 리다이렉트합니다.
     *
     * @param dto            수정할 댓글 정보를 담은 {@link findcommentDTO}
     * @param boardSeq       댓글이 속한 게시글의 고유 번호
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr           리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/findboard/view?seq={boardSeq}" 게시글 상세 페이지로 리다이렉트
     */
    @PostMapping("/editcomment")
    public String editCommentProcess(findcommentDTO dto, @RequestParam("boardSeq") int boardSeq, Authentication authentication, RedirectAttributes rttr) {

        Integer sessionUserId = getLoggedInUserId(authentication);

        if (sessionUserId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }

        int commentAuthorId = findBoardService.getCommentAuthor(dto.getFind_comment_id());
        
        if (sessionUserId != commentAuthorId) {
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
        } else {
            findBoardService.updateComment(dto);
        }
        
        return "redirect:/findboard/view?seq=" + boardSeq;
    }

    /**
     * 댓글 삭제 요청을 처리합니다.
     * 로그인 여부 및 삭제 권한을 확인하여, 권한이 없는 경우 메시지와 함께 게시글 상세 페이지로 리다이렉트합니다.
     *
     * @param commentId      삭제할 댓글의 고유 번호
     * @param boardSeq       댓글이 속한 게시글의 고유 번호
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr           리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/findboard/view?seq={boardSeq}" 게시글 상세 페이지로 리다이렉트
     */
    @GetMapping("/deletecomment")
    public String deleteCommentProcess(@RequestParam int commentId, @RequestParam int boardSeq, Authentication authentication, RedirectAttributes rttr) {
        
        Integer sessionUserId = getLoggedInUserId(authentication);

        if (sessionUserId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }

        int commentAuthorId = findBoardService.getCommentAuthor(commentId);
        
        if (sessionUserId != commentAuthorId) {
            rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
        } else {
            findBoardService.deleteComment(commentId);
        }
        
        return "redirect:/findboard/view?seq=" + boardSeq;
    }
}
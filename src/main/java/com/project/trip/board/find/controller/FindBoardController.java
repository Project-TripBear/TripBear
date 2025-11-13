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

    // ★★★ [ClassCastException 최종 수정] 관리자/일반/비로그인 모두 안전하게 처리 ★★★
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
    
    // --- 1. 목록 조회 ---
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
    
    // --- 2-1. 게시글 등록 GET ---
    @GetMapping("/add")
    public String addFindBoardForm() {
        return "find.add";
    }

    // --- 2-2. 게시글 등록 POST ---
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

    // --- 3. 상세 조회 ---
    @GetMapping("/view")
    public String viewFindBoard(@RequestParam("seq") int boardSeq, Model model, Authentication authentication) {
        
        Integer userId = getLoggedInUserId(authentication); 
        
        findboardDTO dto = findBoardService.getPostDetail(boardSeq, userId);
        List<findcommentDTO> commentList = findBoardService.getCommentList(boardSeq);
        
        model.addAttribute("dto", dto);
        model.addAttribute("commentList", commentList);
        
        return "find.view";
    }

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
    
    // --- 4-2. 게시글 수정 POST ---
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
    
    // --- 5. 게시글 삭제 ---
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

    // --- 6. 좋아요 토글 ---
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

    // --- 7. 스크랩 토글 ---
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
    
    // --- 8. 신고 폼 GET ---
    @GetMapping("/report") 
    public String reportForm(@RequestParam("boardSeq") int boardSeq, @RequestParam("reportedUserId") int reportedUserId, Model model) {
        model.addAttribute("boardSeq", boardSeq);
        model.addAttribute("reportedUserId", reportedUserId);
        return "find.report"; 
    }

    // --- 8-2. 신고 POST ---
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

    // --- 9. 댓글 등록 ---
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
    
    // --- 10. 댓글 수정 POST ---
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

    // --- 11. 댓글 삭제 GET ---
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
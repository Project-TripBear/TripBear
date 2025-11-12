// 파일 경로: com.project.trip.board.find.controller.FindBoardController.java (수정본)

package com.project.trip.board.find.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// (imports ...)
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Value;
// ★★★ Spring Security의 인증 객체 import ★★★
import org.springframework.security.core.Authentication;

import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;
import com.project.trip.board.find.service.FindBoardService;

// ★★★ 형님의 CustomUser 클래스 import ★★★
import com.project.trip.mypage.model.CustomUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/findboard")
public class FindBoardController {

    private final FindBoardService findBoardService;

    @Value("${app.uploadPath}")
    private String uploadPath;

    // --- 1. 목록 조회 ---
    @GetMapping("/list")
    public String getFindBoardList(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword
    ) {
        // ... (서비스 로직)
        return "find.list"; 
    }
    
    // --- 2. 게시글 등록 GET ---
    @GetMapping("/add")
    public String addFindBoardForm() {
        // (Spring Security가 이미 보호하므로 세션 체크 삭제됨)
        return "find.add";
    }

    // --- 2-2. 게시글 등록 POST ---
    @PostMapping("/add")
    public String addFindBoardProcess(
            findboardDTO dto, 
            @RequestParam("attachFile") MultipartFile file, 
            Authentication authentication, // ★★★ (수정) Authentication 사용
            RedirectAttributes rttr) {
        
        // ★★★ (수정) 500 오류 해결: principal에서 UserDTO의 'seq' 가져오기 ★★★
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Integer userId = Integer.parseInt(customUser.getUdto().getSeq());
        
        dto.setUser_id(userId);
        
        // ... (파일 업로드 로직) ...
        
        findBoardService.addPost(dto);
        rttr.addFlashAttribute("msg", "게시글이 등록되었습니다.");
        return "redirect:/findboard/list";
    }

    // --- 3. 상세 조회 ---
    @GetMapping("/view")
    public String viewFindBoard(@RequestParam("seq") int boardSeq, Model model, Authentication authentication) {
        
        Integer userId = null;
        if (authentication != null) {
            // ★★★ (수정) 로그인한 사용자의 seq 가져오기 ★★★
            CustomUser customUser = (CustomUser) authentication.getPrincipal();
            userId = Integer.parseInt(customUser.getUdto().getSeq());
        }
        
        findboardDTO dto = findBoardService.getPostDetail(boardSeq, userId);
        List<findcommentDTO> commentList = findBoardService.getCommentList(boardSeq);
        
        model.addAttribute("dto", dto);
        model.addAttribute("commentList", commentList);
        
        return "find.view";
    }

    // --- 4. 게시글 수정 GET ---
    @GetMapping("/edit")
    public String editFindBoardForm(@RequestParam("seq") int boardSeq, Model model, Authentication authentication, RedirectAttributes rttr) {
        
        // ★★★ (수정) 로그인한 사용자의 seq 가져오기 ★★★
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Integer userId = Integer.parseInt(customUser.getUdto().getSeq());

        findboardDTO dto = findBoardService.getPostById(boardSeq);
        
        if (dto.getUser_id() != userId) {
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
            @RequestParam("attachFile") MultipartFile newFile,
            RedirectAttributes rttr) {
        
        // ... (파일 수정 로직) ...
        
        findBoardService.updatePost(dto);
        rttr.addFlashAttribute("msg", "게시글이 수정되었습니다.");
        return "redirect:/findboard/view?seq=" + dto.getFind_board_id();
    }
    
    // --- 5. 게시글 삭제 ---
    @GetMapping("/delete")
    public String deleteFindBoard(@RequestParam("seq") int boardSeq, Authentication authentication, RedirectAttributes rttr) {

        // ★★★ (수정) 로그인한 사용자의 seq 가져오기 ★★★
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Integer userId = Integer.parseInt(customUser.getUdto().getSeq());
        
        findboardDTO dto = findBoardService.getPostById(boardSeq);
        if (dto.getUser_id() != userId) {
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

        // ★★★ (수정) 로그인한 사용자의 seq 가져오기 ★★★
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Integer userId = Integer.parseInt(customUser.getUdto().getSeq());
        
        findBoardService.toggleLike(boardSeq, userId);
        return "redirect:/findboard/view?seq=" + boardSeq;
    }

    // --- 7. 스크랩 토글 ---
    @GetMapping("/scrap")
    public String toggleScrap(@RequestParam("seq") int boardSeq, Authentication authentication, RedirectAttributes rttr) {

        // ★★★ (수정) 로그인한 사용자의 seq 가져오기 ★★★
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Integer userId = Integer.parseInt(customUser.getUdto().getSeq());
        
        findBoardService.toggleScrap(boardSeq, userId);
        return "redirect:/findboard/view?seq=" + boardSeq;
    }

    // --- 8. 신고 폼 GET ---
    @GetMapping("/report")
    public String reportForm(@RequestParam("boardSeq") int boardSeq, @RequestParam("reportedUserId") int reportedUserId, Model model) {
        // ... (모델 추가)
        return "find.report"; 
    }

    // --- 8-2. 신고 POST ---
    @PostMapping("/report")
    public String reportProcess(@RequestParam int boardSeq, 
                                @RequestParam int reportedUserId, 
                                @RequestParam String reason, 
                                Authentication authentication, 
                                RedirectAttributes rttr) {
        
        // ★★★ (수정) 로그인한 사용자의 seq 가져오기 (reporterId) ★★★
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Integer reporterId = Integer.parseInt(customUser.getUdto().getSeq());

        try {
            findBoardService.addReport(boardSeq, reporterId, reportedUserId, reason);
            return "forward:/WEB-INF/views/inc/report_success_alert.jsp"; 
        } catch (Exception e) {
            return "forward:/WEB-INF/views/inc/report_failure_alert.jsp"; 
        }
    }

    // --- 9. 댓글 등록 ---
    @PostMapping("/addcomment")
    public String addCommentProcess(findcommentDTO dto, Authentication authentication) {

        // ★★★ (수정) 로그인한 사용자의 seq 가져오기 ★★★
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Integer userId = Integer.parseInt(customUser.getUdto().getSeq());
        
        dto.setUser_id(userId);
        findBoardService.addComment(dto);
        
        return "redirect:/findboard/view?seq=" + dto.getFind_board_id();
    }
    
    // --- 10. 댓글 수정 POST ---
    @PostMapping("/editcomment")
    public String editCommentProcess(findcommentDTO dto, @RequestParam("boardSeq") int boardSeq, Authentication authentication, RedirectAttributes rttr) {

        // ★★★ (수정) 로그인한 사용자의 seq 가져오기 ★★★
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Integer sessionUserId = Integer.parseInt(customUser.getUdto().getSeq());

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
        
        // ★★★ (수정) 로그인한 사용자의 seq 가져오기 ★★★
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Integer sessionUserId = Integer.parseInt(customUser.getUdto().getSeq());

        int commentAuthorId = findBoardService.getCommentAuthor(commentId);
        
        if (sessionUserId != commentAuthorId) {
            rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
        } else {
            findBoardService.deleteComment(commentId);
        }
        
        return "redirect:/findboard/view?seq=" + boardSeq;
    }
    
    // --- 12. 키워드 대시보드 ---
    @GetMapping("/dashboard")
    public String getKeywordDashboard(Model model) {
        // ... (서비스 로직)
        return "find.dashboard";
    }
}
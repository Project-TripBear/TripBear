package com.project.trip.board.find.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// ★★★ [삭제] MultipartFile 관련 Import 제거 ★★★
// ★★★ [삭제] @Value 관련 Import 제거 ★★★
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;
import com.project.trip.board.find.service.FindBoardService;
import com.project.trip.mypage.model.CustomUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 로깅을 위해 추가

@Controller
@RequiredArgsConstructor
@RequestMapping("/findboard")
@Slf4j // 로깅 활성화
public class FindBoardController {

    private final FindBoardService findBoardService;

    // ★★★ [핵심 수정] 로그인 사용자 ID를 Integer 타입으로 안전하게 가져오는 유틸리티 메서드 ★★★
    private Integer getLoggedInUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null; // 비로그인 상태
        }
        
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        // UserDTO.seq는 String 타입이므로 getUdto().getSeq() 사용
        String seqStr = customUser.getUdto().getSeq(); 
        
        if (seqStr == null || seqStr.trim().isEmpty()) {
            // seq 값이 없거나 공백인 경우, 오류 발생 방지를 위해 null 반환
            log.warn("Authentication principal exists, but user ID (seq) is null or empty.");
            return null; 
        }
        
        try {
            // String을 Integer로 변환 시도 (NumberFormatException 방지)
            return Integer.parseInt(seqStr.trim()); 
        } catch (NumberFormatException e) {
            // seq에 숫자가 아닌 값이 들어온 경우
            log.error("Failed to parse user ID (seq) '{}' to Integer.", seqStr, e);
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
        // ★★★ [목록 출력 오류 해결] 서비스 호출 및 결과 Map을 Model에 추가 ★★★
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
            // ★★★ [삭제] @RequestParam("attachFile") MultipartFile file 제거 ★★★
            Authentication authentication, 
            RedirectAttributes rttr) {
        
        Integer userId = getLoggedInUserId(authentication);
        
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인 정보가 유효하지 않아 게시글을 등록할 수 없습니다.");
            return "redirect:/login"; // 비로그인 시 로그인 페이지로 리다이렉트 (필요한 경우)
        }
        
        // DTO의 user_id는 String이므로, Integer를 String으로 변환하여 설정
        dto.setUser_id(String.valueOf(userId)); 
        
        findBoardService.addPost(dto); // ★★★ [삭제] 파일/키워드 처리 로직 제거됨 ★★★
        rttr.addFlashAttribute("msg", "게시글이 등록되었습니다.");
        return "redirect:/findboard/list";
    }

    // --- 3. 상세 조회 ---
    @GetMapping("/view")
    public String viewFindBoard(@RequestParam("seq") int boardSeq, Model model, Authentication authentication) {
        
        // ★★★ [수정] 안전 메서드 사용: Integer 타입으로 좋아요/스크랩 확인용 userId 전달 ★★★
        Integer userId = getLoggedInUserId(authentication); 
        
        findboardDTO dto = findBoardService.getPostDetail(boardSeq, userId);
        List<findcommentDTO> commentList = findBoardService.getCommentList(boardSeq);
        
        model.addAttribute("dto", dto);
        model.addAttribute("commentList", commentList);
        
        return "find.view";
    }

    // --- 4-1. 게시글 수정 GET ---
    @GetMapping("/edit")
    public String editFindBoardForm(@RequestParam("seq") int boardSeq, Model model, Authentication authentication, RedirectAttributes rttr) {
        
        Integer userId = getLoggedInUserId(authentication);

        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }

        findboardDTO dto = findBoardService.getPostById(boardSeq);
        
        // 작성자 ID 비교: DTO의 user_id는 String, userId는 Integer이므로 비교 시 String으로 변환
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
            // ★★★ [삭제] @RequestParam("attachFile") MultipartFile newFile 제거 ★★★
            Authentication authentication,
            RedirectAttributes rttr) {
    	
    	if (dto == null || dto.getUser_id() == null || dto.getFind_board_id() == 0) {
            rttr.addFlashAttribute("msg", "수정 정보가 누락되었습니다.");
            return "redirect:/findboard/list"; 
        }
        // 작성자 검증을 위해 현재 로그인 ID를 다시 가져옴 (선택 사항이나 보안상 권장)
        Integer sessionUserId = getLoggedInUserId(authentication);
        
        if (sessionUserId == null || !dto.getUser_id().equals(String.valueOf(sessionUserId))) {
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
            return "redirect:/findboard/view?seq=" + dto.getFind_board_id();
        }
        
        findBoardService.updatePost(dto); // ★★★ [삭제] 파일/키워드 처리 로직 제거됨 ★★★
        rttr.addFlashAttribute("msg", "게시글이 수정되었습니다.");
        return "redirect:/findboard/view?seq=" + dto.getFind_board_id(); // 👈 이 부분에서 find_board_id가 null이면 NPE 발생    }
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
        // 작성자 ID 비교
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

        // Service/Mapper는 Integer를 요구하므로 Integer 타입으로 사용
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

        // Service/Mapper는 Integer를 요구하므로 Integer 타입으로 사용
        Integer userId = getLoggedInUserId(authentication);
        
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }
        
        findBoardService.toggleScrap(boardSeq, userId);
        return "redirect:/findboard/view?seq=" + boardSeq;
    }
 // --- 8. 신고 폼 GET ---
    @GetMapping("/report") // ★★★ 이 부분이 반드시 @GetMapping 이어야 합니다. ★★★
    public String reportForm(@RequestParam("boardSeq") int boardSeq, @RequestParam("reportedUserId") int reportedUserId, Model model) {
        // ... (필요한 모델 추가 로직)
        model.addAttribute("boardSeq", boardSeq);
        model.addAttribute("reportedUserId", reportedUserId);
        return "find.report"; 
    }
    // --- 8-2. 신고 POST ---
    @PostMapping("/report")
    public String reportProcess(@RequestParam int boardSeq, 
                                @RequestParam int reportedUserId, 
                                @RequestParam String reason, 
                                Authentication authentication) { // RedirectAttributes 제거
        
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
            // 로그인 필요 시, 처리 방식에 따라 리다이렉트 변경 가능
            return "redirect:/findboard/view?seq=" + dto.getFind_board_id();
        }
        
        // findcommentDTO의 user_id는 int 타입이므로, Integer를 바로 설정
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
    
    // ★★★ [삭제] 12. 키워드 대시보드 관련 메서드 제거 ★★★
    // @GetMapping("/dashboard") ...
}
// 파일 경로: com.project.trip.board.find.controller.FindBoardController.java (신규 생성)

package com.project.trip.board.find.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;
import com.project.trip.board.find.service.FindBoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/findboard") // 공통 URL 매핑
public class FindBoardController {

    private final FindBoardService findBoardService;

    @Value("${app.uploadPath}")
    private String uploadPath;

    // --- 1. 목록 조회 (findboardList.java 대체) ---
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
        model.addAttribute("paging", resultMap.get("paging")); // ★★★ PagingDTO 객체를 JSP로 전달 ★★★
        model.addAttribute("searchMap", resultMap.get("searchMap")); // 검색 조건을 JSP로 전달
        
        return "find.list"; 
    }
    // --- 2. 게시글 등록 GET (addFindboard.java 대체) ---
    @GetMapping("/add")
    public String addFindBoardForm(HttpSession session) {
        // 세션 권한 확인 (서블릿 로직)
        if (session.getAttribute("userId") == null) {
            return "redirect:/member/login"; 
        }
        return "find.add"; // /WEB-INF/views/findboard/add.jsp
    }

    // --- 2-2. 게시글 등록 POST (addFindboard.java 대체) ---
    @PostMapping("/add")
    public String addFindBoardProcess(
            findboardDTO dto, 
            @RequestParam("attachFile") MultipartFile file, // ★★★ 파일 수신 ★★★
            HttpSession session, 
            RedirectAttributes rttr) {
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/member/login";
        }
        
        dto.setUser_id(userId);
        
        // ★★★ 파일 업로드 로직 ★★★
        if (!file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                Path filePath = Paths.get(uploadPath + storedFilename);
                
                Files.copy(file.getInputStream(), filePath);
                dto.setFind_board_image(storedFilename); // DB에 저장될 파일명 설정
            } catch (IOException e) {
                rttr.addFlashAttribute("msg", "파일 업로드 중 오류가 발생했습니다.");
                e.printStackTrace();
                return "redirect:/findboard/add";
            }
        }
        
        findBoardService.addPost(dto);
        rttr.addFlashAttribute("msg", "게시글이 등록되었습니다.");
        return "redirect:/findboard/list";
    }

    // --- 3. 상세 조회 (viewfindBoard.java 대체) ---
    @GetMapping("/view")
    public String viewFindBoard(@RequestParam("seq") int boardSeq, Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        
        findboardDTO dto = findBoardService.getPostDetail(boardSeq, userId);
        List<findcommentDTO> commentList = findBoardService.getCommentList(boardSeq);
        
        model.addAttribute("dto", dto);
        model.addAttribute("commentList", commentList);
        
        return "find.view";
    }

    // --- 4. 게시글 수정 GET (editfindBoard.java 대체) ---
    @GetMapping("/edit")
    public String editFindBoardForm(@RequestParam("seq") int boardSeq, Model model, HttpSession session, RedirectAttributes rttr) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/member/login";
        }

        findboardDTO dto = findBoardService.getPostById(boardSeq);
        
        if (dto.getUser_id() != userId) {
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }
        
        model.addAttribute("dto", dto);
        return "find.edit";
    }

    // --- 4-2. 게시글 수정 POST (editfindBoard.java 대체) ---
    @PostMapping("/edit")
    public String editFindBoardProcess(
            findboardDTO dto, 
            @RequestParam("attachFile") MultipartFile newFile,
            RedirectAttributes rttr) {
        
        // ★★★ 파일 수정 로직 (기존 파일 삭제 및 새 파일 업로드) ★★★
        if (!newFile.isEmpty()) {
             // 1. 기존 파일 삭제 로직 (생략)
             // 2. 새 파일 업로드
             try {
                String originalFilename = newFile.getOriginalFilename();
                String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                Path filePath = Paths.get(uploadPath + storedFilename);
                Files.copy(newFile.getInputStream(), filePath);
                dto.setFind_board_image(storedFilename);
             } catch (IOException e) {
                rttr.addFlashAttribute("msg", "파일 수정 중 오류가 발생했습니다.");
                e.printStackTrace();
             }
        } else {
            // 새 파일이 없으면 기존 파일명을 유지 (Hidden 필드에서 find_board_image를 DTO에 받아와야 함)
            // (JSP 수정 필요)
        }
        
        findBoardService.updatePost(dto);
        rttr.addFlashAttribute("msg", "게시글이 수정되었습니다.");
        return "redirect:/findboard/view?seq=" + dto.getFind_board_id();
    }
    
    // --- 5. 게시글 삭제 (deletefindBoard.java 대체) ---
    // 서블릿은 GET이었으나, 스프링에서는 POST로 권장
    @GetMapping("/delete") // 임시로 GET 유지 (JSP와 맞추기 위해)
    public String deleteFindBoard(@RequestParam("seq") int boardSeq, HttpSession session, RedirectAttributes rttr) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/member/login";
        }
        
        findboardDTO dto = findBoardService.getPostById(boardSeq);
        if (dto.getUser_id() != userId) {
            rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }

        findBoardService.deletePost(boardSeq);
        rttr.addFlashAttribute("msg", "게시글이 삭제되었습니다.");
        return "redirect:/findboard/list";
    }
    
    // --- 6. 좋아요 토글 (likefindBoard.java 대체) ---
    @GetMapping("/like")
    public String toggleLike(@RequestParam("seq") int boardSeq, HttpSession session, RedirectAttributes rttr) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/member/login";
        }
        
        findBoardService.toggleLike(boardSeq, userId);
        return "redirect:/findboard/view?seq=" + boardSeq;
    }

    // --- 7. 스크랩 토글 (scrapfindBoard.java 대체) ---
    @GetMapping("/scrap")
    public String toggleScrap(@RequestParam("seq") int boardSeq, HttpSession session, RedirectAttributes rttr) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/findboard/view?seq=" + boardSeq;
        }
        
        findBoardService.toggleScrap(boardSeq, userId);
        return "redirect:/findboard/view?seq=" + boardSeq;
    }

    // --- 8. 신고 폼 GET (reportfindBoard.java 대체) ---
    @GetMapping("/report")
    public String reportForm(@RequestParam("boardSeq") int boardSeq, @RequestParam("reportedUserId") int reportedUserId, Model model) {
        model.addAttribute("boardSeq", boardSeq);
        model.addAttribute("reportedUserId", reportedUserId);
        return "find.report"; // /WEB-INF/views/findboard/report.jsp
    }

    // --- 8-2. 신고 POST (reportfindBoard.java 대체) ---
    @PostMapping("/report")
    public String reportProcess(@RequestParam int boardSeq, 
                                @RequestParam int reportedUserId, 
                                @RequestParam String reason, 
                                HttpSession session, 
                                RedirectAttributes rttr) {
        
        Integer reporterId = (Integer) session.getAttribute("userId");
        if (reporterId == null) {
            return "redirect:/member/login";
        }

        try {
            findBoardService.addReport(boardSeq, reporterId, reportedUserId, reason);
            // 자바스크립트 alert 대신 HTML 응답 (팝업창에서 처리해야 하므로 약간 수정 필요)
            // 성공 시 팝업 닫기
            return "forward:/WEB-INF/views/inc/report_success_alert.jsp"; 
        } catch (Exception e) {
            // 실패 시 이전 페이지로 돌아가기
            return "forward:/WEB-INF/views/inc/report_failure_alert.jsp"; 
        }
    }

    // --- 9. 댓글 등록 (addComment.java 대체) ---
    @PostMapping("/addcomment")
    public String addCommentProcess(findcommentDTO dto, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/member/login";
        }
        
        dto.setUser_id(userId);
        findBoardService.addComment(dto);
        
        return "redirect:/findboard/view?seq=" + dto.getFind_board_id();
    }
    
    // --- 10. 댓글 수정 POST (editComment.java 대체) ---
    @PostMapping("/editcomment")
    public String editCommentProcess(findcommentDTO dto, @RequestParam("boardSeq") int boardSeq, HttpSession session, RedirectAttributes rttr) {
        Integer sessionUserId = (Integer) session.getAttribute("userId");
        int commentAuthorId = findBoardService.getCommentAuthor(dto.getFind_comment_id());
        
        if (sessionUserId == null || sessionUserId != commentAuthorId) {
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
        } else {
            findBoardService.updateComment(dto);
        }
        
        return "redirect:/findboard/view?seq=" + boardSeq;
    }

    // --- 11. 댓글 삭제 GET (deleteComment.java 대체) ---
    @GetMapping("/deletecomment")
    public String deleteCommentProcess(@RequestParam int commentId, @RequestParam int boardSeq, HttpSession session, RedirectAttributes rttr) {
        Integer sessionUserId = (Integer) session.getAttribute("userId");
        int commentAuthorId = findBoardService.getCommentAuthor(commentId);
        
        if (sessionUserId == null || sessionUserId != commentAuthorId) {
            rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
        } else {
            findBoardService.deleteComment(commentId);
        }
        
        return "redirect:/findboard/view?seq=" + boardSeq;
    }
    
    // --- 12. 키워드 대시보드 (신규 기능) ---
    @GetMapping("/dashboard")
    public String getKeywordDashboard(Model model) {
        List<Map<String, Object>> keywords = findBoardService.getPopularKeywords();
        model.addAttribute("keywords", keywords);
        return "find.dashboard"; // 키워드 시각화용 JSP
    }
}
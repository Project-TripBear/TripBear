package com.project.trip.board.qna.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.trip.board.qna.model.QnABoardDTO;
import com.project.trip.board.qna.model.QnACommentDTO;
import com.project.trip.board.qna.service.QnABoardService;
import com.project.trip.mypage.model.CustomUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qnaboard")
@Slf4j
public class QnABoardController {

    private final QnABoardService qnaBoardService;

    /* -------------------------------
       🔹 로그인 유저 ID 가져오는 메서드
    -------------------------------- */
    private Integer getLoggedInUserId(Authentication authentication) {

        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        String seqStr = customUser.getUdto().getSeq();

        try {
            return Integer.parseInt(seqStr);
        } catch (Exception e) {
            return null;
        }
    }


    /* -------------------------------
       🔹 1. 목록 조회
    -------------------------------- */
    @GetMapping("/list")
    public String getQnaBoardList(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "category", required = false) String category
    ) {

        // 🔥 Service 호출 (카테고리 포함)
        Map<String, Object> resultMap =
                qnaBoardService.getPostList(currentPage, searchType, searchKeyword, category);

        // 🔥 화면에 값 전달
        model.addAttribute("list", resultMap.get("list"));
        model.addAttribute("totalCount", resultMap.get("totalCount"));
        model.addAttribute("paging", resultMap.get("paging"));
        model.addAttribute("searchMap", resultMap.get("map"));

        // 카테고리 목록
        model.addAttribute("categoryList", qnaBoardService.getCategoryList());

        return "qna.list";
    }



    /* -------------------------------
       🔹 2. 게시글 등록 GET
    -------------------------------- */
    @GetMapping("/add")
    public String addQnaBoardForm(Model model) {
    	model.addAttribute("categoryList", qnaBoardService.getCategoryList());
        return "qna.add";
    }


    /* -------------------------------
       🔹 2-2. 게시글 등록 POST
    -------------------------------- */
    @PostMapping("/add")
    public String addQnaBoardProcess(
            QnABoardDTO dto,
            Authentication authentication,
            RedirectAttributes rttr) {

        Integer userId = getLoggedInUserId(authentication);

        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        dto.setUser_id(String.valueOf(userId));
        qnaBoardService.addPost(dto);

        rttr.addFlashAttribute("msg", "게시글이 등록되었습니다.");
        return "redirect:/qnaboard/list";
    }




    /* -------------------------------
       🔹 3. 상세 조회
    -------------------------------- */
    @GetMapping("/view")
    public String viewQnaBoard(
            @RequestParam("seq") int boardSeq,
            Model model,
            Authentication authentication) {

        Integer userId = getLoggedInUserId(authentication);

        QnABoardDTO dto = qnaBoardService.getPostDetail(boardSeq, userId);
        List<QnACommentDTO> commentList = qnaBoardService.getCommentList(boardSeq);

        model.addAttribute("dto", dto);
        model.addAttribute("commentList", commentList);

        return "qna.view";
    }




    /* -------------------------------
       🔹 4. 게시글 수정 GET
    -------------------------------- */
    @GetMapping("/edit")
    public String editQnaBoardForm(
            @RequestParam("seq") int boardSeq,
            Model model,
            Authentication authentication,
            RedirectAttributes rttr) {

        Integer userId = getLoggedInUserId(authentication);
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/qnaboard/view?seq=" + boardSeq;
        }

        QnABoardDTO dto = qnaBoardService.getPostById(boardSeq);

        if (!dto.getUser_id().equals(String.valueOf(userId))) {
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
            return "redirect:/qnaboard/view?seq=" + boardSeq;
        }

        model.addAttribute("dto", dto);
        model.addAttribute("categoryList", qnaBoardService.getCategoryList());
        
        return "qna.edit";
    }



    /* -------------------------------
       🔹 4-2. 게시글 수정 POST
    -------------------------------- */
    @PostMapping("/edit")
    public String editQnaBoardProcess(
            QnABoardDTO dto,
            Authentication authentication,
            RedirectAttributes rttr) {
    	
    	System.out.println("🔥 POST EDIT 호출됨");


        Integer userId = getLoggedInUserId(authentication);

        if (userId == null || !dto.getUser_id().equals(String.valueOf(userId))) {
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다.");
            return "redirect:/qnaboard/view?seq=" + dto.getQuestion_board_id();
        }

        System.out.println("카테고리 들어온 값: " + dto.getQuestion_category_id());
        
        qnaBoardService.updatePost(dto);
        rttr.addFlashAttribute("msg", "게시글이 수정되었습니다.");
        
        return "redirect:/qnaboard/view?seq=" + dto.getQuestion_board_id();
    }



    /* -------------------------------
       🔹 5. 게시글 삭제
    -------------------------------- */
    @GetMapping("/delete")
    public String deleteQnaBoard(
            @RequestParam("seq") int boardSeq,
            Authentication authentication,
            RedirectAttributes rttr) {

        Integer userId = getLoggedInUserId(authentication);
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/qnaboard/view?seq=" + boardSeq;
        }

        QnABoardDTO dto = qnaBoardService.getPostById(boardSeq);

        if (!dto.getUser_id().equals(String.valueOf(userId))) {
            rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
            return "redirect:/qnaboard/view?seq=" + boardSeq;
        }

        qnaBoardService.deletePost(boardSeq);
        rttr.addFlashAttribute("msg", "게시글이 삭제되었습니다.");

        return "redirect:/qnaboard/list";
    }




    /* -------------------------------
       🔹 6. 좋아요 토글
    -------------------------------- */
    @GetMapping("/like")
    public String toggleLike(
            @RequestParam("seq") int boardSeq,
            Authentication authentication,
            RedirectAttributes rttr) {

        Integer userId = getLoggedInUserId(authentication);
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/qnaboard/view?seq=" + boardSeq;
        }

        qnaBoardService.toggleLike(boardSeq, userId);
        return "redirect:/qnaboard/view?seq=" + boardSeq;
    }



    /* -------------------------------
       🔹 7. 스크랩 토글
    -------------------------------- */
    @GetMapping("/scrap")
    public String toggleScrap(
            @RequestParam("seq") int boardSeq,
            Authentication authentication,
            RedirectAttributes rttr) {

        Integer userId = getLoggedInUserId(authentication);
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/qnaboard/view?seq=" + boardSeq;
        }

        qnaBoardService.toggleScrap(boardSeq, userId);

        return "redirect:/qnaboard/view?seq=" + boardSeq;
    }



    /* -------------------------------
       🔹 8. 신고 폼 GET
    -------------------------------- */
    @GetMapping("/report")
    public String reportForm(
            @RequestParam("boardSeq") int boardSeq,
            @RequestParam("reportedUserId") int reportedUserId,
            Model model) {

        model.addAttribute("boardSeq", boardSeq);
        model.addAttribute("reportedUserId", reportedUserId);

        return "qna.report";
    }




    /* -------------------------------
       🔹 8-2. 신고 POST
    -------------------------------- */
    @PostMapping("/report")
    public String reportProcess(
            @RequestParam int boardSeq,
            @RequestParam int reportedUserId,
            @RequestParam String reason,
            Authentication authentication) {

        Integer reporterId = getLoggedInUserId(authentication);

        if (reporterId == null) {
            return "forward:/WEB-INF/views/inc/report_failure_alert.jsp";
        }

        try {
            qnaBoardService.addReport(boardSeq, reporterId, reportedUserId, reason);
            return "forward:/WEB-INF/views/inc/report_success_alert.jsp";
        } catch (Exception e) {
            log.error("Report error:", e);
            return "forward:/WEB-INF/views/inc/report_failure_alert.jsp";
        }
    }




    /* -------------------------------
       🔹 9. 댓글 등록
    -------------------------------- */
    @PostMapping("/addcomment")
    public String addCommentProcess(
            QnACommentDTO dto,
            Authentication authentication) {

        Integer userId = getLoggedInUserId(authentication);
        if (userId == null) {
            return "redirect:/qnaboard/view?seq=" + dto.getQuestion_board_id();
        }

        dto.setUser_id(userId);
        qnaBoardService.addComment(dto);

        return "redirect:/qnaboard/view?seq=" + dto.getQuestion_board_id();
    }



    /* -------------------------------
    🔹 10. 댓글 수정 (AJAX)
 -------------------------------- */
	 @PostMapping("/editcomment")
	 @ResponseBody
	 public String editCommentProcess(
	         QnACommentDTO dto,
	         Authentication authentication) {
	
	     Integer userId = getLoggedInUserId(authentication);
	     if (userId == null) {
	         return "NOT_LOGIN";   // Ajax 응답
	     }
	
	     int commentAuthorId = qnaBoardService.getCommentAuthor(dto.getQuestion_answer_id());
	     if (userId != commentAuthorId) {
	         return "NO_PERMISSION";  // Ajax 응답
	     }
	
	     qnaBoardService.updateComment(dto);
	     return "OK";  // 성공
	 }




    /* -------------------------------
       🔹 11. 댓글 삭제
    -------------------------------- */
	 @GetMapping("/deletecomment")
	 public String deleteCommentProcess(
	         @RequestParam int commentId,
	         @RequestParam int boardSeq,
	         Authentication authentication,
	         RedirectAttributes rttr) {

	     Integer userId = getLoggedInUserId(authentication);

	     if (userId == null) {
	         rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
	         return "redirect:/qnaboard/view?seq=" + boardSeq;
	     }

	     int commentAuthorId = qnaBoardService.getCommentAuthor(commentId);

	     if (userId != commentAuthorId) {
	         rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
	     } else {
	         qnaBoardService.deleteComment(commentId);
	     }

	     return "redirect:/qnaboard/view?seq=" + boardSeq;
	 }
}

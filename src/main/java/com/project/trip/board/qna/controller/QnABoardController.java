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

/**
 * QnA 게시판과 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 게시글 목록 조회, 등록, 수정, 삭제, 상세 보기, 좋아요, 스크랩, 신고 기능 및
 * 댓글 등록, 수정, 삭제 기능을 제공합니다.
 * Spring Security를 활용하여 사용자 인증 및 권한을 확인합니다.
 * </p>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/qnaboard")
@Slf4j
public class QnABoardController {

    private final QnABoardService qnaBoardService;

    /**
     * Spring Security의 {@link Authentication} 객체로부터 현재 로그인한 사용자의 ID를 추출하는 헬퍼 함수입니다.
     * <p>
     * 로그인 정보가 없거나 ID를 파싱할 수 없는 경우 null을 반환합니다.
     * </p>
     * @param authentication Spring Security의 Authentication 객체
     * @return 로그인한 사용자의 ID (Integer), 로그인 정보가 없거나 파싱 실패 시 null
     */
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


    /**
     * QnA 게시글 목록을 조회하고 페이징, 검색, 카테고리 필터링 기능을 제공하여 뷰에 전달합니다.
     *
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @param currentPage 현재 페이지 번호 (기본값: 1)
     * @param searchType 검색 타입 (예: "title", "content", "writer")
     * @param searchKeyword 검색 키워드
     * @param category 조회할 카테고리
     * @return "qna.list" QnA 게시글 목록 뷰 이름
     */
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



    /**
     * QnA 게시글 등록 폼 페이지를 표시합니다.
     * 카테고리 목록을 뷰에 전달합니다.
     *
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return "qna.add" QnA 게시글 등록 폼 뷰 이름
     */
    @GetMapping("/add")
    public String addQnaBoardForm(Model model) {
    	model.addAttribute("categoryList", qnaBoardService.getCategoryList());
        return "qna.add";
    }


    /**
     * QnA 게시글 등록 요청을 처리합니다.
     * 로그인한 사용자만 게시글을 등록할 수 있으며, 등록 후 목록 페이지로 리다이렉트합니다.
     *
     * @param dto 등록할 게시글 정보를 담은 {@link com.project.trip.board.qna.model.QnABoardDTO}
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/qnaboard/list" 게시글 목록 페이지로 리다이렉트, 로그인 정보가 유효하지 않으면 "/login"으로 리다이렉트
     */
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




    /**
     * 특정 QnA 게시글의 상세 내용을 조회하고, 해당 게시글의 댓글 목록과 함께 뷰에 전달합니다.
     *
     * @param boardSeq 조회할 게시글의 고유 번호
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @param authentication Spring Security의 Authentication 객체
     * @return "qna.view" QnA 게시글 상세 뷰 이름
     */
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




    /**
     * QnA 게시글 수정 폼 페이지를 표시합니다.
     * 로그인 여부 및 수정 권한을 확인하여, 권한이 없는 경우 상세 페이지로 리다이렉트합니다.
     *
     * @param boardSeq 수정할 게시글의 고유 번호
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "qna.edit" QnA 게시글 수정 폼 뷰 이름, 또는 권한이 없는 경우 상세 페이지로 리다이렉트
     */
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



    /**
     * QnA 게시글 수정 요청을 처리합니다.
     * 로그인 여부 및 수정 권한을 확인하여, 권한이 없는 경우 상세 페이지로 리다이렉트합니다.
     *
     * @param dto 수정할 게시글 정보를 담은 {@link com.project.trip.board.qna.model.QnABoardDTO}
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/qnaboard/view?seq={boardSeq}" QnA 게시글 상세 페이지로 리다이렉트
     */
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



    /**
     * QnA 게시글 삭제 요청을 처리합니다.
     * 로그인 여부 및 삭제 권한을 확인하여, 권한이 없는 경우 상세 페이지로 리다이렉트합니다.
     *
     * @param boardSeq 삭제할 게시글의 고유 번호
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/qnaboard/list" QnA 게시글 목록 페이지로 리다이렉트
     */
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




    /**
     * QnA 게시글에 대한 좋아요 상태를 토글합니다.
     * 로그인한 사용자만 좋아요를 누를 수 있으며, 처리 후 게시글 상세 페이지로 리다이렉트합니다.
     *
     * @param boardSeq 좋아요를 토글할 게시글의 고유 번호
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/qnaboard/view?seq={boardSeq}" QnA 게시글 상세 페이지로 리다이렉트
     */
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



    /**
     * QnA 게시글에 대한 스크랩 상태를 토글합니다.
     * 로그인한 사용자만 스크랩할 수 있으며, 처리 후 게시글 상세 페이지로 리다이렉트합니다.
     *
     * @param boardSeq 스크랩을 토글할 게시글의 고유 번호
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/qnaboard/view?seq={boardSeq}" QnA 게시글 상세 페이지로 리다이렉트
     */
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



    /**
     * QnA 게시글 신고 폼 페이지를 표시합니다.
     * 신고할 게시글 ID와 신고 대상 사용자 ID를 뷰에 전달합니다.
     *
     * @param boardSeq 신고할 게시글의 고유 번호
     * @param reportedUserId 신고 대상 사용자의 ID
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return "qna.report" 신고 폼 뷰 이름
     */
    @GetMapping("/report")
    public String reportForm(
            @RequestParam("boardSeq") int boardSeq,
            @RequestParam("reportedUserId") int reportedUserId,
            Model model) {

        model.addAttribute("boardSeq", boardSeq);
        model.addAttribute("reportedUserId", reportedUserId);

        return "qna.report";
    }




    /**
     * QnA 게시글 신고 요청을 처리합니다.
     * 로그인한 사용자만 신고할 수 있으며, 신고 처리 후 성공 또는 실패 알림 페이지로 포워드합니다.
     *
     * @param boardSeq 신고할 게시글의 고유 번호
     * @param reportedUserId 신고 대상 사용자의 ID
     * @param reason 신고 사유
     * @param authentication Spring Security의 Authentication 객체
     * @return "forward:/WEB-INF/views/inc/report_success_alert.jsp" 신고 성공 시,
     * "forward:/WEB-INF/views/inc/report_failure_alert.jsp" 신고 실패 시
     */
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




    /**
     * QnA 게시글에 댓글을 등록하는 요청을 처리합니다.
     * 로그인한 사용자만 댓글을 등록할 수 있으며, 등록 후 게시글 상세 페이지로 리다이렉트합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link com.project.trip.board.qna.model.QnACommentDTO}
     * @param authentication Spring Security의 Authentication 객체
     * @return "redirect:/qnaboard/view?seq={boardSeq}" QnA 게시글 상세 페이지로 리다이렉트
     */
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



    /**
     * 댓글 수정 요청을 처리하는 REST API입니다.
     * 로그인 여부 및 수정 권한을 확인하여, 권한이 없는 경우 적절한 응답 메시지를 반환합니다.
     *
     * @param dto 수정할 댓글 정보를 담은 {@link com.project.trip.board.qna.model.QnACommentDTO}
     * @param authentication Spring Security의 Authentication 객체
     * @return "OK" (성공), "NOT_LOGIN" (로그인 필요), "NO_PERMISSION" (권한 없음)
     */
    @PostMapping("/editcomment")
    @ResponseBody
    public String editCommentProcess(
            QnACommentDTO dto,
            Authentication authentication) {
	
	    Integer userId = getLoggedInUserId(authentication);
	    if (userId == null) {
	        return "NOT_LOGIN";    // Ajax 응답
	    }
	
	    int commentAuthorId = qnaBoardService.getCommentAuthor(dto.getQuestion_answer_id());
	    if (userId != commentAuthorId) {
	        return "NO_PERMISSION";  // Ajax 응답
	    }
	
	    qnaBoardService.updateComment(dto);
	    return "OK";  // 성공
	 }




    /**
     * 댓글 삭제 요청을 처리합니다.
     * 로그인 여부 및 삭제 권한을 확인하여, 권한이 없는 경우 메시지와 함께 게시글 상세 페이지로 리다이렉트합니다.
     *
     * @param commentId 삭제할 댓글의 고유 번호
     * @param boardSeq 댓글이 속한 게시글의 고유 번호
     * @param authentication Spring Security의 Authentication 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 {@link RedirectAttributes}
     * @return "redirect:/qnaboard/view?seq={boardSeq}" QnA 게시글 상세 페이지로 리다이렉트
     */
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
package com.project.trip.admin.board.controller; // 👈 형님께서 지정하신 정확한 패키지 경로

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.security.core.Authentication; 
import org.springframework.security.core.userdetails.User; 

import com.project.trip.board.notice.model.NoticeDTO;
import com.project.trip.board.notice.service.NoticeService;
import com.project.trip.admin.auth.mapper.AdminMapper; // 👈 AdminMapper import (경로 확인)

import lombok.RequiredArgsConstructor;

/**
 * 관리자 페이지의 공지사항 관리와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 공지사항 등록, 수정, 삭제 기능을 제공합니다.
 * </p>
 */
@Controller
@RequestMapping("/admin/notice") 
@RequiredArgsConstructor
public class AdminNoticeController {

	private final NoticeService noticeService;
    private final AdminMapper adminMapper; // 👈 AdminMapper 주입 완료!
	
    /**
     * 신규 공지사항 등록 폼 페이지를 반환합니다.
     * @return 신규 공지사항 등록 페이지의 뷰 이름
     */
	@GetMapping("/add")
	public String addForm() {
		return "notice.add"; 
	}

	/**
     * 신규 공지사항을 등록 처리합니다.
     * <p>
     * 현재 로그인된 관리자의 ID를 조회하여 작성자 정보로 설정한 후,
     * 공지사항을 데이터베이스에 저장합니다.
     * </p>
     * @param dto 공지사항 정보를 담은 DTO
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @param authentication 현재 인증 정보를 담고 있는 Authentication 객체
     * @return 성공 시 공지사항 목록 페이지로 리다이렉트
     */
	@PostMapping("/add")
	public String addAction(NoticeDTO dto, RedirectAttributes rttr, Authentication authentication) {
        
        // 🚨 핵심 로직: 로그인된 관리자의 ADMIN_ID(Long)를 DTO에 설정 🚨
        if (authentication != null && authentication.isAuthenticated()) {
            
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                // 1. 로그인된 사용자 이름 (ADMIN_NAME: String)을 가져옵니다.
                String adminUsername = ((User) principal).getUsername();
                
                // 2. Mapper를 이용해 DB에서 ADMIN_ID (Long) 값을 조회합니다.
                Long adminId = adminMapper.getAdminIdByUsername(adminUsername); 
                
                if (adminId != null) {
                    dto.setAdminId(adminId); // 👈 하드코딩 대신 실제 ADMIN_ID(Long)를 DTO에 설정
                } else {
                    // DB에서 ID를 찾지 못한 경우
                    System.err.println("ERROR: Logged-in admin ID not found for username: " + adminUsername);
                    return "redirect:/admin/login?error=auth_data_mismatch";
                }
                
            } else {
                // 인증된 객체가 예상된 User 타입이 아닐 경우
                return "redirect:/admin/login"; 
            }
        } else {
            // 인증되지 않은 사용자
            return "redirect:/admin/login";
        }
        
		// 🚨 누락되었던 서비스 호출 및 리다이렉트 (추가 완료)
		noticeService.createNotice(dto); 
		
		rttr.addFlashAttribute("message", "공지사항이 등록되었습니다.");
		
		return "redirect:/notice/list"; 
	}
	
	/**
     * 공지사항 수정 폼 페이지를 반환합니다.
     * @param noticePostId 수정할 공지사항의 고유 ID
     * @param model 뷰에 공지사항 데이터를 전달하기 위한 Model 객체
     * @return 공지사항 수정 페이지의 뷰 이름
     */
	@GetMapping("/edit")
	public String editForm(@RequestParam("id") Long noticePostId, Model model) {
		
		NoticeDTO notice = noticeService.getNoticeForEdit(noticePostId);
		
		model.addAttribute("notice", notice);
		
		return "notice.edit";
	}

	/**
     * 공지사항 정보를 수정 처리합니다.
     * @param dto 수정된 공지사항 정보를 담은 DTO
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 수정된 공지사항의 상세 보기 페이지로 리다이렉트
     */
	@PostMapping("/edit")
	public String editAction(NoticeDTO dto, RedirectAttributes rttr) {
		
		noticeService.updateNotice(dto);
		
		rttr.addFlashAttribute("message", "공지사항이 수정되었습니다.");
		
		return "redirect:/notice/view?id=" + dto.getNoticePostId();
	}
	
	/**
     * 공지사항을 삭제 처리합니다.
     * @param noticePostId 삭제할 공지사항의 고유 ID
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 공지사항 목록 페이지로 리다이렉트
     */
	@PostMapping("/delete")
	public String delete(@RequestParam("id") Long noticePostId, RedirectAttributes rttr) {
		
		noticeService.deleteNotice(noticePostId);
		
		rttr.addFlashAttribute("message", "공지사항이 삭제되었습니다");
		
		return "redirect:/notice/list";
	}
}
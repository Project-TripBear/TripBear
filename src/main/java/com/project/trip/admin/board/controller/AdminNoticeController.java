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

@Controller
@RequestMapping("/admin/notice") 
@RequiredArgsConstructor
public class AdminNoticeController {

	private final NoticeService noticeService;
    private final AdminMapper adminMapper; // 👈 AdminMapper 주입 완료!
	
    // ------------------------------------------------------------------
    // 관리자 기능 (글쓰기, 수정, 삭제)
    // ------------------------------------------------------------------

	@GetMapping("/add")
	public String addForm() {
		return "notice.add"; 
	}

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
	
	@GetMapping("/edit")
	public String editForm(@RequestParam("id") Long noticePostId, Model model) {
		
		NoticeDTO notice = noticeService.getNoticeForEdit(noticePostId);
		
		model.addAttribute("notice", notice);
		
		return "notice.edit";
	}

	@PostMapping("/edit")
	public String editAction(NoticeDTO dto, RedirectAttributes rttr) {
		
		noticeService.updateNotice(dto);
		
		rttr.addFlashAttribute("message", "공지사항이 수정되었습니다.");
		
		return "redirect:/notice/view?id=" + dto.getNoticePostId();
	}
	
	@PostMapping("/delete")
	public String delete(@RequestParam("id") Long noticePostId, RedirectAttributes rttr) {
		
		noticeService.deleteNotice(noticePostId);
		
		rttr.addFlashAttribute("message", "공지사항이 삭제되었습니다");
		
		return "redirect:/notice/list";
	}
}
package com.project.trip.board.notice.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.trip.board.notice.model.NoticeDTO;
import com.project.trip.board.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeService noticeService;
	
	@GetMapping("/list")
	public String list(Model model) {
		
		List<NoticeDTO> list = noticeService.getNoticeList();
		
		model.addAttribute("list", list);
		
		return "notice/list";
	}
	
	@GetMapping("/view")
	public String view(@RequestParam("id") Long noticePostId, Model model) {
		
		NoticeDTO notice = noticeService.getNoticeDetail(noticePostId);
		
		model.addAttribute("notice", notice);
		
		return "notice/view";
	}

	@GetMapping("/add")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String addForm() {
		
		return "notice/add";
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String addAction(NoticeDTO dto, RedirectAttributes rttr) {
		
		noticeService.createNotice(dto);
		
		rttr.addFlashAttribute("message", "공지사항이 등록되었습니다.");
		
		return "redirect:/notice/list";
	}
	
	@GetMapping("/edit")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String editForm(@RequestParam("id") Long noticePostId, Model model) {
		
		NoticeDTO notice = noticeService.getNoticeForEdit(noticePostId);
		
		model.addAttribute("notice", notice);
		
		return "notice/edit";
	}

	@PostMapping("/edit")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String editAction(NoticeDTO dto, RedirectAttributes rttr) {
		
		noticeService.updateNotice(dto);
		
		rttr.addFlashAttribute("message", "공지사항이 수정되었습니다.");
		
		return "redirect:/notice/view?id=" + dto.getNoticePostId();
	}
	
	@PostMapping("/delete")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String delete(@RequestParam("id") Long noticePostId, RedirectAttributes rttr) {
		
		noticeService.deleteNotice(noticePostId);
		
		rttr.addFlashAttribute("message", "공지사항이 삭제되었습니다");
		
		return "redirect:/notice/list";
				
		
		 
	}
	
	
}

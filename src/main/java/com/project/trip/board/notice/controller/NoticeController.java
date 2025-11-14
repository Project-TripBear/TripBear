package com.project.trip.board.notice.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.trip.board.notice.model.NoticeDTO;
import com.project.trip.board.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;

// NoticeController.java (일반 사용자 접근용)
@Controller
@RequestMapping("/notice") 
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeService noticeService;
	
    /**
     * 공지사항 목록 조회 (일반 회원/비회원 접근 가능)
     * URL: /notice/list
     */
	@GetMapping("/list")
	public String list(Model model) {
		
		// NoticeService를 통해 DB에서 목록 데이터를 가져옵니다.
		List<NoticeDTO> list = noticeService.getNoticeList();
		
		model.addAttribute("list", list);
		
		return "notice.list";
	}
	
    /**
     * 공지사항 상세 보기 (일반 회원/비회원 접근 가능)
     * URL: /notice/view?id=...
     */
	@GetMapping("/view")
	public String view(@RequestParam("id") Long noticePostId, Model model) {
		
		// 상세 데이터를 가져와 모델에 추가
		NoticeDTO notice = noticeService.getNoticeDetail(noticePostId);
		
		model.addAttribute("notice", notice);
		
		return "notice.view";
	}
}
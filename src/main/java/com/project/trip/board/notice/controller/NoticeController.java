package com.project.trip.board.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.trip.board.notice.model.NoticeDTO;
import com.project.trip.board.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeService noticeService;
	
	@GetMapping("/notice/list")
	public String list(Model model) {
		
		List<NoticeDTO> list = noticeService.getNoticeList();
		
		model.addAttribute("list", list);
		
		return "notice.list";
	}
	
	@GetMapping("/notice/view")
	public String view(@RequestParam("id") String notice_id, Model model) {
		
		NoticeDTO notice = noticeService.getNoticeDetail(notice_id);
		
		model.addAttribute("notice", notice);
		
		return "notice.view";
	}

	@GetMapping("/notice/add")
	public String Add() {
		
		
		return "notice.add";
	}

	@PostMapping("/notice/add")
	public String AddAction(NoticeDTO dto) {
		
		int result = noticeService.creatNotice(dto);
		
		return "redirect:/notice/list";
	}
	
}

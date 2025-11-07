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

@Controller
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@GetMapping("/list.do")
	public String list(Model model) {
		
		List<NoticeDTO> list = noticeService.getNoticeList();
		
		model.addAttribute("list", list);
		
		return "notice/list";
	}
	
	@GetMapping("/view.do")
	public String view(@RequestParam("id") String notice_id, Model model) {
		
		NoticeDTO notice = noticeService.getNoticeDetail(notice_id);
		
		model.addAttribute("notice", notice);
		
		return "notice/list";
	}

	@GetMapping("/add.do")
	public String Add() {
		
		
		return "notice/add";
	}

	@PostMapping("/add.do")
	public String AddAction(NoticeDTO dto) {
		
		int result = noticeService.creatNotice(dto);
		
		return "notice/add";
	}
	
}

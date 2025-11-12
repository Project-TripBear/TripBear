	package com.project.trip.admin.board.controller;
	
	import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.trip.admin.board.model.IntegratedBoardDTO;
import com.project.trip.admin.board.model.PagingDTO;
import com.project.trip.admin.board.service.AdminBoardService;
	
//AdminBoardController.java (수정)

//AdminBoardController.java (수정)
//... (import 생략)

@Controller
@RequestMapping("/admin/board")
public class AdminBoardController {

 @Autowired
 private AdminBoardService adminBoardService;
 
 @RequestMapping(value = "/integratedList", method = RequestMethod.GET)
 public String getIntegratedBoardList(@RequestParam(defaultValue = "1") int currentPage, Model model) {
     
     // 1. 전체 게시글 수 조회
     int totalCount = adminBoardService.getTotalBoardCount(); 
     
     // 2. PagingDTO 객체 생성 및 계산
     PagingDTO paging = new PagingDTO(currentPage, totalCount, 10, 10);     
     // 3. Service를 통해 현재 페이지의 DB 데이터 조회 (startRow, endRow 사용)
     List<IntegratedBoardDTO> boardList = adminBoardService.getIntegratedBoardList(
         paging.getStartRow(), 
         paging.getEndRow()
     ); 
     
     // 4. View(JSP)로 전달
     model.addAttribute("boardList", boardList);
     model.addAttribute("paging", paging); // PagingDTO 객체 전달
     
     return "admin/board/integratedList"; // Tiles 정의명으로 반환 (이전에 확인한 이름)
 }
}
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

@Controller
@RequestMapping("/admin/board")
public class AdminBoardController {

    @Autowired
    private AdminBoardService adminBoardService;
    
    @RequestMapping(value = "/integratedList", method = RequestMethod.GET)
    public String getIntegratedBoardList(
        // ★ 1. JSP 페이징과 맞추기 위해 "currentPage" -> "page"로 수정
        @RequestParam(defaultValue = "1") int page, 
        // ★ 2. boardType 파라미터 받기 (필수 아님)
        @RequestParam(value = "boardType", required = false) String boardType, 
        Model model
    ) {
        
        // ★ 3. boardType을 Service로 전달
        int totalCount = adminBoardService.getTotalBoardCount(boardType); 
        
        // ★ 4. "currentPage" -> "page"로 수정
        PagingDTO paging = new PagingDTO(page, totalCount, 10, 10);     
        
        // ★ 5. boardType을 Service로 전달
        List<IntegratedBoardDTO> boardList = adminBoardService.getIntegratedBoardList(
            paging.getStartRow(), 
            paging.getEndRow(),
            boardType // ★ boardType 추가
        ); 
        
        // 6. View(JSP)로 전달
        model.addAttribute("boardList", boardList);
        model.addAttribute("paging", paging);
        // ★ 7. JSP가 탭과 페이징 링크에서 사용할 수 있도록 boardType 다시 전달
        model.addAttribute("boardType", boardType); 
        
        // 형님이 주신 파일의 반환값 "admin/integratedList"를 사용합니다.
        // (참고: 이전에 사용하시던 "admin.board.integratedList"와 다릅니다.)
        return "admin/integratedList"; 
    }
}
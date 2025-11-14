// 파일 경로: com.project.trip.admin.user.controller.AdminUserController.java

package com.project.trip.admin.user.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping; // @RequestMapping 추가
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.trip.admin.board.model.PagingDTO;
import com.project.trip.admin.user.model.suspendedUserDTO;
import com.project.trip.admin.user.model.AdminUserDTO;
import com.project.trip.admin.user.service.AdminUserService;

import lombok.RequiredArgsConstructor;

// ★★★ 수정: 클래스 레벨에 기본 경로 설정 및 .do 제거 ★★★
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user") 
public class AdminUserController {
    
    private final AdminUserService userService;
    
    @GetMapping("/list") 
    public String getUserList(
        Model model,
        
        // ★★★ "value" 속성을 추가하여 파라미터 이름을 명시합니다. ★★★
        @RequestParam(value = "searchType", required = false) String searchType,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "status", required = false) String status,
        
        @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        
        int totalCount = userService.getTotalUserCount(searchType, keyword, status);
        PagingDTO paging = new PagingDTO(page, totalCount, 10, 10); 
        List<AdminUserDTO> userlist = userService.getUserList(searchType, keyword, status, paging);
        
        model.addAttribute("userlist", userlist);
        model.addAttribute("paging", paging); 
        
        return "admin/userlist"; 
    }
    

    
 // 2. ★★★ [수정] 회원 정지 처리 (adminId 세션 처리 추가) ★★★
    @PostMapping("/suspend") 
    public String suspendUser(
        @RequestParam int userId,
        @RequestParam String reason,
        @RequestParam int duration,
        RedirectAttributes rttr,
        HttpSession session // 3. 세션을 받도록 수정
    ) {
        try {
            // 4. 레거시 파일(suspendUser.java)처럼 세션에서 adminId(auth)를 가져옴
        	//!!!!!!!로그인 페이지 구현 전까지 하드코딩!!!!!
			/*
			 * Integer adminId = (Integer) session.getAttribute("auth");
			 * 
			 * if (adminId == null) { rttr.addFlashAttribute("msg", "오류: 관리자 세션이 만료되었습니다.");
			 * return "redirect:/admin/login"; // (관리자 로그인 페이지로) }
			 */
        	// ★★★ 여기에 임시 관리자 ID를 하드코딩합니다. (예: 1) ★★★
        	int adminId = 1;
        	
            userService.processSuspend(userId, adminId, reason, duration); // 5. adminId 전달
            rttr.addFlashAttribute("msg", "회원번호 " + userId + " 정지 처리가 완료되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "회원 정지 처리 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        
        return "redirect:/admin/user/list"; 
    }
    
    @GetMapping("/suspendedlist")
    public String getSuspendedList(Model model) {
        
        // Service를 호출하여 정지된 회원 목록을 가져옵니다.
        List<suspendedUserDTO> suspendedlist = userService.getSuspendedUserList();
        
        // Model에 "suspendedlist"라는 이름으로 데이터를 담습니다.
        model.addAttribute("suspendedlist", suspendedlist);
        
        // Tiles에 정의할 뷰 이름 "content/admin/suspendedlist"를 반환합니다.
        return "admin/suspendedlist"; 
    }
    
    @PostMapping("/restore")
    public String restoreUser(@RequestParam int userId, RedirectAttributes rttr) {
        
        try {
            userService.restoreUser(userId);
            rttr.addFlashAttribute("msg", "회원번호 " + userId + " 복구 처리가 완료되었습니다.");
            
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "회원 복구 처리 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        
        // 복구 작업이 일어난 '정지된 회원 목록'으로 다시 리다이렉트합니다.
        return "redirect:/admin/user/suspendedlist";
    }
}

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

/**
 * 관리자 페이지의 사용자 관리와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 사용자 목록 조회, 사용자 정지, 정지된 사용자 목록 조회, 사용자 복구 등의 기능을 제공합니다.
 * </p>
 */
// ★★★ 수정: 클래스 레벨에 기본 경로 설정 및 .do 제거 ★★★
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user") 
public class AdminUserController {
    
    private final AdminUserService userService;
    
    /**
     * 사용자 목록 페이지를 반환합니다.
     * <p>
     * 검색 조건(searchType, keyword, status)과 페이징 정보(page)를 받아
     * 조건에 맞는 사용자 목록을 조회하고 뷰에 전달합니다.
     * </p>
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @param searchType 검색 유형 (예: 닉네임, 이메일)
     * @param keyword 검색어
     * @param status 사용자 상태 (예: 활성, 정지, 탈퇴)
     * @param page 현재 페이지 번호
     * @return 사용자 목록 페이지의 뷰 이름
     */
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
    

    /**
     * 특정 사용자를 정지 처리합니다.
     * <p>
     * 사용자 ID, 정지 사유, 정지 기간을 받아 해당 사용자를 정지 상태로 변경하고
     * 정지 로그를 기록합니다. 관리자 ID는 세션에서 가져오거나 임시로 하드코딩됩니다.
     * </p>
     * @param userId 정지할 사용자의 고유 ID
     * @param reason 정지 사유
     * @param duration 정지 기간 (일 단위)
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @param session 현재 HTTP 세션
     * @return 사용자 목록 페이지로 리다이렉트
     */
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
    
    /**
     * 정지된 사용자 목록 페이지를 반환합니다.
     * <p>
     * 정지된 사용자들의 목록을 조회하여 뷰에 전달합니다.
     * </p>
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 정지된 사용자 목록 페이지의 뷰 이름
     */
    @GetMapping("/suspendedlist")
    public String getSuspendedList(Model model) {
        
        // Service를 호출하여 정지된 회원 목록을 가져옵니다.
        List<suspendedUserDTO> suspendedlist = userService.getSuspendedUserList();
        
        // Model에 "suspendedlist"라는 이름으로 데이터를 담습니다.
        model.addAttribute("suspendedlist", suspendedlist);
        
        // Tiles에 정의할 뷰 이름 "content/admin/suspendedlist"를 반환합니다.
        return "admin/suspendedlist"; 
    }
    
    /**
     * 특정 사용자를 복구 처리합니다.
     * <p>
     * 사용자 ID를 받아 해당 사용자의 정지 상태를 해제하고 관련 정지 로그를 삭제합니다.
     * </p>
     * @param userId 복구할 사용자의 고유 ID
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 정지된 사용자 목록 페이지로 리다이렉트
     */
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

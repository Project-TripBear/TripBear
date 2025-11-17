package com.project.trip.admin.main.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.trip.admin.main.service.AdminMainService;

import lombok.RequiredArgsConstructor;

/**
 * 관리자 페이지의 메인 및 대시보드 관련 요청을 처리하는 컨트롤러입니다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminMainController {

    private final AdminMainService mainService;
    
    /**
     * 관리자 메인 대시보드 페이지를 반환합니다.
     * <p>
     * 대시보드에 필요한 통계 데이터(예: 신규 회원 수, 총 예약 수 등)와
     * 최신 신고 내역을 조회하여 모델에 추가한 후, 메인 페이지 뷰를 반환합니다.
     * </p>
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 관리자 메인 페이지의 뷰 이름
     */
    @GetMapping({"/main", "/dashboard"})
    public String adminMain(Model model) {
        
        // 서비스로부터 통계 데이터 맵을 받습니다.
        Map<String, Integer> stats = mainService.getDashboardStats();
        model.addAttribute("stats", stats);

        // 최신 신고 내역도 Model에 추가
        model.addAttribute("latestReports", mainService.getLatestReports());
        
        return "admin/main"; // tiles_admin.xml의 admin/main 정의와 일치
    }
    
    /**
     * 관리자 로그인 폼 페이지를 반환합니다.
     * @return 관리자 로그인 페이지의 뷰 이름
     */
    @GetMapping("/loginPage")
    public String adminLoginForm() {
        return "admin/admin_login";
    }
}	
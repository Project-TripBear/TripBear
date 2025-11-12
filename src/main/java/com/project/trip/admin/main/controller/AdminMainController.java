package com.project.trip.admin.main.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.trip.admin.main.service.AdminMainService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminMainController {

    private final AdminMainService mainService;
    
    /**
     * 어드민 메인 대시보드 (로그인 후)
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
     * ▼▼▼ 이 메서드를 추가했습니다! ▼▼▼
     * 어드민 로그인 폼 페이지를 보여주는 메서드 (GET 요청)
     */
    @GetMapping("/login")
    public String adminLoginForm() {
        
        // /WEB-INF/views/admin/login.jsp 파일을 보여주라는 의미
        return "admin/admin_login";
    }
}
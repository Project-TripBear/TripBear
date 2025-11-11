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
    
    @GetMapping({"/main", "/dashboard"})
    public String adminMain(Model model) {
        
        // 서비스로부터 통계 데이터 맵을 받습니다.
        Map<String, Integer> stats = mainService.getDashboardStats();
        model.addAttribute("stats", stats);

        // ★★★ [추가] 최신 신고 내역도 Model에 추가 ★★★
        model.addAttribute("latestReports", mainService.getLatestReports());
        
        return "admin/main"; // tiles_admin.xml의 admin/main 정의와 일치
    }
}
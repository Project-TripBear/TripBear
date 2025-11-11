package com.project.trip.admin.main.controller; //

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.trip.admin.main.service.AdminMainService; // ★ Service 임포트

import lombok.RequiredArgsConstructor; // ★ RequiredArgsConstructor 임포트

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // ★ final 필드 생성자 주입
public class AdminMainController {

    private final AdminMainService mainService; // ★ Service 주입

    /**
     * 관리자 메인 대시보드 페이지로 이동합니다.
     */
    @GetMapping("/main")
    public String adminMain(Model model) {
        
        // ★ [수정] Service에서 실제 DB 데이터 조회
        Map<String, Integer> stats = mainService.getDashboardStats();
        
        // ★ [수정] Model에 실제 데이터 담기
        model.addAttribute("totalMembers", stats.get("totalMembers"));
        model.addAttribute("todayReservations", stats.get("todayReservations"));
        model.addAttribute("pendingReports", stats.get("pendingReports"));
        
        // tiles_admin.xml에 정의된 "admin/main"을 반환
        return "admin/main";
    }
}
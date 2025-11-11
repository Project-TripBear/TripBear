package com.project.trip.admin.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    
    // 애플리케이션의 루트 경로(/) 요청을 처리합니다.
    // 로그에 찍힌 "/trip/" 요청은 보통 이 루트 경로로 해석됩니다.
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        
        // 1. 필요한 비즈니스 로직 처리 (예: 최신 게시글 목록 조회 등)
        
        // 2. 메인 화면 View 이름 반환
        // 예시: "main/home" (Tiles나 InternalResourceViewResolver 설정에 맞게 지정해야 함)
        return "admin/home"; 
    }
}
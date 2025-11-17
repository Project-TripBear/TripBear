package com.project.trip.AI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserRouteViewController {

    /**
     * 사용자가 저장한 '내 여행' 경로를 지도 위에 표시하는 페이지를 반환합니다.
     *
     * @param id Long 조회할 사용자 여행 경로의 ID
     * @param model Model 뷰에 사용자 경로 ID를 전달하기 위한 모델
     * @return String "ai.userRouteView" 뷰 이름을 반환하여 지도 페이지를 렌더링
     */
    // URL: /trip/user/route/view?id=123
    @GetMapping("/user/route/view")
    public String viewUserRoute(@RequestParam("id") Long id, Model model) {
        model.addAttribute("userRouteId", id);
        // JSP 실제 경로: /WEB-INF/views/content/ai/userRouteView.jsp
        return "ai.userRouteView";
    }
}

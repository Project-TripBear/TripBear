package com.project.trip.AI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserRouteViewController {

    // URL: /trip/user/route/view?id=123
    @GetMapping("/user/route/view")
    public String viewUserRoute(@RequestParam("id") Long id, Model model) {
        model.addAttribute("userRouteId", id);
        // JSP 실제 경로: /WEB-INF/views/content/ai/userRouteView.jsp
        return "ai.userRouteView";
    }
}

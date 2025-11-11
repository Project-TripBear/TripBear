package com.project.trip.AI.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.trip.AI.service.AiRouteSaveService;
import com.project.trip.mypage.model.CustomUser;

@Controller
public class AiRouteSaveController {

    @Autowired
    private AiRouteSaveService aiRouteSaveService;

    @PostMapping("/ai/saveUserRoute")
    @ResponseBody
    public Map<String, Object> saveUserRoute(@RequestParam("aiRouteId") Long aiRouteId,
                                             @AuthenticationPrincipal CustomUser user) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long newUserRouteId = aiRouteSaveService.saveUserRouteFromAi(aiRouteId, user.getUserId());
            result.put("success", true);
            result.put("newUserRouteId", newUserRouteId);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}

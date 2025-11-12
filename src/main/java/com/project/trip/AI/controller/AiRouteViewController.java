package com.project.trip.AI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.service.AiRouteViewService;

@Controller
@RequestMapping("/ai")
public class AiRouteViewController {

    @Autowired
    private AiRouteViewService aiRouteViewService;

    @GetMapping("/mapview")
    public String viewAiRoute(@RequestParam("id") Long aiRouteId, Model model) {
        RouteDTO route = aiRouteViewService.getAiRoute(aiRouteId);
        model.addAttribute("route", route);
        return "ai.aiMapView";
    }

    @GetMapping("/route/{id}")
    @ResponseBody
    public RouteDTO getAiRouteData(@PathVariable Long id) {
        return aiRouteViewService.getAiRoute(id);
    }
}

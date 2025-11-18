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

    /**
     * AI가 생성한 여행 경로를 지도 위에 표시하는 페이지를 반환합니다.
     *
     * @param aiRouteId Long 조회할 AI 여행 경로의 ID
     * @param model Model 뷰에 경로 데이터를 전달하기 위한 모델
     * @return String "ai.aiMapView" 뷰 이름을 반환하여 지도 페이지를 렌더링
     */
    @GetMapping("/mapview")
    public String viewAiRoute(@RequestParam("id") Long aiRouteId, Model model) {
        RouteDTO route = aiRouteViewService.getAiRoute(aiRouteId);
        model.addAttribute("route", route);
        return "ai.aiMapView";
    }

    /**
     * AI가 생성한 여행 경로 데이터를 JSON 형태로 반환하는 API입니다.
     *
     * @param id PathVariable 조회할 AI 여행 경로의 ID
     * @return RouteDTO 경로 데이터를 담은 DTO 객체
     */
    @GetMapping("/route/{id}")
    @ResponseBody
    public RouteDTO getAiRouteData(@PathVariable Long id) {
        return aiRouteViewService.getAiRoute(id);
    }
}

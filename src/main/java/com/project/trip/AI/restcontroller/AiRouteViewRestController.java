package com.project.trip.AI.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.service.AiRouteViewService;

/**
 * AI가 생성한 여행 경로 조회와 관련된 REST API 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 특정 AI 여행 경로의 상세 정보를 JSON 형태로 제공합니다.
 * </p>
 */
@RestController
@RequestMapping("/api/ai")
public class AiRouteViewRestController {

    @Autowired
    private AiRouteViewService aiRouteViewService;

    /**
     * 특정 AI 여행 경로의 상세 데이터를 JSON 형태로 반환합니다.
     *
     * @param id 조회할 AI 여행 경로의 고유 ID
     * @return AI 여행 경로 데이터를 담은 {@link RouteDTO} 객체
     */
    @GetMapping("/route/{id}")
    public RouteDTO getAiRouteData(@PathVariable Long id) {
        return aiRouteViewService.getAiRoute(id);
    }
}

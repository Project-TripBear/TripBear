package com.project.trip.AI.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.service.AiRouteViewService;

@RestController
@RequestMapping("/api/ai")
public class AiRouteViewRestController {

    @Autowired
    private AiRouteViewService aiRouteViewService;

    @GetMapping("/route/{id}")
    public RouteDTO getAiRouteData(@PathVariable Long id) {
        return aiRouteViewService.getAiRoute(id);
    }
}

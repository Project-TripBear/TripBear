package com.project.trip.board.routepost.restcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.trip.board.routepost.service.RoutePostService;

@RestController
@RequestMapping("/api/routepost")
public class RoutePostRestController {

    @Autowired
    private RoutePostService postService;

 // ===== 좋아요 토글 =====
    @PostMapping("/like/toggle")
    public boolean toggleLike(@RequestBody Map<String, Object> map) {
        return postService.toggleLike(map);
    }

    // ===== 스크랩 토글 =====
    @PostMapping("/scrap/toggle")
    public boolean toggleScrap(@RequestBody Map<String, Object> map) {
        return postService.toggleScrap(map);
    }
}

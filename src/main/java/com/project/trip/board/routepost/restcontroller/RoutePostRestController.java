package com.project.trip.board.routepost.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    
    @GetMapping("/like/status")
    @ResponseBody
    public boolean likeStatus(@RequestParam int routepostId,
                              @RequestParam int userId) {

        Map<String, Object> map = new HashMap<>();
        map.put("routepostId", routepostId);
        map.put("userId", userId);

        return postService.checkLike(map);
    }

    @GetMapping("/scrap/status")
    @ResponseBody
    public boolean scrapStatus(@RequestParam int routepostId,
                               @RequestParam int userId) {

        Map<String, Object> map = new HashMap<>();
        map.put("routepostId", routepostId);
        map.put("userId", userId);

        return postService.checkScrap(map);
    }



    
    
    
}

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

    // ===== 좋아요 =====
    @PostMapping("/like")
    public int addLike(@RequestBody Map<String, Object> map) {
        return postService.addLike(map);
    }

    @DeleteMapping("/like")
    public int removeLike(@RequestBody Map<String, Object> map) {
        return postService.removeLike(map);
    }

    // ===== 스크랩 =====
    @PostMapping("/scrap")
    public int addScrap(@RequestBody Map<String, Object> map) {
        return postService.addScrap(map);
    }

    @DeleteMapping("/scrap")
    public int removeScrap(@RequestBody Map<String, Object> map) {
        return postService.removeScrap(map);
    }

    // ===== 좋아요/스크랩 상태 확인 (선택사항) =====
    @PostMapping("/like/check")
    public boolean checkLike(@RequestBody Map<String, Object> map) {
        return postService.isLiked(map);
    }

    @PostMapping("/scrap/check")
    public boolean checkScrap(@RequestBody Map<String, Object> map) {
        return postService.isScrapped(map);
    }
}

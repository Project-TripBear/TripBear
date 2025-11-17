package com.project.trip.board.review.restcontroller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.trip.board.review.service.ReviewService;

@RestController
@RequestMapping("/api/review")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

    // ===== 좋아요 토글 =====
    @PostMapping("/like/toggle")
    public boolean toggleLike(@RequestBody Map<String, Object> map) {
        return reviewService.toggleLike(map);
    }

    // ===== 스크랩 토글 =====
    @PostMapping("/scrap/toggle")
    public boolean toggleScrap(@RequestBody Map<String, Object> map) {
        return reviewService.toggleScrap(map);
    }

    // ===== 좋아요 상태 조회 =====
    @GetMapping("/like/status")
    @ResponseBody
    public boolean likeStatus(@RequestParam int reviewPostId,
                              @RequestParam long userId) {

        Map<String, Object> map = new HashMap<>();
        map.put("reviewPostId", reviewPostId);
        map.put("userId", userId);

        return reviewService.checkLike(map);
    }

    // ===== 스크랩 상태 조회 =====
    @GetMapping("/scrap/status")
    @ResponseBody
    public boolean scrapStatus(@RequestParam int reviewPostId,
                               @RequestParam long userId) {

        Map<String, Object> map = new HashMap<>();
        map.put("reviewPostId", reviewPostId);
        map.put("userId", userId);

        return reviewService.checkScrap(map);
    }
}

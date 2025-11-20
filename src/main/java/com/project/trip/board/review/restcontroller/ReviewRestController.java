package com.project.trip.board.review.restcontroller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.trip.board.review.service.ReviewService;

/**
 * 리뷰 게시글의 좋아요 및 스크랩 관련 REST API를 처리하는 컨트롤러입니다.
 * 좋아요/스크랩 토글 및 상태 조회 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/review")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

    // ===== 좋아요 토글 =====
    /**
     * 특정 리뷰 게시글에 대한 사용자의 좋아요 상태를 토글합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 좋아요 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    @PostMapping("/like/toggle")
    public boolean toggleLike(@RequestBody Map<String, Object> map) {
        return reviewService.toggleLike(map);
    }

    // ===== 스크랩 토글 =====
    /**
     * 특정 리뷰 게시글에 대한 사용자의 스크랩 상태를 토글합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 스크랩 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    @PostMapping("/scrap/toggle")
    public boolean toggleScrap(@RequestBody Map<String, Object> map) {
        return reviewService.toggleScrap(map);
    }

    // ===== 좋아요 상태 조회 =====
    /**
     * 특정 리뷰 게시글에 대한 사용자의 좋아요 상태를 조회합니다.
     *
     * @param reviewPostId 좋아요 상태를 조회할 리뷰 게시글의 고유 번호
     * @param userId       좋아요 상태를 조회할 사용자의 고유 번호
     * @return 좋아요를 눌렀으면 true, 아니면 false
     */
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
    /**
     * 특정 리뷰 게시글에 대한 사용자의 스크랩 상태를 조회합니다.
     *
     * @param reviewPostId 스크랩 상태를 조회할 리뷰 게시글의 고유 번호
     * @param userId       스크랩 상태를 조회할 사용자의 고유 번호
     * @return 스크랩했으면 true, 아니면 false
     */
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

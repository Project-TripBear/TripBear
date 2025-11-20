package com.project.trip.AI.restcontroller;

import com.project.trip.AI.model.LocalSearchResponseDTO;
import com.project.trip.AI.service.KakaoLocalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 카카오 로컬 검색 API를 사용하여 장소 검색 기능을 제공하는 REST 컨트롤러입니다.
 * <p>
 * 키워드를 기반으로 장소를 검색하고, 검색 결과를 JSON 형태로 반환합니다.
 * </p>
 */
@RestController
@RequestMapping("/api/local")
public class LocalSearchController {

    @Autowired
    private KakaoLocalService service;

    /**
     * 키워드를 사용하여 카카오 로컬 검색 API를 통해 장소를 검색합니다.
     *
     * @param query 검색할 키워드
     * @return 카카오 로컬 검색 API의 원본 JSON 응답을 담은 {@code ResponseEntity<?>}
     */
    @GetMapping("/keyword")
    public ResponseEntity<?> search(@RequestParam String query) {

        try {
            // 👉 카카오 원본 JSON 그대로 반환
            String rawJson = service.searchKeywordRaw(query);
            return ResponseEntity.ok(rawJson);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"documents\":[]}");
        }
    }
}


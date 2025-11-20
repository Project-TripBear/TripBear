package com.project.trip.AI.restcontroller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trip.AI.service.NaverDirectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 네이버 길찾기 API를 사용하여 경로 정보를 제공하는 REST 컨트롤러입니다.
 * <p>
 * 출발지와 목적지의 좌표를 받아 다양한 교통수단(driving, pedestrian, bicycle 등)에 따른
 * 최적의 경로 정보를 JSON 형태로 반환합니다.
 * </p>
 */
@RestController
@RequestMapping("/api/naver")
public class NaverDirectionsController {

    @Autowired
    private NaverDirectionsService naverDirectionsService; // ⭐ 이것 때문에 오류났던 것

    /**
     * 네이버 길찾기 API를 호출하여 출발지와 목적지 간의 경로 정보를 반환합니다.
     *
     * @param start 출발지 좌표 (예: "127.12345,37.54321")
     * @param goal 목적지 좌표 (예: "127.67890,37.98765")
     * @param mode 교통수단 (기본값: "driving", "pedestrian", "bicycle" 등)
     * @return 네이버 길찾기 API 응답을 가공한 JSON 형태의 경로 정보를 담은 {@code ResponseEntity<?>}
     */
    @GetMapping("/directions")
    public ResponseEntity<?> route(
            @RequestParam String start,
            @RequestParam String goal,
            @RequestParam(defaultValue = "driving") String mode) {

        try {
            JsonNode root = naverDirectionsService.requestRoute(start, goal, mode);
            JsonNode routeNode = root.path("route");

            String[] types = {"trafast", "traoptimal", "tracomfort", "pedestrian", "bicycle"};

            ObjectMapper mapper = new ObjectMapper();
            var mergedPath = mapper.createArrayNode();

            for (String type : types) {
                if (routeNode.has(type)) {
                    for (JsonNode item : routeNode.get(type)) {
                        JsonNode path = item.get("path");
                        for (JsonNode p : path) {
                            mergedPath.add(p);
                        }
                    }
                    break;
                }
            }

            var result = mapper.createObjectNode();
            result.put("mode", mode);
            result.set("path", mergedPath);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\":\"fail\"}");
        }
    }
}

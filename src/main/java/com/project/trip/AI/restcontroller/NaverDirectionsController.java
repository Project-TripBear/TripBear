package com.project.trip.AI.restcontroller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trip.AI.service.NaverDirectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/naver")
public class NaverDirectionsController {

    @Autowired
    private NaverDirectionsService naverDirectionsService; // ⭐ 이것 때문에 오류났던 것

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

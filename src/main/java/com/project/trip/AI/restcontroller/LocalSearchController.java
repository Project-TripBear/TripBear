package com.project.trip.AI.restcontroller;

import com.project.trip.AI.model.LocalSearchResponseDTO;
import com.project.trip.AI.service.KakaoLocalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/local")
public class LocalSearchController {

    @Autowired
    private KakaoLocalService service;

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


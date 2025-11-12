package com.project.trip.AI.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.trip.AI.model.UserRouteDTO;
import com.project.trip.AI.service.UserRouteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/route")
@RequiredArgsConstructor
public class UserRouteRestController {

    private final UserRouteService userRouteService;

    // ✅ 사용자 루트 조회 (userRouteView.jsp)
    @GetMapping("/{id}")
    public ResponseEntity<UserRouteDTO> getUserRoute(@PathVariable("id") Long id) {
        UserRouteDTO dto = userRouteService.getUserRouteWithStops(id);
        return (dto == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    // ✅ 사용자 루트 삭제 (userRouteView.jsp)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserRoute(@PathVariable("id") Long id) {
        int deleted = userRouteService.deleteUserRouteCascade(id);
        return (deleted > 0)
            ? ResponseEntity.ok("삭제 완료")
            : ResponseEntity.notFound().build();
    }
}

package com.project.trip.AI.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.AI.model.ReorderRequest;
import com.project.trip.AI.model.UserRouteDTO;
import com.project.trip.AI.service.UserRouteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/route")
@RequiredArgsConstructor
public class UserRouteRestController {

    private final UserRouteService userRouteService;

    // ✅ 사용자 루트 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserRouteDTO> getUserRoute(@PathVariable("id") Long id) {
        UserRouteDTO dto = userRouteService.getUserRouteWithStops(id);
        return (dto == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    // ✅ 사용자 루트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserRoute(@PathVariable("id") Long id) {
        int deleted = userRouteService.deleteUserRouteCascade(id);
        return (deleted > 0)
            ? ResponseEntity.ok("삭제 완료")
            : ResponseEntity.notFound().build();
    }

    // ✅ 방문 순서/일차 수정
    @PatchMapping("/stop/{stopId}")
    public ResponseEntity<String> updateStopOrder(
            @PathVariable("stopId") Long stopId,
            @RequestParam("day") int day,
            @RequestParam("order") int order) {

        int updated = userRouteService.updateStopOrder(stopId, day, order);
        return (updated > 0)
            ? ResponseEntity.ok("순서/날짜 수정 완료")
            : ResponseEntity.notFound().build();
    }

    // ✅ 이동수단 수정
    @PatchMapping("/stop/{stopId}/mode")
    public ResponseEntity<String> updateTransportMode(
            @PathVariable("stopId") Long stopId,
            @RequestParam("mode") String mode) {

        int updated = userRouteService.updateTransportMode(stopId, mode);
        return (updated > 0)
            ? ResponseEntity.ok("이동수단 변경 완료")
            : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/stop/reorder")
    public ResponseEntity<?> reorderStops(@RequestBody ReorderRequest req) {
        userRouteService.updateStopOrders(req.getDay(), req.getStops());
        return ResponseEntity.ok().build();
    }


    
}

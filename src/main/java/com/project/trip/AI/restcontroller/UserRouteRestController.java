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

    // ==============================
    //  루트 조회
    // ==============================
    @GetMapping("/{id}")
    public ResponseEntity<UserRouteDTO> getUserRoute(@PathVariable Long id) {
        UserRouteDTO dto = userRouteService.getUserRouteWithStops(id);
        return (dto == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(dto);
    }

    // ==============================
    //  루트 삭제
    // ==============================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserRoute(@PathVariable Long id) {
        int deleted = userRouteService.deleteUserRouteCascade(id);
        return (deleted > 0)
                ? ResponseEntity.ok("삭제 완료")
                : ResponseEntity.notFound().build();
    }

    // ==============================
    //  🔥 일차(day) 단독 수정
    // ==============================
    @PatchMapping("/stop/{stopId}/day")
    public ResponseEntity<String> updateStopDay(
            @PathVariable Long stopId,
            @RequestParam int day) {

        int updated = userRouteService.updateStopDay(stopId, day);
        return (updated > 0)
                ? ResponseEntity.ok("일차 수정 완료")
                : ResponseEntity.notFound().build();
    }

    // ==============================
    //  🔥 순서(order) 단독 수정
    // ==============================
    @PatchMapping("/stop/{stopId}/order")
    public ResponseEntity<String> updateStopOrder(
            @PathVariable Long stopId,
            @RequestParam int order) {

        int updated = userRouteService.updateOrder(stopId, order);
        return (updated > 0)
                ? ResponseEntity.ok("순서 수정 완료")
                : ResponseEntity.notFound().build();
    }

    // ==============================
    //  이동수단 변경
    // ==============================
    @PatchMapping("/stop/{stopId}/mode")
    public ResponseEntity<String> updateTransportMode(
            @PathVariable Long stopId,
            @RequestParam String mode) {

        int updated = userRouteService.updateTransportMode(stopId, mode);
        return (updated > 0)
                ? ResponseEntity.ok("이동수단 변경 완료")
                : ResponseEntity.notFound().build();
    }

    // ==============================
    //  🔥 Bulk 재정렬
    // ==============================
    @PostMapping("/stop/bulk/reorder")
    public ResponseEntity<?> reorderStops(@RequestBody ReorderRequest req) {
        userRouteService.updateStopOrders(req.getDay(), req.getStops());
        return ResponseEntity.ok().build();
    }
}

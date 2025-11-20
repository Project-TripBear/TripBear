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

/**
 * 사용자가 저장한 여행 경로(내 여행)와 관련된 REST API 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 사용자 경로 조회, 삭제, 경유지 순서 및 교통수단 변경, 일차 변경 등
 * 사용자 정의 여행 경로 관리를 위한 기능을 제공합니다.
 * </p>
 */
@RestController
@RequestMapping("/api/user/route")
@RequiredArgsConstructor
public class UserRouteRestController {

    private final UserRouteService userRouteService;

    // ==============================
    //  루트 조회
    // ==============================
    /**
     * 특정 사용자 여행 경로의 상세 정보를 조회합니다.
     *
     * @param id 조회할 사용자 여행 경로의 고유 ID
     * @return 조회된 {@link UserRouteDTO} 객체 또는 404 Not Found 응답
     */
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
    /**
     * 특정 사용자 여행 경로를 삭제합니다.
     * 경로와 관련된 모든 경유지 정보도 함께 삭제됩니다.
     *
     * @param id 삭제할 사용자 여행 경로의 고유 ID
     * @return 삭제 성공 여부를 나타내는 {@code ResponseEntity<String>}
     */
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
    /**
     * 특정 경유지의 일차(day)를 업데이트합니다.
     *
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param day 새로운 일차 값
     * @return 업데이트 성공 여부를 나타내는 {@code ResponseEntity<String>}
     */
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
    /**
     * 특정 경유지의 순서(order)를 업데이트합니다.
     *
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param order 새로운 순서 값
     * @return 업데이트 성공 여부를 나타내는 {@code ResponseEntity<String>}
     */
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
    /**
     * 특정 경유지까지의 이동 수단을 업데이트합니다.
     *
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param mode 새로운 이동 수단 (예: "CAR", "WALK")
     * @return 업데이트 성공 여부를 나타내는 {@code ResponseEntity<String>}
     */
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
    /**
     * 특정 일차에 해당하는 경유지들의 순서를 일괄적으로 재정렬합니다.
     *
     * @param req 재정렬할 일차와 해당 일차의 경유지 목록을 담은 {@link ReorderRequest} 객체
     * @return 성공 시 200 OK 응답
     */
    @PostMapping("/stop/bulk/reorder")
    public ResponseEntity<?> reorderStops(@RequestBody ReorderRequest req) {
        userRouteService.updateStopOrders(req.getDay(), req.getStops());
        return ResponseEntity.ok().build();
    }
}

package com.project.trip.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.reservation.model.IntegratedReservation;
import com.project.trip.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

/**
 * 예약과 관련된 RESTful API 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 통합 예약 생성 및 조회 기능을 제공하여 클라이언트 측에서 비동기적으로 예약 데이터를
 * 처리할 수 있도록 합니다.
 * </p>
 */
@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationRestController {

	private final ReservationService reservationService;

	@PostMapping
	public ResponseEntity<Long> createReservation(@RequestBody IntegratedReservation req) {

		try {
			Long reservationId = reservationService.createIntegratedReservation(req.getReservation(),
					req.getAccomReservation(), req.getCarReservation());

			return ResponseEntity.ok(reservationId);

		} catch (Exception e) {
			throw new RuntimeException("예약 등록 중 오류 발생", e);
		}
	}
	
	@GetMapping("/{reservationId}")
	public ResponseEntity<IntegratedReservation> getReservation(
	        @PathVariable Long reservationId) throws Exception {

	    return ResponseEntity.ok(reservationService.getIntegratedReservation(reservationId));
	}


}

package com.project.trip.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.reservation.model.IntegratedReservationRequest;
import com.project.trip.reservation.model.IntegratedReservationResponse;
import com.project.trip.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationRestController {

	private final ReservationService reservationService;

	@PostMapping
	public ResponseEntity<Long> createReservation(@RequestBody IntegratedReservationRequest req) {

		try {
			Long reservationId = reservationService.createIntegratedReservation(req.getReservation(),
					req.getAccomReservation(), req.getCarReservation());

			return ResponseEntity.ok(reservationId);

		} catch (Exception e) {
			throw new RuntimeException("예약 등록 중 오류 발생", e);
		}
	}
	
	@GetMapping("/{reservationId}")
	public ResponseEntity<IntegratedReservationResponse> getReservation(
	        @PathVariable Long reservationId) throws Exception {

	    return ResponseEntity.ok(reservationService.getIntegratedReservation(reservationId));
	}


}

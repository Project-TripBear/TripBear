package com.project.trip.reservation.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.trip.reservation.model.IntegratedReservation;
import com.project.trip.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationConfirmController {

    private final ReservationService reservationService;

    @GetMapping("/reservation/confirm")
    public ModelAndView confirm(HttpServletRequest req) throws Exception {

        String region = req.getParameter("region");
        String checkin = req.getParameter("checkin");
        String checkout = req.getParameter("checkout");
        String people = req.getParameter("people");
        String roomId = req.getParameter("roomId");
        String carId = req.getParameter("carId"); // optional

        // ✅ 총 금액 계산 (서비스에서 숙박비 + 차량비)
        long totalPrice = reservationService.calculateTotalPrice(
                roomId != null ? Long.parseLong(roomId) : null,
                carId != null && !carId.isBlank() ? Long.parseLong(carId) : null,
                checkin, checkout
        );

        // ✅ 필요한 정보 DTO로 담기
        IntegratedReservation data = reservationService.getIntegratedReservationPreview(
                region, checkin, checkout, people, roomId, carId
        );

        ModelAndView mav = new ModelAndView("reservation.confirm");
        mav.addObject("data", data);
        mav.addObject("totalPrice", totalPrice);

        return mav;
    }
}


package com.project.trip.reservation.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.trip.reservation.model.IntegratedReservation;
import com.project.trip.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationConfirmController {

    private final ReservationService reservationService;

    @GetMapping("/confirm")
    public ModelAndView confirm(HttpServletRequest req) throws Exception {

        String region = req.getParameter("region");
        String checkin = req.getParameter("checkin");
        String checkout = req.getParameter("checkout");
        String roomId = req.getParameter("roomId");
        String carId = req.getParameter("carId");
        String rentalStart = req.getParameter("rentalStart");
        String rentalEnd = req.getParameter("rentalEnd");

        // 숙소·차량 정보 불러오기 (이미 존재)
        IntegratedReservation data = reservationService.getIntegratedReservationPreview(
                region, checkin, checkout, roomId, carId);

        // 날짜 정보도 같이 셋팅
        data.setCheckin(rentalStart);
        data.setCheckout(rentalEnd);

        long totalPrice = reservationService.calculateTotalPrice(
    	    roomId != null && !roomId.isBlank() ? Long.parseLong(roomId) : null,
    	    carId != null && !carId.isBlank() ? Long.parseLong(carId) : null,
    	    checkin, checkout
    	);

        ModelAndView mav = new ModelAndView("reservation.confirm");
        mav.addObject("data", data);
        mav.addObject("totalPrice", totalPrice);
        return mav;
    }


}


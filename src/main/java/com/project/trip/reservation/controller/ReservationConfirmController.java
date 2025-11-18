package com.project.trip.reservation.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.trip.reservation.model.IntegratedReservation;
import com.project.trip.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

/**
 * 예약 확인 페이지와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 사용자가 선택한 숙소 및 렌터카 정보를 기반으로 통합 예약 미리보기를 제공하고,
 * 최종 결제 금액을 계산하여 예약 확인 페이지로 전달하는 기능을 제공합니다.
 * </p>
 */
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


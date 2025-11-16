package com.project.trip.reservation.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trip.reservation.model.RentalCarCardDTO;
import com.project.trip.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationSelectCarController {

    private final ReservationService reservationService;

    @GetMapping("/select-car")
    public ModelAndView selectCar(HttpServletRequest req) throws Exception {

        // ✅ 기본 파라미터
        String region   = req.getParameter("region");
        String checkin  = req.getParameter("checkin");
        String checkout = req.getParameter("checkout");
        String roomId   = req.getParameter("roomId");

        // ✅ 필터 파라미터
        String carType  = req.getParameter("carType");
        String fuelType = req.getParameter("fuelType");
        String seatsStr = req.getParameter("seats");
        String maxPriceStr = req.getParameter("maxPrice");

        Integer seats = (seatsStr != null && !seatsStr.isBlank()) ? Integer.parseInt(seatsStr) : null;
        Integer maxPrice = (maxPriceStr != null && !maxPriceStr.isBlank()) ? Integer.parseInt(maxPriceStr) : null;

        // ✅ 차량 목록 조회
        List<RentalCarCardDTO> cars = reservationService.findCarsByRegion(region, carType, fuelType, seats, maxPrice);

        // ✅ 필터 옵션 (DB에서 DISTINCT로 가져오기)
        Map<String, List<?>> filters = reservationService.getCarFilterOptions();

        // ✅ ModelAndView 구성
        ModelAndView mav = new ModelAndView("reservation.select-car");
        mav.addObject("region", region);
        mav.addObject("checkin", checkin);
        mav.addObject("checkout", checkout);
        mav.addObject("roomId", roomId);

        mav.addObject("carType", carType);
        mav.addObject("fuelType", fuelType);
        mav.addObject("seats", seats);
        mav.addObject("maxPrice", maxPrice);

        mav.addObject("carList", cars);
        mav.addObject("filters", filters); // 👈 JSP에서 동적 필터 출력용
        mav.addObject("carsJson", new ObjectMapper().writeValueAsString(cars));

        System.out.println("🚗 [SelectCarController] 차량 수: " + (cars == null ? 0 : cars.size()));

        return mav;
    }
}
package com.project.trip.reservation.controller;

import java.util.List;

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

        String region   = req.getParameter("region");
        String checkin  = req.getParameter("checkin");
        String checkout = req.getParameter("checkout");
        String people   = req.getParameter("people");
        String roomId   = req.getParameter("roomId");

        // 차량 카드 조회
        List<RentalCarCardDTO> cars = reservationService.findCarsByRegion(region);

        ModelAndView mav = new ModelAndView("reservation.select-car");
        mav.addObject("region", region);
        mav.addObject("checkin", checkin);
        mav.addObject("checkout", checkout);
        mav.addObject("people", people);
        mav.addObject("roomId", roomId);

        mav.addObject("cars", cars);
        mav.addObject("carsJson", new ObjectMapper().writeValueAsString(cars));

        return mav;
    }

}



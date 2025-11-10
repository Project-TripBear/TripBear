package com.project.trip.reservation.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.trip.reservation.model.IntegratedReservation;
import com.project.trip.reservation.service.ReservationService;

@Controller
public class ReservationDetailController {
	
	@Autowired
	private ReservationService reservationService;
	
	@GetMapping("/reservation/detail.do")
	public ModelAndView detail(HttpServletRequest request) throws Exception {

	    String reservationId = request.getParameter("reservationId");

	    IntegratedReservation data = reservationService.getIntegratedReservation(Long.parseLong(reservationId));

	    ModelAndView mav = new ModelAndView("reservation.detail");
	    mav.addObject("data", data);

	    return mav;
	}

	

}

package com.project.trip.reservation.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trip.reservation.model.AccomRoomCardDTO;
import com.project.trip.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

/**
 * 숙소 선택 페이지와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 사용자가 선택한 지역 및 날짜를 기반으로 이용 가능한 숙소 목록을 조회하고,
 * 숙소 선택 페이지로 전달하는 기능을 제공합니다.
 * </p>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationSelectAccomController {

	private final ReservationService reservationService;

    // 숙소 선택 화면

    @GetMapping("/select-accom")
    public ModelAndView selectAccom(HttpServletRequest req) throws Exception {
        
    	System.out.println("### SELECT ACCOM CALLED ###");

    	// 루트에서 넘어온 파라미터 (문자열 region, 인원, 날짜)
        String region   = req.getParameter("region");    // 예: "부산"
        String checkin  = req.getParameter("checkin");   // 예: "2025-11-12"
        String checkout = req.getParameter("checkout");  // 예: "2025-11-14"

        // 방 카드 리스트 조회 (region 텍스트 → 내부에서 JOIN 처리)
        List<AccomRoomCardDTO> rooms = reservationService.findRoomsByRegion(region);

        ModelAndView mav = new ModelAndView("reservation.select-accom");
        mav.addObject("region", region);
        mav.addObject("checkin", checkin);
        mav.addObject("checkout", checkout);
        mav.addObject("rooms", rooms);
        
        mav.addObject("roomsJson", new ObjectMapper().writeValueAsString(rooms));
        
        return mav;
    }
	
}

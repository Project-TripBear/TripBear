package com.project.trip.reservation.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.trip.reservation.mapper.ReservationMapper;
import com.project.trip.reservation.model.AccomReservationDTO;
import com.project.trip.reservation.model.CarReservationDTO;
import com.project.trip.reservation.model.ReservationDTO;
import com.project.trip.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationCompleteController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @PostMapping("/complete")
    public ModelAndView complete(HttpServletRequest req) throws Exception {

        // ✅ 테스트용 userRouteId 하드코딩
        long userId = 1L; // 임시 회원 ID
        long userRouteId = 1L; // AI 추천 루트 (테스트용)
        int statusId = 1; // 예약요청 상태

        // ✅ 파라미터 받기
        long roomId = Long.parseLong(req.getParameter("roomId"));

        String region = req.getParameter("region");
        String checkin = req.getParameter("checkin");
        String checkout = req.getParameter("checkout");
        String rentalStart = req.getParameter("rentalStart");
        String rentalEnd = req.getParameter("rentalEnd");
        String accomNotes   = req.getParameter("accomNotes");
        String carNotes     = req.getParameter("carNotes");
        String people = req.getParameter("people");
        String pickupLocation = req.getParameter("pickupLocation");
        String dropoffLocation = req.getParameter("dropoffLocation");
        String carIdParam = req.getParameter("carId");

        Long carId = (carIdParam != null && !carIdParam.isBlank()) ? Long.parseLong(carIdParam) : null;

        // 숙박일수
        var sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        long nights = (sdf.parse(checkout).getTime() - sdf.parse(checkin).getTime()) / (1000*60*60*24);
        
        // 단가 조회
        Integer roomPricePerNight = reservationMapper.getRoomPricePerNight(roomId); // Mapper 재사용
        int roomTotal = roomPricePerNight * (int) nights;
        
        int carTotal = 0;
        if (carId != null) {
            Integer carPricePerDay = reservationMapper.getCarPricePerDay(carId);
            carTotal = carPricePerDay * (int) nights;
        }
        
        long totalPrice = (long) roomTotal + (long) carTotal;

        // ✅ DTO 생성
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserId(userId);
        reservationDTO.setUserRouteId(userRouteId);
        reservationDTO.setStatusId(statusId);
        reservationDTO.setReservationPrice(totalPrice);
        reservationDTO.setReservationStartDate(checkin);
        reservationDTO.setReservationEndDate(checkout);

        AccomReservationDTO accomDTO = new AccomReservationDTO();
        accomDTO.setRoomId(roomId);
        accomDTO.setUserRouteId(userRouteId);
        accomDTO.setGuestCount(Integer.parseInt(people));
        accomDTO.setRoomTotalPrice(roomTotal);
        accomDTO.setCheckinDate(checkin);
        accomDTO.setCheckoutDate(checkout);
        accomDTO.setAccomNotes(accomNotes);

        CarReservationDTO carDTO = null;
        if (carId != null) {
            carDTO = new CarReservationDTO();
            carDTO.setCarId(carId);
            carDTO.setUserRouteId(userRouteId);
            carDTO.setPickupDate(rentalStart);
            carDTO.setDropoffDate(rentalEnd);
            carDTO.setCarTotalPrice(carTotal);
			carDTO.setPickupLocation(pickupLocation);
			carDTO.setDropoffLocation(dropoffLocation);
			carDTO.setCarNotes(carNotes);
        }

        // ✅ 통합예약 INSERT
        Long reservationId = reservationService.createIntegratedReservation(reservationDTO, accomDTO, carDTO);

        // ✅ 예약 완료 페이지로 이동
        ModelAndView mav = new ModelAndView("reservation.complete");
        mav.addObject("reservationId", reservationId);
        mav.addObject("region", region);
        mav.addObject("checkin", checkin);
        mav.addObject("checkout", checkout);
        mav.addObject("totalPrice", totalPrice);
        return mav;
    }
}

	


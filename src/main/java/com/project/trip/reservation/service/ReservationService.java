package com.project.trip.reservation.service;

import java.util.List;
import java.util.Map;

import com.project.trip.reservation.model.AccomReservationDTO;
import com.project.trip.reservation.model.AccomRoomCardDTO;
import com.project.trip.reservation.model.CarReservationDTO;
import com.project.trip.reservation.model.IntegratedReservation;
import com.project.trip.reservation.model.RentalCarCardDTO;
import com.project.trip.reservation.model.ReservationDTO;

/**
 * 예약과 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 * <p>
 * 통합 예약 생성 및 조회, 숙소 및 렌터카 목록 조회, 가격 계산, 필터링 옵션 제공 등
 * 예약 시스템 운영에 필요한 다양한 기능을 제공합니다.
 * </p>
 */
public interface ReservationService {

    /**
     * 통합 예약을 생성합니다.
     * <p>
     * {@link ReservationDTO}, {@link AccomReservationDTO}, {@link CarReservationDTO} 정보를 받아
     * 데이터베이스에 저장하고, 생성된 예약의 고유 ID를 반환합니다.
     * </p>
     * @param reservationDTO 통합 예약의 기본 정보
     * @param accomReservationDTO 숙소 예약 정보
     * @param carReservationDTO 렌터카 예약 정보 (선택 사항, null 가능)
     * @return 생성된 예약의 고유 ID
     * @throws Exception 예약 생성 중 발생할 수 있는 예외
     */
    Long createIntegratedReservation(
            ReservationDTO reservationDTO,
            AccomReservationDTO accomReservationDTO,
            CarReservationDTO carReservationDTO
    ) throws Exception;
    
    /**
     * 특정 예약 ID에 해당하는 통합 예약 정보를 조회합니다.
     * <p>
     * 예약 기본 정보, 숙소 예약 정보, 렌터카 예약 정보를 모두 포함하여 반환합니다.
     * </p>
     * @param reservationId 조회할 예약의 고유 ID
     * @return 조회된 통합 예약 정보 {@link IntegratedReservation}
     * @throws Exception 예약 조회 중 발생할 수 있는 예외
     */
    IntegratedReservation getIntegratedReservation(Long reservationId) throws Exception;
    
    /**
     * 특정 지역에 해당하는 숙소 객실 목록을 조회합니다.
     *
     * @param region 조회할 지역 이름
     * @return 해당 지역의 숙소 객실 카드 정보 {@code List<AccomRoomCardDTO>}
     * @throws Exception 숙소 객실 조회 중 발생할 수 있는 예외
     */
    List<AccomRoomCardDTO> findRoomsByRegion(String region) throws Exception;

    /**
     * 특정 지역에 해당하는 렌터카 목록을 조회합니다.
     * <p>
     * 차량 타입, 연료 타입, 좌석 수, 최대 가격 등의 필터링 조건을 적용할 수 있습니다.
     * </p>
     * @param region 조회할 지역 이름
     * @param carType 필터링할 차량 타입 (예: "경차", "소형")
     * @param fuelType 필터링할 연료 타입 (예: "휘발유", "경유")
     * @param seats 필터링할 좌석 수
     * @param maxPrice 필터링할 최대 가격
     * @return 해당 지역의 렌터카 카드 정보 {@code List<RentalCarCardDTO>}
     */
    List<RentalCarCardDTO> findCarsByRegion(String region, String carType, String fuelType, Integer seats, Integer maxPrice);


	/**
	 * 숙소 및 렌터카 예약의 총 가격을 계산합니다.
	 * <p>
	 * 숙소 ID, 렌터카 ID, 체크인/체크아웃 날짜를 기반으로 각 항목의 가격을 조회하고 합산합니다.
	 * </p>
	 * @param roomId 예약할 숙소 객실의 고유 ID (null 가능)
	 * @param carId 예약할 렌터카의 고유 ID (null 가능)
	 * @param checkin 숙소 체크인 날짜 (yyyy-MM-dd 형식)
	 * @param checkout 숙소 체크아웃 날짜 (yyyy-MM-dd 형식)
	 * @return 계산된 총 가격
	 * @throws Exception 가격 계산 중 발생할 수 있는 예외
	 */
	long calculateTotalPrice(Long roomId, Long carId, String checkin, String checkout) throws Exception;
	/**
	 * 예약 확인 페이지에 표시할 통합 예약 미리보기 정보를 조회합니다.
	 * <p>
	 * 지역, 날짜, 숙소 ID, 렌터카 ID를 기반으로 숙소 및 렌터카의 상세 정보를 조회하여
	 * {@link IntegratedReservation} 객체에 담아 반환합니다.
	 * </p>
	 * @param region 예약 지역
	 * @param checkin 체크인 날짜 (yyyy-MM-dd 형식)
	 * @param checkout 체크아웃 날짜 (yyyy-MM-dd 형식)
	 * @param roomId 예약할 숙소 객실의 고유 ID (문자열, null 가능)
	 * @param carId 예약할 렌터카의 고유 ID (문자열, null 가능)
	 * @return 통합 예약 미리보기 정보 {@link IntegratedReservation}
	 * @throws Exception 미리보기 정보 조회 중 발생할 수 있는 예외
	 */
	IntegratedReservation getIntegratedReservationPreview(String region, String checkin, String checkout, String roomId, String carId) throws Exception;

	/**
	 * 렌터카 필터링을 위한 옵션 목록(차량 타입, 연료 타입, 좌석 수)을 조회합니다.
	 *
	 * @return 각 필터 옵션 목록을 담은 {@code Map<String, List<?>>}
	 */
	Map<String, List<?>> getCarFilterOptions();
	
	/**
	 * 숙소 및 렌터카 예약의 총 가격을 계산합니다.
	 * <p>
	 * 숙소 ID, 렌터카 ID, 체크인/체크아웃 날짜, 렌터카 대여/반납 날짜를 기반으로
	 * 각 항목의 가격을 조회하고 합산합니다.
	 * </p>
	 * @param roomId 예약할 숙소 객실의 고유 ID (null 가능)
	 * @param carId 예약할 렌터카의 고유 ID (null 가능)
	 * @param checkin 숙소 체크인 날짜 (yyyy-MM-dd 형식)
	 * @param checkout 숙소 체크아웃 날짜 (yyyy-MM-dd 형식)
	 * @param rentalStart 렌터카 대여 시작 날짜 (yyyy-MM-dd 형식)
	 * @param rentalEnd 렌터카 대여 종료 날짜 (yyyy-MM-dd 형식)
	 * @return 계산된 총 가격
	 * @throws Exception 가격 계산 중 발생할 수 있는 예외
	 */
	long calculateTotalPrice(Long roomId, Long carId, String checkin, String checkout,
            String rentalStart, String rentalEnd) throws Exception;

	/**
	 * 특정 렌터카의 정보를 조회합니다.
	 *
	 * @param carId 조회할 렌터카의 고유 ID
	 * @return 조회된 렌터카 정보 {@link RentalCarCardDTO}
	 */
	Object getCarInfo(long carId);
	
	/**
	 * 특정 숙소 객실의 정보를 조회합니다.
	 *
	 * @param roomId 조회할 숙소 객실의 고유 ID
	 * @return 조회된 숙소 객실 정보 {@link AccomRoomCardDTO}
	 */
	Object getRoomInfo(long roomId);

}

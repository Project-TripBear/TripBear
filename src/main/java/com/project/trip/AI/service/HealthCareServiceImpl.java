package com.project.trip.AI.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.AI.mapper.AiMapper;
import com.project.trip.AI.model.HealthCareLogDTO;
import com.project.trip.AI.model.RouteStopDTO;

/**
 * {@link HealthCareService} 인터페이스의 구현 클래스입니다.
 * <p>
 * AI 추천 경로를 이용한 사용자의 건강 관리 활동 기록을 처리합니다.
 * 특정 경유지에서의 활동을 기반으로 칼로리 소모량 등을 계산하여 헬스케어 로그를 데이터베이스에 저장합니다.
 * </p>
 */
@Service
public class HealthCareServiceImpl implements HealthCareService{

	@Autowired
	private AiMapper aimapper;
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Override
	public void saveHealthCareLog(long userId, double userWeight, RouteStopDTO stop, LocalDate tripStartDate) {

		double mets = 0;
		String activityCode = stop.getActivityCode();
		
		if (activityCode == null) {
			return;
		}
		
		switch (activityCode) {
			case "WALK_SLOW": 	mets = 2.0; break;
			case "WALK_NORMAL": mets = 3.5; break;
			case "WALK_FAST": 	mets = 4.3; break;
			case "HIKE_LIGHT": 	mets = 5.3; break;
			default:
				return;
		}
		
		double durationInHours = (double) stop.getDurationInMinutes() / 60.0;
		double caloriesBurned = mets * userWeight * durationInHours;
		
		HealthCareLogDTO logDTO = new HealthCareLogDTO();
		logDTO.setUserId(userId);
		logDTO.setAiRouteStopId(stop.getAiRouteStopId());
		
		
		//날짜 계산
		LocalDate healthcareDate = tripStartDate.plusDays(stop.getAiRouteDay() - 1);
		logDTO.setHealthcareDate(healthcareDate.format(DATE_FORMATTER));

		logDTO.setHealthcareDistanceKm(stop.getWalkingDistanceKm());
		logDTO.setHealthcareStepsCount(stop.getWalkingStepsCount());
		logDTO.setHealthcareCaloriesBurned(caloriesBurned);
		
		aimapper.insertHealthCareLog(logDTO);
		
		System.out.println(String.format(
				"[HealthCareService] 헬스케어 로그 저장: RouteID=%d, StopId= %d, 날짜=%s, 칼로리=%.1f kcal", 
				stop.getAiRouteId(), stop.getAiRouteStopId(),logDTO.getHealthcareDate(), caloriesBurned
		));
		
	}

}

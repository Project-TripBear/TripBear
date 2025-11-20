package com.project.trip.AI.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.AI.mapper.AiMapper;
import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.RouteStopDTO;
import com.project.trip.weather.model.WeatherDTO;
import com.project.trip.weather.service.OpenWeatherService;
import com.project.trip.weather.service.WeatherService;


/**
 * {@link AiService} 인터페이스의 구현 클래스입니다.
 * <p>
 * Gemini AI를 통해 여행 경로를 생성하고, 날씨 정보를 활용하며,
 * 생성된 경로와 경유지를 데이터베이스에 저장하는 비즈니스 로직을 처리합니다.
 * '헬스케어' 여행 스타일의 경우 헬스케어 로그를 기록하는 기능도 포함되어 있습니다.
 * </p>
 */
@Service
public class AiServiceImpl implements AiService {

	@Autowired
	private AiMapper aimapper;
	
	@Autowired
	private GeminiService geminiService;
	
	@Autowired
	private WeatherService weatherService;
	
	@Autowired
	private HealthCareService healthCareService;
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * 이 메소드는 {@code @Transactional}로 관리됩니다.
	 * 사용자 선호도와 날씨 정보를 기반으로 Gemini AI를 통해 여행 경로를 생성하고,
	 * 생성된 경로와 각 경유지를 데이터베이스에 저장합니다.
	 * '헬스케어' 여행 스타일인 경우, 각 경유지에 대한 헬스케어 로그도 함께 저장합니다.
	 * </p>
	 * @param preferences 사용자 선호도 정보가 담긴 {@link AiRouteRequestDTO} 객체
	 * @param longUserId 경로를 생성하는 사용자의 고유 ID
	 * @param userWeight 사용자의 몸무게 (헬스케어 계산에 사용)
	 * @return 생성 및 저장된 AI 여행 경로 정보를 담은 {@link RouteDTO} 객체
	 */
	@Override
	@Transactional
	public RouteDTO createAndSaveAiRoute(AiRouteRequestDTO preferences,long longUserId, double userWeight) { 
        
		WeatherDTO weather =
			    weatherService.getWeather(preferences.getCity(), preferences.getStartDate());
			RouteDTO generatedRoute =
			    geminiService.generateRoute(preferences, weather);
			
        generatedRoute.setUserId(longUserId); 
        
       generatedRoute.setAiRouteRegion(preferences.getCity());
        generatedRoute.setAiRouteStartDate(preferences.getStartDate());
        generatedRoute.setAiRouteEndDate(preferences.getEndDate());
        
        
        aimapper.insertAiRoute(generatedRoute);

        long routeId = generatedRoute.getAiRouteId();
        if (generatedRoute.getStops() != null && !generatedRoute.getStops().isEmpty()) {
            
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	LocalDate startDate = LocalDate.parse(preferences.getStartDate(), formatter);
        	
        	
        	for (RouteStopDTO stop : generatedRoute.getStops()) {
                stop.setAiRouteId(routeId); // 부모 ID 설정
                aimapper.insertAiRouteStop(stop);
                
                if ("헬스케어".equals(preferences.getTravelStyle())) {
                	healthCareService.saveHealthCareLog(
                			longUserId, 
                			userWeight, 
                			stop, 
                			startDate
                	);
                }
            }
        }
        
        System.out.println("[AiService] AI 루트 생성 및 저장 완료. ID: " + routeId);
        return generatedRoute;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 특정 AI 여행 경로 ID에 해당하는 경로의 기본 정보와 모든 경유지 목록을 조회합니다.
     * </p>
     * @param routeId 조회할 AI 여행 경로의 고유 ID
     * @return AI 여행 경로의 전체 정보를 담은 {@link RouteDTO} 객체
     */
    @Override
    public RouteDTO getAiRouteById(long routeId) {
        RouteDTO route = aimapper.findRouteById(routeId);
        if (route != null) {
            List<RouteStopDTO> stops = aimapper.findStopsByRouteId(routeId);
            route.setStops(stops);
        }
        return route;
    }
}


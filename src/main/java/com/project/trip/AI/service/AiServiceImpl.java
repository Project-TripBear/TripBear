package com.project.trip.AI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.AI.mapper.AiMapper;
import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.RouteStopDTO;

@Service
public class AiServiceImpl implements AiService {

	@Autowired
	private AiMapper aimapper;
	
	@Autowired
	private GeminiService geminiService;

	@Override
	@Transactional
public RouteDTO createAndSaveAiRoute(AiRouteRequestDTO preferences, long longUserId) { 
        
        RouteDTO generatedRoute = geminiService.generateRoute(preferences);
        
        generatedRoute.setUserId(longUserId); 
        
       generatedRoute.setAiRouteRegion(preferences.getCity());
        generatedRoute.setAiRouteStartDate(preferences.getStartDate());
        generatedRoute.setAiRouteEndDate(preferences.getEndDate());
        
        
        aimapper.insertAiRoute(generatedRoute);

        long routeId = generatedRoute.getAiRouteId();
        if (generatedRoute.getStops() != null && !generatedRoute.getStops().isEmpty()) {
            for (RouteStopDTO stop : generatedRoute.getStops()) {
                stop.setAiRouteId(routeId); // 부모 ID 설정
                aimapper.insertAiRouteStop(stop);
            }
        }
        
        System.out.println("[AiService] AI 루트 생성 및 저장 완료. ID: " + routeId);
        return generatedRoute;
    }

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


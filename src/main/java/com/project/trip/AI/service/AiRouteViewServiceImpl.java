package com.project.trip.AI.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.trip.AI.mapper.AiRouteViewMapper;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.RouteStopDTO; // ← 스탑 모델 클래스 (필요시 추가)

/**
 * {@link AiRouteViewService} 인터페이스의 구현 클래스입니다.
 * <p>
 * {@link AiRouteViewMapper}를 통해 데이터베이스에서 AI 여행 경로의 기본 정보와 경유지 목록을 조회하고,
 * 카카오 모빌리티 API를 연동하여 각 경유지 간의 이동 경로선을 가져와 경로 정보에 추가하는 역할을 합니다.
 * </p>
 */
@Service
public class AiRouteViewServiceImpl implements AiRouteViewService {

    @Autowired
    private AiRouteViewMapper mapper;

    // ✅ REST API 키는 절대 외부로 노출하지 말 것
    private static final String KAKAO_REST_API_KEY = "43d0c1f4f0c2ff0bd368637ae2fa9b7a";
    private static final String MOBILITY_URL = "https://apis-navi.kakaomobility.com/v1/directions";

    /**
     * {@inheritDoc}
     * <p>
     * 특정 AI 여행 경로의 기본 정보와 경유지 목록을 데이터베이스에서 조회합니다.
     * 또한, 각 경유지 간의 이동 경로를 카카오 모빌리티 API를 통해 조회하여
     * {@link RouteDTO} 객체에 {@code mobilityRoutes}로 추가합니다.
     * </p>
     * @param aiRouteId 조회할 AI 여행 경로의 고유 ID
     * @return AI 여행 경로의 전체 정보를 담은 {@link RouteDTO} 객체
     */
    @Override
    public RouteDTO getAiRoute(long aiRouteId) {
        // ① 기본 경로 정보 조회
        RouteDTO route = mapper.getAiRoute(aiRouteId);

        if (route != null) {
            // ② 스탑(경유지) 목록 추가
            List<RouteStopDTO> stops = mapper.getStops(aiRouteId);
            route.setStops(stops);

            // ③ Mobility API로 각 구간 경로선 추가
            List<Object> mobilityRoutes = new ArrayList<>();
            for (int i = 0; i < stops.size() - 1; i++) {
                RouteStopDTO start = stops.get(i);
                RouteStopDTO end = stops.get(i + 1);

                try {
                    String url = MOBILITY_URL + "?origin=" + start.getAiRouteLong() + "," + start.getAiRouteLat()
                            + "&destination=" + end.getAiRouteLong() + "," + end.getAiRouteLat()
                            + "&priority=TIME";

                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "KakaoAK " + KAKAO_REST_API_KEY);

                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                            new HttpEntity<>(headers), String.class);

                    if (response.getStatusCode() == HttpStatus.OK) {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode json = mapper.readTree(response.getBody());
                        mobilityRoutes.add(json);
                    }
                } catch (Exception e) {
                    System.err.println("❌ Mobility API 오류: " + e.getMessage());
                }
            }

            // ④ JSON 형태로 전체 경로선 저장 (원하면 DTO에 필드 추가)
            route.setMobilityRoutes(mobilityRoutes);
        }

        return route;
    }
}

package com.project.trip.AI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.WeatherDTO;
import com.project.trip.AI.model.gemini.GeminiApiResponse;
import com.project.trip.AI.model.gemini.GeminiApiResponse.Part;

@Service
public class GeminiServiceImpl implements GeminiService{
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Gson gson = new Gson();

	private String GEMINI_API_KEY = "";
	
	@Override
	public RouteDTO generateRoute(AiRouteRequestDTO preferences, WeatherDTO weather) {

		String prompt = createPrompt(preferences, weather);
		String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY;
		
		try {
			
			String escapedPrompt = prompt.replace("\"", "\\\"").replace("\n", "\\n");
			String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\": \"%s\"}]}]}", escapedPrompt);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			
			ResponseEntity<String> response = restTemplate.postForEntity(geminiApiUrl, entity, String.class);
			
			String jsonResponse = response.getBody();
			RouteDTO route = parseGeminiResponse(jsonResponse);
			
			if (route == null) {
		
				throw new RuntimeException("Gemini가 유효한 JSON 루트를 반환하지 않았습니다.");
		
			}
			
				route.setWeatherConsideration(weather.toPromptString());
				return route;
				
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Gemini API 호출 중 오류 발생: " + e.getMessage());
		}
		
	}


	private RouteDTO parseGeminiResponse(String jsonResponse) {
		
		try {
			GeminiApiResponse apiResponse = gson.fromJson(jsonResponse, GeminiApiResponse.class);
			
			if (apiResponse != null && apiResponse.getCandidate() != null && !apiResponse.getCandidate().isEmpty()) {
				Part part = apiResponse.getCandidate().get(0).getContent().getParts().get(0);
				String extractedJsonText = part.getText();
				extractedJsonText = extractedJsonText.trim().replace("```json", "").replace("```", "");
				System.out.println("------Gemini API 응답 (JSON 추출)------");
				System.out.println(extractedJsonText);
				System.out.println("---------------------------------------");
				
				return gson.fromJson(extractedJsonText, RouteDTO.class);
				
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("-----Gemini 응답 파싱 실패-----");
			System.err.println(jsonResponse);
		}
		return null;
		
	}
	
	private String createPrompt(AiRouteRequestDTO dto, WeatherDTO weather) {
		StringBuilder prompt = new StringBuilder();
		
		prompt.append("당신은 한국 최고의 여행 계획 전문가입니다.\n");
        prompt.append("아래 조건에 맞춰 최적의 여행 루트를 JSON 형식으로만 응답해주세요. 설명이나 다른 말은 절대 추가하지 마세요.\n");
        
        prompt.append("JSON 출력 형식: {\"aiRouteTitle\":\"...\", \"aiRouteDays\":..., ");
        prompt.append("\"stops\":[{\"aiRouteDay\":..., \"aiRouteStopOrder\":..., \"aiRouteDescription\":\"...\", \"aiRouteLat\":..., \"aiRouteLong\":..., ");
        prompt.append("\"activityCode\":\"...\", \"durationInMinutes\":..., \"transportationMode\":\"...\", ");
        prompt.append("\"restaurantCategory\":\"...\", \"walkingDistanceKm\":..., \"walkingStepsCount\":...}]}\n\n");
        
        prompt.append("### 조건 1: 날씨 정보 (중요)\n");
        prompt.append("- 여행 시작일 날씨는 다음과 같습니다: ").append(weather.toPromptString()).append("\n");
        prompt.append("- 이 날씨를 바탕으로 사용자의 실내/실외 선호도('").append(dto.getActivityType()).append("')에 맞춰 계획하세요.\n\n");
        
        prompt.append("### 조건 2: 장소 정보 규칙 (매우 중요)\n");
        prompt.append("- 각 경유지('stops'의 각 요소)에 대해 다음 규칙을 반드시 준수하세요:\n");
        prompt.append("  1. 'transportationMode': 이전 장소에서 이 장소까지의 이동수단 (WALKS, DIRECTIONS, BICYCLE 중 하나. 첫 장소는 null).\n");
        prompt.append("  2. 식사 추천: **여행일마다 점심과 저녁, 총 2개의 식당을 반드시 추천**해야 합니다. 식당도 경유지입니다.\n");
        prompt.append("  3. 'activityCode': 활동 종류를 다음 중에서만 선택하세요: VIEWING(관람), WALK_SLOW(느린걷기), WALK_NORMAL(보통걷기), WALK_FAST(빠른걷기), HIKE_LIGHT(가벼운등산), SHOPPING(쇼핑), EATING(식사).\n");
        prompt.append("  4. 'durationInMinutes': 해당 장소에서 머무는 예상 소요 시간을 분 단위 숫자로 표기하세요.\n");
        prompt.append("  5. 'restaurantCategory': 추천 장소가 식당일 경우에만 음식 종류(예: '한식', '일식')를 표기하고, 식당이 아니면 null로 표기하세요.\n\n");
        
        if ("헬스케어".equals(dto.getTravelStyle()) && dto.getPhysicalInfo() != null) {
            prompt.append("### 조건 3: 헬스케어 맞춤 계획 (가장 중요)\n");
            prompt.append("아래 프로필과 규칙에 따라 헬스케어 여행을 계획하세요.\n");
            prompt.append("- 사용자 신체: ").append(dto.getPhysicalInfo().getGender());
            prompt.append(", ").append(dto.getPhysicalInfo().getHeight()).append("cm"); // String
            prompt.append(", ").append(dto.getPhysicalInfo().getWeight()).append("kg\n"); // String
            
            prompt.append("#### 헬스케어 준수 규칙:\n");
            prompt.append("1. 식단 계획: 사용자의 음식 선호도('").append(dto.getFoodPreference()).append("')에 맞는 건강 식단을 제공하는 실제 식당을 점심과 저녁에 추천하세요.\n");
            prompt.append("   - 건강식: 샐러드, 건강 한정식 등\n");
            prompt.append("   - 균형식: 일반 백반, 샤브샤브 등\n");
            prompt.append("   - 고단백식: 장어, 소고기 요리 등\n");
            prompt.append("   - 식당 추천 시, 'restaurantCategory' 필드에 해당 식당의 종류(예: '한식', '샐러드')를 명시하세요.\n\n");
            
            prompt.append("2. 활동 규칙: 사용자의 건강 목표('").append(dto.getHealthGoal()).append("')에 맞는 'activityCode'를 중심으로 계획하세요.\n");
            prompt.append("   - (예: '가볍게 걷기' 목표 -> 'WALK_SLOW' 또는 'WALK_NORMAL' 위주)\n\n");

            prompt.append("3. 거리/걸음 수 계산 (필수): 'activityCode'가 'WALK_' 또는 'HIKE_'로 시작하는 활동에 대해, ");
            prompt.append("예상 도보 거리('walkingDistanceKm')와 예상 걸음 수('walkingStepsCount')를 반드시 숫자로 예측하여 포함하세요. (예: 2.5, 3000). ");
            prompt.append("그 외 활동('VIEWING', 'EATING' 등)은 모두 null로 표기하세요.\n\n");
        }
       
        prompt.append("### 사용자 요청 정보\n");
        prompt.append("- 여행 도시: ").append(dto.getCity()).append("\n");
        prompt.append("- 여행 기간: ").append(dto.getDuration()).append("박 ").append(Integer.parseInt(dto.getDuration())+1).append("일\n");
        prompt.append("- 여행 스타일: ").append(dto.getTravelStyle()).append("\n");
        prompt.append("- 이동 수단: ").append(dto.getTransportation()).append("\n");
        
        return prompt.toString();
	}

}

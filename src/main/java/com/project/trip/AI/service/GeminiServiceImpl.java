package com.project.trip.AI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.WeatherDTO;
import com.project.trip.AI.model.gemini.GeminiApiResponse.Content;
import com.project.trip.AI.model.gemini.GeminiApiResponse.Part;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
public class GeminiServiceImpl implements GeminiService {

    @Autowired
    private RestTemplate restTemplate;

    private final Gson gson = new Gson();

    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class GeminiRequest {
        private List<Content> contents;
    }

    @Override
    public RouteDTO generateRoute(AiRouteRequestDTO dto, WeatherDTO weather) {

        String prompt = createPrompt(dto, weather);

        System.out.println("======== Gemini Prompt ========");
        System.out.println(prompt);
        System.out.println("================================");
        System.out.println("[DEBUG] GEMINI_API_KEY 원본: [" + GEMINI_API_KEY + "]");

        if (GEMINI_API_KEY == null || GEMINI_API_KEY.trim().isEmpty()) {
            throw new IllegalStateException("GEMINI_API_KEY가 null 또는 빈 값입니다. application.properties에 gemini.api.key 확인.");
        }

        String trimmedKey = GEMINI_API_KEY.trim();
        System.out.println("[DEBUG] GEMINI_API_KEY trim 적용: [" + trimmedKey + "]");

        String envKey = System.getenv("GEMINI_API_KEY");
        String propKey = System.getProperty("GEMINI_API_KEY");
        
        System.out.println("[DEBUG] GEMINI_API_KEY (env): [" + envKey + "]");
        System.out.println("[DEBUG] GEMINI_API_KEY (system property): [" + propKey + "]");

        String geminiApiUrl =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                        + trimmedKey;
        System.out.println("[DEBUG] 최종 Gemini API URL: " + geminiApiUrl);

        try {
            Part part = new Part();
            part.setText(prompt);

            Content content = new Content();
            content.setParts(List.of(part));

            GeminiRequest request = new GeminiRequest(List.of(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(geminiApiUrl, entity, String.class);

            String jsonResponse = response.getBody();

            System.out.println("======== Gemini Raw Response ========");
            System.out.println(jsonResponse);
            System.out.println("=====================================");

            RouteDTO route = parseGeminiResponse(jsonResponse);

            if (route == null) {
                throw new RuntimeException("Gemini가 올바른 JSON을 반환하지 않았습니다.");
            }

            // 날씨 설명 세팅
            route.setWeatherConsideration(weather.toPromptString());
            return route;

        } catch (HttpClientErrorException e) {
            System.out.println("-------- Gemini 4xx 응답 바디 ----------");
            System.out.println(e.getResponseBodyAsString());
            System.out.println("--------------------------------------");
            throw new RuntimeException("Gemini API 호출 중 오류 발생: " + e.getStatusCode(), e);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gemini API 호출 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * Gemini 응답 JSON을 직접 파싱해서
     * candidates[0].content.parts[0].text 안의 JSON만 뽑아서 RouteDTO로 변환
     */
    private RouteDTO parseGeminiResponse(String jsonResponse) {
        try {
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                System.err.println("Gemini 응답이 비어 있습니다.");
                return null;
            }

            JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();

            // candidates 배열 꺼내기
            JsonArray candidates = root.getAsJsonArray("candidates");
            if (candidates == null || candidates.size() == 0) {
                System.err.println("candidates가 비어있습니다.");
                return null;
            }

            JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
            JsonObject content = firstCandidate.getAsJsonObject("content");
            if (content == null) {
                System.err.println("content가 없습니다.");
                return null;
            }

            JsonArray parts = content.getAsJsonArray("parts");
            if (parts == null || parts.size() == 0) {
                System.err.println("parts가 비어있습니다.");
                return null;
            }

            JsonElement firstPart = parts.get(0);
            if (!firstPart.isJsonObject() ||
                !firstPart.getAsJsonObject().has("text")) {
                System.err.println("parts[0].text가 존재하지 않습니다.");
                return null;
            }

            String text = firstPart.getAsJsonObject().get("text").getAsString();
            System.out.println("------ Gemini content.parts[0].text ------");
            System.out.println(text);
            System.out.println("------------------------------------------");

            // 혹시 코드블록이 섞여 있으면 제거
            String extracted = text
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            // 순수 JSON 블록만 추출: 첫 '{' ~ 마지막 '}'
            int start = extracted.indexOf("{");
            int end = extracted.lastIndexOf("}");

            if (start == -1 || end == -1 || start > end) {
                System.err.println("텍스트 안에서 JSON 중괄호 영역을 찾지 못했습니다.");
                return null;
            }

            String pureJson = extracted.substring(start, end + 1).trim();

            System.out.println("------ Gemini API 응답 (JSON 순수 추출) ------");
            System.out.println(pureJson);
            System.out.println("------------------------------------------------");

            // 여기서 실제 RouteDTO로 변환
            return gson.fromJson(pureJson, RouteDTO.class);

        } catch (Exception e) {
            System.err.println("----- Gemini 응답 파싱 실패 -----");
            e.printStackTrace();
            System.err.println(jsonResponse);
            return null;
        }
    }

    // 질문 카드 + DTO 반영된 프롬프트 생성
    private String createPrompt(AiRouteRequestDTO dto, WeatherDTO weather) {
        StringBuilder prompt = new StringBuilder();

        // 0. 역할 & 출력 형식 고정
        prompt.append("당신은 한국 최고의 여행 루트 생성 AI입니다.\n");
        prompt.append("아래 조건을 기반으로 여행 루트를 JSON ONLY 형식으로 반환하세요.\n");
        prompt.append("설명, 텍스트, 말머리, 마크다운, 코드블록, ```json 등은 절대 포함하지 마세요.\n\n");

        // 1. JSON 스키마 (RouteDTO + RouteStopDTO 에 맞춤)
        prompt.append("JSON 형식 예시는 다음과 같습니다:\n");
        prompt.append("{\n");
        prompt.append("  \"aiRouteTitle\": \"...\",\n");
        prompt.append("  \"aiRouteRegion\": \"...\",\n");
        prompt.append("  \"aiRouteDays\": 2,\n");
        prompt.append("  \"weatherConsideration\": \"...\",\n");
        prompt.append("  \"stops\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"aiRouteDay\": 1,\n");
        prompt.append("      \"aiRouteStopOrder\": 1,\n");
        prompt.append("      \"aiRouteDescription\": \"...\",\n");
        prompt.append("      \"aiRouteLat\": 35.158,\n");
        prompt.append("      \"aiRouteLong\": 129.160,\n");
        prompt.append("      \"activityCode\": \"VIEWING\",\n");
        prompt.append("      \"durationInMinutes\": 90,\n");
        prompt.append("      \"transportationMode\": \"DIRECTIONS\",\n");
        prompt.append("      \"restaurantCategory\": null,\n");
        prompt.append("      \"walkingDistanceKm\": null,\n");
        prompt.append("      \"walkingStepsCount\": null,\n");
        prompt.append("      \"healthcareCaloriesBurned\": null\n");
        prompt.append("    }\n");
        prompt.append("  ]\n");
        prompt.append("}\n\n");

        // 2. 날씨 정보 + 실내/실외
        prompt.append("### 조건 1: 날씨 고려 (중요)\n");
        prompt.append("- 여행 날짜: ").append(dto.getStartDate()).append(" ~ ").append(dto.getEndDate()).append("\n");
        prompt.append("- 날씨 설명: ").append(weather.toPromptString()).append("\n");
        prompt.append("- 사용자의 실내/실외 선호도: '").append(dto.getActivityType()).append("'\n");
        prompt.append("- 날씨와 실내/실외 선호를 고려하여, 실내 활동 또는 실외 활동 비율을 조정하세요.\n");
        prompt.append("- 전반적인 날씨 반영 내용은 weatherConsideration 필드에 한국어 문장으로 요약하세요.\n\n");

        // 3. 장소/스탑 공통 규칙
        prompt.append("### 조건 2: 장소 정보 규칙 (매우 중요)\n");
        prompt.append("- stops 배열의 각 원소는 하나의 실제 장소(명소, 카페, 식당 등)입니다.\n");
        prompt.append("- 각 필드의 규칙은 다음과 같습니다.\n");
        prompt.append("  1. aiRouteDay: 여행 며칠차인지 (1부터 시작하는 정수).\n");
        prompt.append("  2. aiRouteStopOrder: 해당 날짜 내 방문 순서 (1부터 시작하는 정수).\n");
        prompt.append("  3. aiRouteDescription: **반드시 실제 존재하는 장소명 또는 식당명만 사용.**\n");
        prompt.append("     절대 '광안리 맛집', '제주 카페거리', '서면 핫플', '부산 전망좋은 카페' 등과 같은 키워드형 장소명을 쓰지 말 것.\n");
        prompt.append("     반드시 실제 상호명(예: '오조해녀의집', '연돈', '명진전복', '우진해장국') 또는 실재 명소명(예: '사려니숲길', '만장굴')만 사용.\n");
        prompt.append("  4. aiRouteLat / aiRouteLong: 반드시 소수점 6자리로 출력.\n");
        prompt.append("     - 예: 35.123456 / 129.123456 형식\n");
        prompt.append("     - 소수점 5자리 이하, 7자리 이상 절대 금지\n");
        prompt.append("     - 항상 6자리로 '0'을 포함해 패딩해서 출력\n");
        prompt.append("  5. activityCode: VIEWING, WALK_SLOW, WALK_NORMAL, WALK_FAST, HIKE_LIGHT, SHOPPING, EATING 중 하나.\n");
        prompt.append("  6. durationInMinutes: 해당 장소에서 머무는 시간 (분 단위 정수).\n");
        prompt.append("  7. transportationMode: WALK, CAR, BICYCLE 중 하나. 첫 장소는 null.\n");
        prompt.append("  8. restaurantCategory: 식당일 경우 음식 종류(예: '한식', '일식', '샐러드'), 아니면 null.\n");
        prompt.append("  9. 모든 여행일마다 점심/저녁 = EATING 2개 반드시 포함.\n\n");

        // 4. 예산
        prompt.append("### 조건 3: 예산(budget) 반영\n");
        prompt.append("- 사용자의 예산: '").append(dto.getBudget()).append("'\n");
        prompt.append("- 예산에 따라 식당/체험의 고급도와 유료/무료 비율을 조절하세요.\n\n");

        // 5. 선호 지역
        prompt.append("### 조건 4: 선호 지역(preferredArea) 반영\n");
        prompt.append("- 사용자의 선호 지역: '").append(dto.getPreferredArea()).append("'\n\n");

        // 6. 활동 시간대
        prompt.append("### 조건 5: 활동 시간대(activityTime) 반영\n");
        prompt.append("- 사용자의 활동 시간대: '").append(dto.getActivityTime()).append("'\n\n");

        // 7. 동행
        prompt.append("### 조건 6: 동행(companion) 반영\n");
        prompt.append("- 사용자의 동행: '").append(dto.getCompanion()).append("'\n\n");

        // 8. 여행 스타일
        prompt.append("### 조건 7: 여행 스타일(travelStyle) 반영\n");
        prompt.append("- 사용자의 여행 스타일: '").append(dto.getTravelStyle()).append("'\n\n");

        // 9. 헬스케어일 경우 추가 조건 (필요하면 여기 다시 채워도 됨)
        if ("헬스케어".equals(dto.getTravelStyle()) && dto.getPhysicalInfo() != null) {
            prompt.append("### 조건 8: 헬스케어 맞춤 계획\n");
            prompt.append("- 성별: ").append(dto.getPhysicalInfo().getGender()).append("\n");
            prompt.append("- 키: ").append(dto.getPhysicalInfo().getHeight()).append("cm\n");
            prompt.append("- 몸무게: ").append(dto.getPhysicalInfo().getWeight()).append("kg\n");
            prompt.append("- 건강 목표: ").append(dto.getHealthGoal()).append("\n");
            prompt.append("- 음식 선호: ").append(dto.getFoodPreference()).append("\n");
            prompt.append("- 특이 건강 상태: ").append(dto.getHealthCondition()).append("\n\n");

            prompt.append("#### 헬스케어 세부 규칙:\n");
            prompt.append("- WALK_, HIKE_ 활동에는 walkingDistanceKm, walkingStepsCount, healthcareCaloriesBurned를 숫자로 채우고,\n");
            prompt.append("- 나머지 활동에는 이 세 필드를 모두 null로 설정하세요.\n\n");
        }

        // 10. 최종 사용자 입력 정보 요약
        prompt.append("### 사용자 요청 요약\n");
        prompt.append("- 도시(city): ").append(dto.getCity()).append("\n");
        prompt.append("- 여행 기간(duration): ").append(dto.getDuration()).append("\n");
        prompt.append("- 여행 시작일(startDate): ").append(dto.getStartDate()).append("\n");
        prompt.append("- 여행 종료일(endDate): ").append(dto.getEndDate()).append("\n");
        prompt.append("- 여행 스타일(travelStyle): ").append(dto.getTravelStyle()).append("\n");
        prompt.append("- 활동 시간대(activityTime): ").append(dto.getActivityTime()).append("\n");
        prompt.append("- 예산(budget): ").append(dto.getBudget()).append("\n");
        prompt.append("- 선호 지역(preferredArea): ").append(dto.getPreferredArea()).append("\n");
        prompt.append("- 이동 수단(transportation): ").append(dto.getTransportation()).append("\n");
        prompt.append("- 실내/실외(activityType): ").append(dto.getActivityType()).append("\n");
        prompt.append("- 동행(companion): ").append(dto.getCompanion()).append("\n");

        return prompt.toString();
    }
}

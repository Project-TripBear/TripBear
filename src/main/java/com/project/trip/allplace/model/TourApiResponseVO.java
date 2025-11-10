package com.project.trip.allplace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiResponseVO {

    // 1. JSON의 "response" 키
    private Response response;

    // --- 중첩된 JSON 구조를 그대로 본따서 static 내부 클래스를 만듭니다 ---
    
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        // 2. "body"
        private Body body;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        // 3. "items"
        private Items items;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        // 4. "item"
        // [중요] 상세정보조회 API는 item이 배열(List)이 아닌 단일 객체입니다.
        private TourItemVO item;
    }
}
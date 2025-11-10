package com.project.trip.allplace.model;

// [추가] List를 사용하기 위해 import
import java.util.List; 

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiResponseVO {

    private Response response;
    
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private Body body;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private Items items;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        
        // --- [핵심 수정] ---
        // 단일 객체(TourItemVO)에서 리스트(List<TourItemVO>)로 변경
        // private TourItemVO item; // (기존)
        private List<TourItemVO> item; // (수정)
        // --- [여기까지] ---
    }
}
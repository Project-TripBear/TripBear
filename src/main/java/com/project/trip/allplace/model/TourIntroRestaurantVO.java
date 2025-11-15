package com.project.trip.allplace.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TourAPI '소개정보(detailIntro2)' 중 
 * '음식점(contentTypeId=39)'의 응답을 받는 DTO
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourIntroRestaurantVO {

    // (json 래퍼 구조)
    private Response response;
    
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response { private Body body; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body { private Items items; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items { private List<Item> item; } 

    /**
     * API의 음식점 상세 필드
     */
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        
        @JsonProperty("contentid")
        private String contentId;
        
        @JsonProperty("contenttypeid")
        private String contentTypeId;
        
        @JsonProperty("firstmenu")
        private String firstMenu; // (대표 메뉴)

        @JsonProperty("treatmenu")
        private String treatMenu; // (취급 메뉴)

        @JsonProperty("opentimefood")
        private String openingHours; // (영업 시간)

        @JsonProperty("restdatefood")
        private String restDay; // (쉬는 날)

        @JsonProperty("infocenterfood")
        private String contactInfo; // (전화번호)
        
        @JsonProperty("parkingfood")
        private String parkingInfo; // (주차)
        
        @JsonProperty("smoking")
        private String smoking; // (금연/흡연)
        
        @JsonProperty("kidsfacility")
        private String kidsFacility; // (놀이방)
    }
}
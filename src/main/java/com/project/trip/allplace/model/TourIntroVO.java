package com.project.trip.allplace.model;

import java.util.List; // (List 사용을 위해 추가)
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourIntroVO {

    // (json 래퍼 구조는 TourApiResponseVO와 동일)
    private Response response;
    
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response { private Body body; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body { private Items items; }

    // [중요] 소개정보 API는 item이 리스트(배열)일 수 있습니다.
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items { private List<Item> item; } 
    
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        
        @JsonProperty("contentid")
        private String contentId;
        
        @JsonProperty("contenttypeid")
        private String contentTypeId;

        @JsonProperty("usetime")
        private String openingHours; // (운영시간)

        @JsonProperty("restdate")
        private String restDay; // (쉬는날)

        @JsonProperty("parking")
        private String parkingInfo; // (주차정보)
        
        @JsonProperty("usetimefee")
        private String admissionFee; // (입장료)
        
        @JsonProperty("infocenter")
        private String contactInfo; // (연락처)
    }
}
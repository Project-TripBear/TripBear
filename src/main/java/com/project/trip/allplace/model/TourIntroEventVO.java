package com.project.trip.allplace.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TourAPI '소개정보(detailIntro1)' 중 
 * '축제/행사(contentTypeId=15)'의 응답을 받는 DTO
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourIntroEventVO {

    // (json 래퍼 구조)
    private Response response;
    
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response { private Body body; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body { private Items items; }

    // (소개정보 API는 item이 List<Item> 구조)
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items { private List<Item> item; } 

    /**
     * API의 축제/행사 상세 필드
     * (tblEvent의 event_start, event_end 등과 매핑됨)
     */
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        
        @JsonProperty("contentid")
        private String contentId;
        
        @JsonProperty("contenttypeid")
        private String contentTypeId;

        @JsonProperty("eventstartdate")
        private String eventStart; // (행사시작일)

        @JsonProperty("eventenddate")
        private String eventEnd;   // (행사종료일)

        @JsonProperty("eventplace")
        private String eventInfo;  // (행사장소 -> tblEvent.event_info)

        @JsonProperty("eventhomepage")
        private String eventLink;  // (홈페이지 -> tblEvent.event_link)
    }
}
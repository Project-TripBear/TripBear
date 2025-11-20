package com.project.trip.allplace.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TourAPI '소개 정보 조회(detailIntro)' 중 축제/행사(contentTypeId=15) 타입의
 * 응답을 매핑하기 위한 데이터 객체입니다.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourIntroEventVO {

    private Response response;

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response { private Body body; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body { private Items items; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items { private List<Item> item; }

    /**
     * 축제/행사 소개 정보의 상세 내용을 담는 객체입니다.
     */
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        /**
         * 콘텐츠 ID
         */
        @JsonProperty("contentid")
        private String contentId;

        /**
         * 콘텐츠 타입 ID (15)
         */
        @JsonProperty("contenttypeid")
        private String contentTypeId;

        /**
         * 행사 시작일 (yyyyMMdd)
         */
        @JsonProperty("eventstartdate")
        private String eventStart;

        /**
         * 행사 종료일 (yyyyMMdd)
         */
        @JsonProperty("eventenddate")
        private String eventEnd;

        /**
         * 행사장소 정보 (tblEvent.event_info에 매핑될 수 있음)
         */
        @JsonProperty("eventplace")
        private String eventInfo;

        /**
         * 행사 홈페이지 URL (tblEvent.event_link에 매핑될 수 있음)
         */
        @JsonProperty("eventhomepage")
        private String eventLink;
    }
}
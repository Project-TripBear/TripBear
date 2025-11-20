package com.project.trip.allplace.model;

import java.util.List; // (List 사용을 위해 추가)
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TourAPI '소개 정보 조회(detailIntro)' 중 관광지(contentTypeId=12) 타입의
 * 응답을 매핑하기 위한 데이터 객체입니다.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourIntroVO {

    private Response response;

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response { private Body body; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body { private Items items; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items { private List<Item> item; }

    /**
     * 관광지 소개 정보의 상세 내용을 담는 객체입니다.
     */
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        /**
         * 콘텐츠 ID
         */
        @JsonProperty("contentid")
        private String contentId;

        /**
         * 콘텐츠 타입 ID (12)
         */
        @JsonProperty("contenttypeid")
        private String contentTypeId;

        /**
         * 이용 시간
         */
        @JsonProperty("usetime")
        private String openingHours;

        /**
         * 쉬는 날
         */
        @JsonProperty("restdate")
        private String restDay;

        /**
         * 주차 시설 정보
         */
        @JsonProperty("parking")
        private String parkingInfo;

        /**
         * 입장료 및 이용 요금
         */
        @JsonProperty("usetimefee")
        private String admissionFee;

        /**
         * 문의 및 안내 전화번호
         */
        @JsonProperty("infocenter")
        private String contactInfo;
    }
}
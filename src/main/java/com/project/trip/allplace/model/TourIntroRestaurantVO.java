package com.project.trip.allplace.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TourAPI '소개 정보 조회(detailIntro)' 중 음식점(contentTypeId=39) 타입의
 * 응답을 매핑하기 위한 데이터 객체입니다.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourIntroRestaurantVO {

    private Response response;

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response { private Body body; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body { private Items items; }

    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items { private List<Item> item; }

    /**
     * 음식점 소개 정보의 상세 내용을 담는 객체입니다.
     */
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        /**
         * 콘텐츠 ID
         */
        @JsonProperty("contentid")
        private String contentId;

        /**
         * 콘텐츠 타입 ID (39)
         */
        @JsonProperty("contenttypeid")
        private String contentTypeId;

        /**
         * 대표 메뉴
         */
        @JsonProperty("firstmenu")
        private String firstMenu;

        /**
         * 취급 메뉴
         */
        @JsonProperty("treatmenu")
        private String treatMenu;

        /**
         * 영업 시간
         */
        @JsonProperty("opentimefood")
        private String openingHours;

        /**
         * 쉬는 날
         */
        @JsonProperty("restdatefood")
        private String restDay;

        /**
         * 문의 및 안내 전화번호
         */
        @JsonProperty("infocenterfood")
        private String contactInfo;

        /**
         * 주차 시설 정보
         */
        @JsonProperty("parkingfood")
        private String parkingInfo;

        /**
         * 금연/흡연 가능 정보
         */
        @JsonProperty("smoking")
        private String smoking;

        /**
         * 어린이 놀이방 여부
         */
        @JsonProperty("kidsfacility")
        private String kidsFacility;
    }
}
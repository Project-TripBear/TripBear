package com.project.trip.allplace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Tour API 응답의 개별 'item' 객체(장소 1개) 정보를 매핑하기 위한 값 객체(VO)입니다.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourItemVO {

    /**
     * 콘텐츠 ID
     */
    @JsonProperty("contentid")
    private String contentId;

    /**
     * 콘텐츠 타입 ID
     */
    @JsonProperty("contenttypeid")
    private String contentTypeId;

    /**
     * 지역 코드
     */
    @JsonProperty("areacode")
    private String areaCode;

    /**
     * 장소 이름 (제목)
     */
    @JsonProperty("title")
    private String title;

    /**
     * 주소
     */
    @JsonProperty("addr1")
    private String address;

    /**
     * X좌표 (경도)
     */
    @JsonProperty("mapx")
    private String longitude;

    /**
     * Y좌표 (위도)
     */
    @JsonProperty("mapy")
    private String latitude;

    /**
     * 대표 이미지 URL (원본)
     */
    @JsonProperty("firstimage")
    private String firstImage;

    /**
     * 장소 개요 설명
     */
    @JsonProperty("overview")
    private String overview;
}
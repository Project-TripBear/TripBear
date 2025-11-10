package com.project.trip.allplace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 	TourAPI 응답 JSON의 "item" 객체 (장소 1개 정보)
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // API가 주는 수많은 필드 중, 여기서 정의한 것 외에는 무시
public class TourItemVO {

    @JsonProperty("contentid")
    private String contentId; 

    @JsonProperty("title")
    private String title; 

    @JsonProperty("addr1")
    private String address; 

    // API는 좌표를 String으로 줌
    @JsonProperty("mapx")
    private String longitude;

    @JsonProperty("mapy")
    private String latitude;

    @JsonProperty("firstimage")
    private String firstImage;
}
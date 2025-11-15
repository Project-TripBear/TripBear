package com.project.trip.allplace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourItemVO {

    @JsonProperty("contentid")
    private String contentId; 

    @JsonProperty("contenttypeid")
    private String contentTypeId; 
    
    @JsonProperty("areacode")
    private String areaCode;


    @JsonProperty("title")
    private String title; 

    @JsonProperty("addr1")
    private String address; 

    @JsonProperty("mapx")
    private String longitude;

    @JsonProperty("mapy")
    private String latitude;

    @JsonProperty("firstimage")
    private String firstImage;
    
    @JsonProperty("overview")
    private String overview;
}
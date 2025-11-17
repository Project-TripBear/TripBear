package com.project.trip.allplace.model;

import lombok.Data;

/**
 * 기상청 초단기예보 API의 원시 데이터를 가공하여,
 * 프론트엔드에서 사용하기 쉬운 형태로 변환한 날씨 정보를 담는 값 객체(VO)입니다.
 */
@Data
public class WeatherVO {

    /**
     * 예보 발표일자 (예: "20251112")
     */
    private String baseDate;

    /**
     * 예보 발표시각 (예: "0930")
     */
    private String baseTime;

    /**
     * 하늘 상태 (예: "맑음", "구름많음", "흐림")
     */
    private String skyStatus;

    /**
     * 현재 기온 (섭씨, 예: "20")
     */
    private String temperature;

    /**
     * 1시간 강수량 (예: "강수없음", "1mm 미만", "5mm")
     */
    private String rainAmount;

    /**
     * 강수 형태 (예: "없음", "비", "비/눈", "눈")
     */
    private String rainType;

    /**
     * 습도 (%, 예: "60")
     */
    private String humidity;

    /**
     * 기상청 API 원본 강수형태 코드 (0:없음, 1:비, 2:비/눈, 3:눈, 5:빗방울, 6:빗방울/눈날림, 7:눈날림)
     */
    private String ptyCode;

    /**
     * 기상청 API 원본 하늘상태 코드 (1:맑음, 3:구름많음, 4:흐림)
     */
    private String skyCode;
}
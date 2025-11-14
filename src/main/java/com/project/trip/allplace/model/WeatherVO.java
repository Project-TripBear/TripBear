package com.project.trip.allplace.model;

import lombok.Data;

/**
 * 기상청 API 응답을 가공하여 프론트엔드에 전달하기 위한 VO
 * (초단기예보 API 기준으로 수정됨)
 */
@Data
public class WeatherVO {

    private String baseDate; // "20251112"
    private String baseTime; // "0930" (HH30 형태)

    // --- 프론트엔드 표시용 ---
    
    /**
     * 하늘 상태 (예: "맑음", "구름많음", "비", "눈")
     */
    private String skyStatus; 
    
    /**
     * 현재 기온 (예: "20")
     */
    private String temperature; 
    
    /**
     * [수정] 1시간 강수량 (예: "강수없음", "1mm 미만", "5mm")
     */
    private String rainAmount;
    
    /**
     * 강수 형태 (예: "비", "눈", "없음")
     */
    private String rainType;
    
    /**
     * 습도 (예: "60")
     */
    private String humidity;

    // (기상청 API 원본 코드)
    private String ptyCode; // (강수형태: 0, 1, 2, 3, 5, 6, 7)
    private String skyCode; // (하늘상태: 1, 3, 4)
}
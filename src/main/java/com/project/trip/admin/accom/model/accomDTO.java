// 파일 경로: com.project.trip.admin.model.accomDTO.java
package com.project.trip.admin.accom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class accomDTO {
    // DB 테이블의 컬럼들과 매칭됩니다.
    private int roomId;
    private String accomName;
    private String accomType;
    private String roomName;
    private int pricePerNight;
    private int capacity;
    
    // ★★★ [추가] tblAccomRoom의 room_image_url 컬럼 ★★★
    private String roomImage; 
    
    // 예약 여부를 저장할 변수 (DB에는 없는 변수)
    private boolean isReserved;
}
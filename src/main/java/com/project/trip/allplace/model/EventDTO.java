package com.project.trip.allplace.model;

import java.util.Date; // Date 타입을 위해 import
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tblEvent (행사/축제) 테이블과 매핑되는 DTO
 */
@Data
@NoArgsConstructor
public class EventDTO {

    // (ERD 'tblEvent' 컬럼 기준)
    
    // 1. PK (tblEvent)
    private long eventId; 
    
    // 2. FK (tblPlace 참조)
    private long placeId; 
    
    // 3. FK (tblEventTheme 참조)
    private long eventThemeId; 

    private String eventName;
    private String eventStart; // (행사시작일)
    private String eventEnd;   // (행사종료일)
    private String eventInfo;  // (행사정보)
    private String eventLink;  // (관련링크)
}
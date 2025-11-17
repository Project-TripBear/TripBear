package com.project.trip.allplace.model;

import java.util.Date; // Date 타입을 위해 import
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 축제 또는 행사 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblEvent` 테이블과 매핑됩니다.
 */
@Data
@NoArgsConstructor
public class EventDTO {

    /**
     * 행사의 고유 식별자 (PK)
     */
    private long eventId;

    /**
     * 행사가 열리는 장소의 고유 식별자 (FK, tblPlace 참조)
     */
    private long placeId;

    /**
     * 행사 테마의 고유 식별자 (FK, tblEventTheme 참조)
     */
    private long eventThemeId;

    /**
     * 행사 이름
     */
    private String eventName;

    /**
     * 행사 시작일 (yyyyMMdd 형식의 문자열)
     */
    private String eventStart;

    /**
     * 행사 종료일 (yyyyMMdd 형식의 문자열)
     */
    private String eventEnd;

    /**
     * 행사 관련 추가 정보
     */
    private String eventInfo;

    /**
     * 행사 관련 외부 링크 URL
     */
    private String eventLink;
}
package com.project.trip.allplace.model;

import java.util.Date; // (regdate를 위해 import)
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 해시태그로 사용되는 키워드 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblKeyword` 테이블과 매핑됩니다.
 */
@Data
@NoArgsConstructor
public class KeywordDTO {
    /**
     * 키워드의 고유 식별자 (PK)
     */
    private long keywordId;

    /**
     * 키워드(해시태그)의 이름
     */
    private String keywordName;

    /**
     * 키워드가 등록된 날짜
     */
    private Date keywordRegdate;
}
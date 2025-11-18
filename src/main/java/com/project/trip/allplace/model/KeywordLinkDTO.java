package com.project.trip.allplace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 장소와 키워드(해시태그) 간의 다대다 관계를 나타내는 연결 정보 DTO입니다.
 * `tblKeywordLink` 테이블과 매핑됩니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordLinkDTO {

    /**
     * 연결된 장소의 고유 식별자 (FK, tblPlace 참조)
     */
    private long placeId;

    /**
     * 연결된 키워드의 고유 식별자 (FK, tblKeyword 참조)
     */
    private long keywordId;

}
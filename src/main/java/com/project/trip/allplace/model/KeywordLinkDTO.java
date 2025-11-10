package com.project.trip.allplace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class KeywordLinkDTO {

    // (ERD의 keyword_link_id는 시퀀스가 넣을 PK이므로 DTO에는 없어도 됨)

    private long placeId;    // (FK: tblPlace 참조)
    private long keywordId;  // (FK: tblKeyword 참조)
}
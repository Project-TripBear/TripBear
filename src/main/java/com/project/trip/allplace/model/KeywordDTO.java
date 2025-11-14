package com.project.trip.allplace.model;

import java.util.Date; // (regdate를 위해 import)
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeywordDTO {
    private long keywordId;
    private String keywordName;
    private Date keywordRegdate; // (ERD의 keyword_regdate와 매핑)
}
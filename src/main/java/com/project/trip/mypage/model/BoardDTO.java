package com.project.trip.mypage.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDTO {
	
    private String seq;        // bseq
    private String boradTitle; // activity_type
    private String boradCode;  // activity_code
    private String subject;    // title
    private String regdate;    // regdate
    private String commentcontent;  // 댓글 내용 추가

	}

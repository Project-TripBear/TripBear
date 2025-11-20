package com.project.trip.mypage.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 마이페이지에서 게시글, 댓글, 좋아요, 스크랩 활동 내역을 조회하기 위한 데이터 전송 객체(DTO)입니다.
 * 다양한 활동 내역의 공통 정보를 포함합니다.
 */
@Getter
@Setter
@ToString
public class BoardDTO {
	
    /**
     * 게시글 또는 댓글의 고유 번호 (bseq)
     */
    private String seq;        // bseq
    /**
     * 활동 유형 (activity_type)
     */
    private String boradTitle; // activity_type
    /**
     * 활동 코드 (activity_code)
     */
    private String boradCode;  // activity_code
    /**
     * 제목 (title)
     */
    private String subject;    // title
    /**
     * 등록일 (regdate)
     */
    private String regdate;    // regdate
    /**
     * 댓글 내용 (댓글 활동 내역 조회 시 사용)
     */
    private String commentcontent;  // 댓글 내용 추가

	}

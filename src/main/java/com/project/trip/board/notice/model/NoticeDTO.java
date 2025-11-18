package com.project.trip.board.notice.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 공지사항 게시글 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblNotice` 테이블과 매핑됩니다.
 */
@Getter
@Setter
@ToString
public class NoticeDTO {
	
	/**
	 * 공지사항 게시글의 고유 식별자 (PK)
	 */
	private Long noticePostId;
	/**
	 * 공지사항을 작성한 관리자의 고유 식별자 (FK)
	 */
	private Long adminId;
	/**
	 * 공지사항 제목
	 */
	private String noticeHeader;
	/**
	 * 공지사항 내용
	 */
	private String noticeContent;
	/**
	 * 공지사항 조회수
	 */
	private Long noticeViewCount;
	/**
	 * 공지사항 등록일
	 */
	private Date noticeRegdate;

}

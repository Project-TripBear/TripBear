package com.project.trip.board.hotdeal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 핫딜 게시글의 댓글 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblHotDealComment` 테이블과 매핑됩니다.
 */
@Getter
@Setter
@ToString
public class HotDealCommentDTO {
	/**
	 * 댓글의 고유 식별자 (PK)
	 */
	private Long seq;
	/**
	 * 댓글 내용
	 */
	private String content;
	/**
	 * 댓글 작성자의 아이디
	 */
	private String id;
	/**
	 * 댓글 등록일
	 */
	private String regdate;
	/**
	 * 댓글이 속한 핫딜 게시글의 고유 식별자 (FK)
	 */
	private String bseq;
	/**
	 * 댓글 작성자의 사용자 고유 번호 (FK)
	 */
	private String useq;
	/**
	 * 댓글 작성자의 이름 (화면 표시용)
	 */
	private String name;
}


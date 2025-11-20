package com.project.trip.board.hotdeal.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 핫딜 게시글 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblHotDealBoard` 테이블과 매핑되며, 화면 표시를 위한 추가 정보도 포함합니다.
 */
@Getter
@Setter
@ToString
public class HotDealDTO {
	/**
	 * 게시글의 고유 식별자 (PK)
	 */
	private String seq;
	/**
	 * 게시글 제목
	 */
	private String subject;
	/**
	 * 게시글 내용
	 */
	private String content;
	/**
	 * 게시글 작성자의 아이디
	 */
	private String id;
	/**
	 * 게시글 등록일
	 */
	private String regdate;
	/**
	 * 게시글 조회수
	 */
	private String readcount;
	/**
	 * 핫딜 카테고리
	 */
	private String category;
	/**
	 * 핫딜 상태 (예: "진행중", "종료")
	 */
	private String status;
	/**
	 * 상품명
	 */
	private String itemName;
	/**
	 * 가격
	 */
	private String price;
	/**
	 * 상품 URL
	 */
	private String url;
	/**
	 * 게시글 작성자의 사용자 고유 번호 (FK)
	 */
	private String useq;
	/**
	 * 게시판 제목 (화면 표시용)
	 */
	private String boradTitle;
	/**
	 * 게시판 코드 (화면 표시용)
	 */
	private String boradCode;
	/**
	 * 댓글 내용 (화면 표시용)
	 */
	private String commentcontent;
	
	/**
	 * 작성자 이름 (화면 표시용)
	 */
	private String name;
	/**
	 * 댓글 수 (화면 표시용)
	 */
	private String commentCount;
	/**
	 * 좋아요 수 (화면 표시용)
	 */
	private String likeCount;
	/**
	 * 대표 이미지 파일명 (화면 표시용)
	 */
	private String img;
}

package com.project.trip.board.hotdeal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 핫딜 게시글에 첨부된 이미지 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblHotDealImage` 테이블과 매핑됩니다.
 */
@Getter
@Setter
@ToString
public class HotDealImageDTO {
	/**
	 * 핫딜 이미지의 고유 식별자 (PK)
	 */
	private String hotdealImageId;
    /**
     * 이미지가 속한 핫딜 게시글의 고유 식별자 (FK)
     */
    private String hotdealId;
    /**
     * 이미지 파일 경로 또는 URL
     */
    private String hotdealImageUrl;
    /**
     * 핫딜 게시글 내 이미지 순서
     */
    private int hotdealImageSeq;
}

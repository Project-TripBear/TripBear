package com.project.trip.board.qna.model;

import lombok.Data;

/**
 * 게시판 페이징 처리에 필요한 정보를 담는 데이터 전송 객체(DTO)입니다.
 */
@Data
public class PagingDTO {
    /**
     * 현재 페이지 번호
     */
    private int page;
    /**
     * 전체 페이지 수
     */
    private int totalPage;
    /**
     * 페이지 블록의 시작 페이지 번호
     */
    private int startPage;
    /**
     * 페이지 블록의 끝 페이지 번호
     */
    private int endPage;
    /**
     * 이전 페이지 블록의 존재 여부
     */
    private boolean prev;
    /**
     * 다음 페이지 블록의 존재 여부
     */
    private boolean next;
}


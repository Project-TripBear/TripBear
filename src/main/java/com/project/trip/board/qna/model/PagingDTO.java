package com.project.trip.board.qna.model;

import lombok.Data;

@Data
public class PagingDTO {
    private int page;       // 현재 페이지
    private int totalPage;  // 전체 페이지 수
    private int startPage;  // 페이지 블록 시작
    private int endPage;    // 페이지 블록 끝
    private boolean prev;   // 이전 블록 존재 여부
    private boolean next;   // 다음 블록 존재 여부
}


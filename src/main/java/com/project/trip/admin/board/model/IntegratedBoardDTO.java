	package com.project.trip.admin.board.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 여러 종류의 게시판 글을 관리자 페이지에서 통합하여 표현하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 각기 다른 게시판(예: QnA, 여행 후기)의 글들을 공통된 형식으로 조회하고 관리하는 데 사용됩니다.
 * </p>
 */
@Getter
@Setter
@ToString
public class IntegratedBoardDTO {
    /**
     * 게시글의 고유 식별자
     */
    private int seq;
    /**
     * 게시판의 종류 (예: "QnA", "여행후기")
     */
    private String boardType;
    /**
     * 게시글의 제목
     */
    private String title;
    /**
     * 작성자의 닉네임
     */
    private String nickname;
    /**
     * 게시글 작성일
     */
    private Date regdate;
    /**
     * 게시글 조회수
     */
    private int viewCount;
    
    /**
     * 게시글의 대상 유형 (예: 특정 게시판, 사용자 등)
     */
    private String targetType; 
    /**
     * 게시글에 달린 댓글 수
     */
    private int commentCount;
    /**
     * 게시글이 받은 좋아요 수
     */
    private int likeCount;


}
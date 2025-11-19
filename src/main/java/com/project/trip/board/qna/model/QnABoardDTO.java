package com.project.trip.board.qna.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * QnA 게시판의 게시글 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblQuestionBoard` 테이블과 매핑되며, 화면 표시를 위한 추가 정보도 포함합니다.
 */
@Getter
@Setter
@ToString
public class QnABoardDTO {

    /**
     * 게시글의 고유 식별자 (PK)
     */
    private int question_board_id;

    /**
     * 게시글 작성자의 사용자 ID (FK)
     */
    private String user_id;

    /**
     * 게시글 제목
     */
    private String question_board_title;

    /**
     * 게시글 내용
     */
    private String question_board_content;

    /**
     * 게시글 조회수
     */
    private int question_board_view_count;

    /**
     * 게시글에 달린 댓글 수 (화면 표시용)
     */
    private int commentCount;

    /**
     * 게시글 신고 횟수
     */
    private int question_board_report_count;

    /**
     * 게시글 답변 상태 (예: "답변 대기", "답변 완료")
     */
    private String question_board_answer_status;

    /**
     * 게시글 상태 (예: "정상", "블라인드")
     */
    private String question_board_status;
    
    /**
     * 게시글 등록일
     */
    private Date question_board_regdate;

    /**
     * 게시글 최종 수정일
     */
    private Date question_board_update;
    
    /**
     * 등록일로부터 경과된 시간 (시간 단위, 화면 표시용)
     */
    private long regHourDiff;

    /**
     * 포맷된 등록일 (yyyy-MM-dd, 화면 표시용)
     */
    private String regDateFormatted;

    /**
     * QnA 카테고리 번호 (FK)
     */
    private int question_category_id;

    /**
     * QnA 카테고리 이름 (화면 표시용)
     */
    private String question_category_name;
    
    /**
     * 게시글의 총 추천수 (화면 표시용)
     */
    private int likeCount;

    /**
     * 작성자 닉네임 (화면 표시용)
     */
    private String nickname;

    /**
     * 현재 로그인한 사용자의 게시글 추천 여부 (true/false, 화면 표시용)
     */
    private boolean liked;
    
    /**
     * 게시글의 총 스크랩 수 (화면 표시용)
     */
    private int scrapCount;

    /**
     * 현재 로그인한 사용자의 게시글 스크랩 여부 (true/false, 화면 표시용)
     */
    private boolean scrapped;

    /**
     * 목록 조회 시 행 번호 (화면 표시용)
     */
    private int rownum;
}
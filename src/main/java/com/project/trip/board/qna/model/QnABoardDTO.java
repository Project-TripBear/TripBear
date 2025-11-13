package com.project.trip.board.qna.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnABoardDTO {

    // tblQuestionBoard 테이블 컬럼
    private int question_board_id;
    private String user_id;
    private String question_board_title;
    private String question_board_content;
    private int question_board_view_count;
    private int commentCount;
    private int question_board_report_count;
    private String question_board_answer_status;
    private String question_board_status;
    
    private Date question_board_regdate;
    private Date question_board_update;
    
    private long regHourDiff;         // 몇 시간 전인지
    private String regDateFormatted;  // yyyy-MM-dd

    private int question_category_id;		//QnA카테고리 번호
    private String question_category_name;	//QnA카테고리 이름

    // ★★★ [삭제] 키워드/해시태그 관련 필드 제거 ★★★
    
    // 화면 표시용 추가 데이터
    private int likeCount;
    private String nickname;   // 작성자 닉네임
    private boolean liked;     // 현재 로그인한 사용자의 추천 여부 (true/false)
    
    // 스크랩 정보
    private int scrapCount;
    private boolean scrapped;  // 현재 로그인한 사용자의 스크랩 여부 (true/false)
    private int rownum;
}
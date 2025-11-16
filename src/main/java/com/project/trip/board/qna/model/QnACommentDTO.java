// com.project.trip.board.find.model.findcommentDTO.java
package com.project.trip.board.qna.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnACommentDTO {

    // tblFindComment 테이블 컬럼
    private int question_answer_id;
    private int user_id;
    private int question_board_id;
    private String question_answer_content;
    private Date question_answer_regdate;
    
    // 화면 표시용 추가 데이터
    private String nickname; // 댓글 작성자 닉네임
}
// com.project.trip.board.find.model.findcommentDTO.java
package com.project.trip.board.find.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class findcommentDTO {

    // tblFindComment 테이블 컬럼
    private int find_comment_id;
    private int find_board_id;
    private int user_id;
    private String find_comment_content;
    private String find_comment_regdate;
    
    // 화면 표시용 추가 데이터
    private String nickname; // 댓글 작성자 닉네임
}
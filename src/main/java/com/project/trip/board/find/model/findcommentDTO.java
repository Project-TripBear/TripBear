// com.project.trip.board.find.model.findcommentDTO.java
package com.project.trip.board.find.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * '찾아주세요' 게시판의 댓글 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblFindComment` 테이블과 매핑되며, 화면 표시를 위한 추가 정보도 포함합니다.
 */
@Getter
@Setter
@ToString
public class findcommentDTO {

    /**
     * 댓글의 고유 식별자 (PK)
     */
    private int find_comment_id;

    /**
     * 댓글이 속한 게시글의 고유 식별자 (FK)
     */
    private int find_board_id;

    /**
     * 댓글 작성자의 사용자 ID (FK)
     */
    private int user_id;

    /**
     * 댓글 내용
     */
    private String find_comment_content;

    /**
     * 댓글 등록일
     */
    private String find_comment_regdate;

    /**
     * 댓글 작성자 닉네임 (화면 표시용)
     */
    private String nickname;
}
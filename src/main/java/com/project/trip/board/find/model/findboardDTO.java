// findboardDTO.java (최종 수정본)
package com.project.trip.board.find.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class findboardDTO {

    // tblFindBoard 테이블 컬럼
    private int find_board_id;
    private String user_id;
    private String find_board_title;
    private String find_board_content;
    private int find_board_view_count;
    private int find_board_report_count;
    private String find_board_report_status;
    private String find_board_regdate;
    private String find_board_update;

    // ★★★ [삭제] 키워드/해시태그 관련 필드 제거 ★★★
    
    // 화면 표시용 추가 데이터
    private String nickname;   // 작성자 닉네임
    private int commentCount;  // 댓글 수
    private int likeCount;     // 총 추천수
    private boolean liked;     // 현재 로그인한 사용자의 추천 여부 (true/false)
    
    // 스크랩 정보
    private int scrapCount;    // 총 스크랩 수
    private boolean scrapped;  // 현재 로그인한 사용자의 스크랩 여부 (true/false)
    private int rownum;
}
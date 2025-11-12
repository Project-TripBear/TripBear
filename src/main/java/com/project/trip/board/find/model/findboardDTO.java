// findboardDTO.java 파일 수정

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
    private int user_id;
    private String find_board_title;
    private String find_board_content;
    private int find_board_view_count;
    private int find_board_report_count;
    private String find_board_report_status;
    private String find_board_regdate;
    private String find_board_update;

    private String find_board_image; // 파일 경로/이름 저장

    // ★★★ [추가] 키워드/해시태그 관련 필드 ★★★
    private String find_board_keyword; // 쉼표로 구분된 키워드 문자열 (DB 저장용)
    
    // 화면 표시용 추가 데이터
    private String nickname;   // 작성자 닉네임
    private int commentCount;  // 댓글 수
    private int likeCount;     // 총 추천수
    private boolean liked;     // 현재 로그인한 사용자의 추천 여부 (true/false)
    
    // ★★★ 핵심 수정: 스크랩 정보 추가 ★★★
    private int scrapCount;    // 총 스크랩 수
    private boolean scrapped;  // 현재 로그인한 사용자의 스크랩 여부 (true/false)
    private int rownum;
    
    
}
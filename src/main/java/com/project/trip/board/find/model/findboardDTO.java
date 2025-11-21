// findboardDTO.java (최종 수정본)
package com.project.trip.board.find.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class findboardDTO {

    private int find_board_id;
    private String user_id;
    private String find_board_title;
    private String find_board_content;
    private int find_board_view_count;
    private int find_board_report_count;
    private String find_board_report_status;
    private String find_board_regdate;
    private String find_board_update;
    private String nickname;
    private int commentCount;
    private int likeCount;
    private boolean liked;
    private int scrapCount;
    private boolean scrapped;
    private int rownum;
}
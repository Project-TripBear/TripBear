package com.project.trip.board.notice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeDTO {
	
	private String notice_post_id;
	private String admin_id;
	private String notice_header;
	private String notice_content;
	private String notice_view_count;
	private String notice_regdate;

}

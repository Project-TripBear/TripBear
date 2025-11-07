package com.project.trip.board.notice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeDTO {
	
	private String noticePostId;
	private String adminId;
	private String noticeHeader;
	private String noticeContent;
	private String noticeViewCount;
	private String noticeRegdate;

}

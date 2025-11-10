package com.project.trip.board.notice.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeDTO {
	
	private Long noticePostId;
	private Long adminId;
	private String noticeHeader;
	private String noticeContent;
	private Long noticeViewCount;
	private Date noticeRegdate;

}

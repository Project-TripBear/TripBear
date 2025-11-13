package com.project.trip.board.hotdeal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HotDealCommentDTO {
	private String seq;
	private String content;
	private String id;
	private String regdate;
	private String bseq;
	
	private String name;
}

package com.project.trip.board.hotdeal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HotDealCommentDTO {
	private Long seq;
	private String content;
	private String id;
	private String regdate;
	private String bseq;
	private String useq;
	private String name;
}


package com.project.trip.board.hotdeal.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HotDealDTO {
	private String seq;
	private String subject;
	private String content;
	private String id;
	private String regdate;
	private String readcount;
	private String category;
	private String status;
	private String itemName;
	private String price;
	private String url;
	private String useq;
	private String boradTitle;
	private String boradCode;
	private String commentcontent;
	
	private String name;			//작성자
	private String commentCount;	//댓글 수
	private String img; 			//이미지

}

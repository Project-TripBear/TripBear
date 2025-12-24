package com.project.trip.board.routepost.model;

import lombok.Data;

@Data
public class RoutePostDTO {
	
	
	private int routepostId;
	private long userId;
	private String routepostTitle;
    private String routepostContent;
    private String routepostSatisfaction;
    private String routepostReportStatus;
    private int routepostViewCount;
    private int routepostReportCount;
    private int likeCount;
    private String nickname;
    private String routepostRegdate;
    private long commentCount; // 댓글 수 추가
    private int routeId; //연결된 루트 추가
	
}

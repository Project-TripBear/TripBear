package com.project.trip.board.routepost.model;

import java.util.List;

import lombok.Data;

@Data
public class RoutePostDTO {
	
	private int routepostId;
    private String userId;
    private String routepostTitle;
    private String routepostContent;
    private String routepostSatisfaction;
    private String routepostStatus;
    private int routepostViewCount;
    private int routepostReportCount;
    private int likeCount;
    private String nickname;           
    private String routepostRegdate;
    
    private long commentCount; // 댓글 수 추가
    
    private List<RoutePostImageDTO> images;
    
    private int routeId; //연결된 루트 추가
	
}

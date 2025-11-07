package com.project.trip.board.routepost.model;

import java.util.List;

import lombok.Data;

@Data
public class RoutePostDTO {
	
	private String routepostId;
    private String userId;
    private String routepostTitle;
    private String routepostContent;
    private String routepostSatisfaction;
    private String routepostStatus;
    private String routepostViewCount;
    private String routepostReportCount;
    private String likeCount;
    private String nickname;           
    private String routepostRegdate;
    
    private List<RoutePostImageDTO> images;
	
}

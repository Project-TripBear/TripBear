package com.project.trip.board.routepost.model;

import lombok.Data;

@Data
public class RoutePostCommentDTO {

	private String routepostCommentId;
    private String userId;
    private String routepostId;
    private String routepostContent;
    private String routepostRegdate;
    private String routepostCommentReportCount;
    private String commentStatus;
    private String nickname; // JOIN용
	
}

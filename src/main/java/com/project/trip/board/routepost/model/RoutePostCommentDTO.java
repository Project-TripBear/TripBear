package com.project.trip.board.routepost.model;

import lombok.Data;

@Data
public class RoutePostCommentDTO {

	private long routepostCommentId;
    private String userId;
    private int routepostId;
    private String routepostContent;
    private String routepostRegdate;
    private int routepostCommentReportCount;
    private String commentStatus;
    private String nickname; // JOIN용
	
}

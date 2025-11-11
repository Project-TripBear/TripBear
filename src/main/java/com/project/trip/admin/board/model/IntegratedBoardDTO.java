	package com.project.trip.admin.board.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IntegratedBoardDTO {
    private int seq;
    private String boardType;
    private String title;
    private String nickname;
    private Date regdate;
    private int viewCount;
    
    private String targetType; 
    private int commentCount;
    private int likeCount;


}
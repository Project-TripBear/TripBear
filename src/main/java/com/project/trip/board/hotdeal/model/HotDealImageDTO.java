package com.project.trip.board.hotdeal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HotDealImageDTO {
	private String hotdealImageId;
    private String hotdealId;
    private String hotdealImageUrl;
    private int hotdealImageSeq;
}

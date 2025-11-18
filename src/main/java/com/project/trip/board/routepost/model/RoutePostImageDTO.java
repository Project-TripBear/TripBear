package com.project.trip.board.routepost.model;

import lombok.Data;

/**
 * RoutePost 게시글의 이미지 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblRoutePostImage` 테이블과 매핑됩니다.
 */
@Data
public class RoutePostImageDTO {
	
	/**
	 * RoutePost 이미지의 고유 식별자 (PK)
	 */
	private int routepostImageId;
    /**
     * 이미지가 속한 RoutePost 게시글의 고유 식별자 (FK)
     */
    private int routepostId;
    /**
     * 이미지 순서
     */
    private int routepostImageSeq;
    /**
     * 이미지 파일 경로 또는 URL
     */
    private String routepostImageUrl;
	

}

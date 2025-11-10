package com.project.trip.allplace.service;

import java.util.List;

import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;

public interface AllPlaceService {

	public PlaceDTO addPlaceOnDemand(String contentId);
	
	public PlaceDTO getPlaceDetail(long placeId);
	
	public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId);
	
	
	public void addHashtagToPlace(long placeId, String keywordName);
    
    // 2. 해시태그로 장소 목록 검색
    public List<PlaceDTO> findPlacesByKeyword(String keywordName);
	
}

package com.project.trip.allplace.service;

import java.util.List;

import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourItemVO;

public interface AllPlaceService {

	public PlaceDTO addPlaceOnDemand(TourItemVO item);
	
	public PlaceDTO getPlaceDetail(long placeId);
	
	public TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId);
	
	
	public void addHashtagToPlace(long placeId, String keywordName);
    
    // 2. 해시태그로 장소 목록 검색
    public List<PlaceDTO> findPlacesByKeyword(String keywordName);
    
    public TourApiResponseVO searchFestival(String eventStartDate, String arrange, long locationId);
	
    public TourApiResponseVO searchByArea(long locationId, String contentTypeId, String arrange);
    
    List<PlaceDTO> searchByAreaAll(long locationId, String contentTypeId, String arrange, int rows, int maxPages);

	List<PlaceDTO> getRecommendPlaces(PlaceDTO base);

	List<String> getHashtags(long placeId);

}

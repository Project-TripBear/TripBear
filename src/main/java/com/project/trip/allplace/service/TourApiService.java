package com.project.trip.allplace.service;


import java.util.List;

import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourIntroEventVO;
import com.project.trip.allplace.model.TourIntroRestaurantVO;
import com.project.trip.allplace.model.TourIntroVO;
import com.project.trip.allplace.model.TourItemVO;

public interface TourApiService {

	public TourItemVO getPlaceDetail(String contentId);
	
	public TourIntroVO getPlaceIntro(String contentId, String contentTypeId);

	public TourIntroEventVO getEventIntro(String contentId, String contentTypeId);
	
	public TourIntroRestaurantVO getRestaurantIntro(String contentId, String contentTypeId);
	/**
	 * 3. (키워드검색) API 호출
	 * - arrange 파라미터 추가
	 * - 한글 키워드 인코딩 오류 해결
	 */
	TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId);
	
	public TourApiResponseVO searchFestival(String eventStartDate, String arrange);
	
	public TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange);
	
	List<PlaceDTO> searchByAreaAll(long locationId, String contentTypeId, String arrange, int rows, int maxPages);

	
}

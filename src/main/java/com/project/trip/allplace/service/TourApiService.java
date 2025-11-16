package com.project.trip.allplace.service;

import java.util.List;

import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourIntroEventVO;
import com.project.trip.allplace.model.TourIntroRestaurantVO;
import com.project.trip.allplace.model.TourIntroVO;
import com.project.trip.allplace.model.TourItemVO;

public interface TourApiService {

    // (기존)
    TourApiResponseVO searchByKeyword(String keyword, String arrange, String contentTypeId);
    TourApiResponseVO searchFestival(String eventStartDate, String arrange, String areaCode);
    TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange, int pageNo, int rows);
    List<TourItemVO> searchByAreaAllRaw(String areaCode, String contentTypeId, String arrange, int rows, int maxPages);
    TourItemVO getPlaceDetail(String contentId);

    // (기존 - intro API)
    TourIntroVO getPlaceIntro(String contentId, String contentTypeId);
    TourIntroEventVO getEventIntro(String contentId, String contentTypeId);
    TourIntroRestaurantVO getRestaurantIntro(String contentId, String contentTypeId);
    
    // --- [신규 추가] ---
    // AllPlaceServiceImpl.java의 282라인이 호출하기 위해 필요합니다.
    TourApiResponseVO searchByArea(String areaCode, String contentTypeId, String arrange);
    
    //지도 idle사용
    public TourApiResponseVO searchByLocation(String lat, String lng, String radius, String contentTypeId);
}
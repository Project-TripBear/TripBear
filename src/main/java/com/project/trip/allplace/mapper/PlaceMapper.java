package com.project.trip.allplace.mapper;

import java.util.List;

import com.project.trip.allplace.model.KeywordDTO;
import com.project.trip.allplace.model.KeywordLinkDTO;
import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TouristSpotDTO;

public interface PlaceMapper {

	PlaceDTO findPlaceByContentId(String apiContentId);
	
	void insertPlace(PlaceDTO place);
	
	public int insertTouristSpot(TouristSpotDTO dto);

	PlaceDTO getPlaceDetailById(long placeId);
	
	// 1. 키워드 이름으로 tblKeyword 조회
    public KeywordDTO findKeywordByName(String keywordName);
    
    // 2. tblKeyword에 새 키워드 INSERT
    public int insertKeyword(KeywordDTO dto);
    
    // 3. tblKeywordLink에 장소-키워드 연결
    public int insertKeywordLink(KeywordLinkDTO link);
    
    // 4. 키워드 이름으로 장소 목록 검색 (3개 테이블 JOIN)
    public List<PlaceDTO> findPlacesByKeywordName(String keywordName);
	
}

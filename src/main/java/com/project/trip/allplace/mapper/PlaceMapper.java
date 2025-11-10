package com.project.trip.allplace.mapper;

import com.project.trip.allplace.model.PlaceDTO;

public interface PlaceMapper {

	PlaceDTO findPlaceByContentId(String apiContentId);
	
	void insertPlace(PlaceDTO place);
	
}

package com.project.trip.mypage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.project.trip.mypage.model.AccomReservationViewDTO;
import com.project.trip.mypage.model.BoardDTO;
import com.project.trip.mypage.model.CarReservationViewDTO;


public interface MyPageMapper {

	int getAllBoardTotalCount(Map<String, String> map);

	List<BoardDTO> totalBoardList(Map<String, String> map);

	int getAllCommentTotalCount(Map<String, String> map);

	List<BoardDTO> totalCommentList(Map<String, String> map);

	int getAllLikeTotalCount(Map<String, String> map);

	List<BoardDTO> totalLikeList(Map<String, String> map);

	int getAllScrapTotalCount(Map<String, String> map);

	List<BoardDTO> totalScrapList(Map<String, String> map);

	int getAccomReservationTotalCount(Map<String, String> map);

	List<AccomReservationViewDTO> totalAccomList(Map<String, String> map);
	 AccomReservationViewDTO getAccomReservation(
		        @Param("seq") String seq, 
		        @Param("accomseq") String accomseq
		    );
	void addAccomCancel(String accomseq);

	int getCarReservationTotalCount(Map<String, String> map);

	List<CarReservationViewDTO> totalCarList(Map<String, String> map);

	  CarReservationViewDTO getCarReservation(
		        @Param("seq") String seq, 
		        @Param("carseq") String carseq
		    );
	void addCarCancel(String carseq);





}

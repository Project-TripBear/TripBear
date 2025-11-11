package com.project.trip.mypage.mapper;

import java.util.List;
import java.util.Map;

import com.project.trip.mypage.model.BoardDTO;


public interface MyPageMapper {

	int getAllBoardTotalCount(Map<String, String> map);

	List<BoardDTO> totalBoardList(Map<String, String> map);

	int getAllCommentTotalCount(Map<String, String> map);

	List<BoardDTO> totalCommentList(Map<String, String> map);

	int getAllLikeTotalCount(Map<String, String> map);

	List<BoardDTO> totalLikeList(Map<String, String> map);

	int getAllScrapTotalCount(Map<String, String> map);

	List<BoardDTO> totalScrapList(Map<String, String> map);



}

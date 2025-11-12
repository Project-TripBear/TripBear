package com.project.trip.board.hotdeal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.trip.board.hotdeal.model.HotDealCommentDTO;
import com.project.trip.board.hotdeal.model.HotDealDTO;

@Mapper
public interface HotDealMapper {

	int getBoardTotalCount(Map<String, String> map);

	List<HotDealDTO> list(Map<String, String> map);

	void updateReadcount(@Param("seq") String seq);

	HotDealDTO get(@Param("seq") String seq);

	List<HotDealCommentDTO> listComment(@Param("bseq") String seq);

	
	

	int delComment(@Param("seq") String seq, @Param("userId") String userId);



	HotDealCommentDTO getComment(@Param("seq") Long seq);

	int addComment(HotDealCommentDTO dto);

	List<HotDealCommentDTO> moreComment(Map<String, Object> params);

	int editComment(HotDealCommentDTO dto);

}

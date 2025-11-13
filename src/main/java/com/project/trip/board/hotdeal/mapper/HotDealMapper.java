package com.project.trip.board.hotdeal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.trip.board.hotdeal.model.HotDealCommentDTO;
import com.project.trip.board.hotdeal.model.HotDealDTO;
import com.project.trip.board.hotdeal.model.HotDealImageDTO;

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
	
	 // 게시글 등록
    int insertBoard(HotDealDTO board);

    // 최근 게시글 Seq 조회 (예: 방금 등록한 게시글 번호)
    String selectRecentSeq(@Param("board") HotDealDTO board);

    // 이미지 데이터 개별 등록
	int insertBoardImage(Map<String, Object> param);

	List<HotDealImageDTO> selectImages(String seq);

	int updateBoard(HotDealDTO dto);

	void deleteImage(String imageId);

	int selectMaxImageSeq(String seq);

	void deleteAllImages(String seq);

	int deleteBoard(String seq);


}

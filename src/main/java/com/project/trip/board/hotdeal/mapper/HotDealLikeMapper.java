package com.project.trip.board.hotdeal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HotDealLikeMapper {

	int likeCheck(@Param("userId") String userSeq,@Param("hotdealId") String seq);

	int scrapCheck(@Param("userId") String userSeq,@Param("hotdealId") String seq);

}

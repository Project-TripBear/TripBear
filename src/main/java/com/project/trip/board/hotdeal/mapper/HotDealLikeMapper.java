package com.project.trip.board.hotdeal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HotDealLikeMapper {

	 int likeCheck(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
	 void likeAdd(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
	 void likeDel(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
	 int getLikeCount(@Param("bseq") String bseq);
	    
	 int scrapCheck(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
	 void scrapAdd(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
	 void scrapDel(@Param("userSeq") String userSeq, @Param("bseq") String bseq);


}

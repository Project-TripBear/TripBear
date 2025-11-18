package com.project.trip.board.hotdeal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HotDealLikeMapper {

    /**
     * 특정 사용자가 특정 게시글에 좋아요를 눌렀는지 여부를 확인합니다.
     *
     * @param userSeq 사용자 고유 번호
     * @param bseq    게시글 고유 번호
     * @return 좋아요 여부 (1: 좋아요 누름, 0: 좋아요 누르지 않음)
     */
    int likeCheck(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
    /**
     * 특정 사용자가 특정 게시글에 좋아요를 추가합니다.
     *
     * @param userSeq 사용자 고유 번호
     * @param bseq    게시글 고유 번호
     */
    void likeAdd(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
    /**
     * 특정 사용자가 특정 게시글에 좋아요를 취소합니다.
     *
     * @param userSeq 사용자 고유 번호
     * @param bseq    게시글 고유 번호
     */
    void likeDel(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
    /**
     * 특정 게시글의 총 좋아요 개수를 조회합니다.
     *
     * @param bseq 게시글 고유 번호
     * @return 해당 게시글의 총 좋아요 개수
     */
    int getLikeCount(@Param("bseq") String bseq);
	    
    /**
     * 특정 사용자가 특정 게시글을 스크랩했는지 여부를 확인합니다.
     *
     * @param userSeq 사용자 고유 번호
     * @param bseq    게시글 고유 번호
     * @return 스크랩 여부 (1: 스크랩함, 0: 스크랩하지 않음)
     */
    int scrapCheck(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
    /**
     * 특정 사용자가 특정 게시글을 스크랩 목록에 추가합니다.
     *
     * @param userSeq 사용자 고유 번호
     * @param bseq    게시글 고유 번호
     */
    void scrapAdd(@Param("userSeq") String userSeq, @Param("bseq") String bseq);
	    
    /**
     * 특정 사용자가 특정 게시글의 스크랩을 취소합니다.
     *
     * @param userSeq 사용자 고유 번호
     * @param bseq    게시글 고유 번호
     */
    void scrapDel(@Param("userSeq") String userSeq, @Param("bseq") String bseq);


}

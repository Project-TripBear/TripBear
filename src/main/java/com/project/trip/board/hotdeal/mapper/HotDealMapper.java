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

    /**
     * 검색 조건에 맞는 핫딜 게시글의 총 개수를 조회합니다.
     *
     * @param map 검색 조건을 담은 {@code Map<String, String>} (column, word, search 등)
     * @return 조회된 게시글의 총 개수
     */
    int getBoardTotalCount(Map<String, String> map);

    /**
     * 검색 및 페이징 조건에 맞는 핫딜 게시글 목록을 조회합니다.
     *
     * @param map 검색 및 페이징 조건을 담은 {@code Map<String, String>} (column, word, search, begin, end 등)
     * @return 조회된 핫딜 게시글 {@link HotDealDTO}의 리스트
     */
    List<HotDealDTO> list(Map<String, String> map);

    /**
     * 특정 핫딜 게시글의 조회수를 1 증가시킵니다.
     *
     * @param seq 조회수를 증가시킬 게시글의 고유 번호
     */
    void updateReadcount(@Param("seq") String seq);

    /**
     * 특정 핫딜 게시글의 상세 정보를 조회합니다.
     *
     * @param seq 조회할 게시글의 고유 번호
     * @return 조회된 핫딜 게시글 {@link HotDealDTO}
     */
    HotDealDTO get(@Param("seq") String seq);

    /**
     * 특정 핫딜 게시글에 달린 모든 댓글 목록을 조회합니다.
     *
     * @param seq 댓글을 조회할 게시글의 고유 번호
     * @return 조회된 댓글 {@link HotDealCommentDTO}의 리스트
     */
    List<HotDealCommentDTO> listComment(@Param("bseq") String seq);

	
	

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param seq    삭제할 댓글의 고유 번호
     * @param userId 댓글 작성자의 사용자 ID (권한 확인용)
     * @return 삭제된 행의 수
     */
    int delComment(@Param("seq") String seq, @Param("userId") String userId);



    /**
     * 특정 댓글의 상세 정보를 조회합니다.
     *
     * @param seq 조회할 댓글의 고유 번호
     * @return 조회된 댓글 {@link HotDealCommentDTO}
     */
    HotDealCommentDTO getComment(@Param("seq") Long seq);

    /**
     * 새로운 댓글을 데이터베이스에 삽입합니다.
     *
     * @param dto 삽입할 댓글 정보를 담은 {@link HotDealCommentDTO} 객체
     * @return 삽입된 행의 수
     */
    int addComment(HotDealCommentDTO dto);

    /**
     * 특정 게시글의 추가 댓글 목록을 페이징하여 조회합니다.
     *
     * @param params 게시글 ID("bseq")와 시작 인덱스("begin")를 담은 {@code Map<String, Object>}
     * @return 조회된 댓글 {@link HotDealCommentDTO}의 리스트
     */
    List<HotDealCommentDTO> moreComment(Map<String, Object> params);

    /**
     * 기존 댓글 정보를 데이터베이스에서 업데이트합니다.
     *
     * @param dto 업데이트할 댓글 정보를 담은 {@link HotDealCommentDTO} 객체
     * @return 업데이트된 행의 수
     */
    int editComment(HotDealCommentDTO dto);
	
    /**
     * 새로운 핫딜 게시글을 데이터베이스에 삽입합니다.
     *
     * @param board 삽입할 핫딜 게시글 정보를 담은 {@link HotDealDTO} 객체
     * @return 삽입된 행의 수
     */
    int insertBoard(HotDealDTO board);

    /**
     * 최근에 등록된 핫딜 게시글의 고유 번호(seq)를 조회합니다.
     *
     * @param board 최근 게시글 정보를 담은 {@link HotDealDTO} 객체
     * @return 최근 게시글의 고유 번호
     */
    String selectRecentSeq(@Param("board") HotDealDTO board);

    // 이미지 데이터 개별 등록
    /**
     * 핫딜 게시글에 첨부된 이미지 정보를 데이터베이스에 삽입합니다.
     *
     * @param param 핫딜 ID("hotdealId"), 이미지 파일명("img"), 이미지 순서("hotdealImageSeq")를 담은 {@code Map<String, Object>}
     * @return 삽입된 행의 수
     */
    int insertBoardImage(Map<String, Object> param);

    /**
     * 특정 핫딜 게시글에 첨부된 이미지 목록을 조회합니다.
     *
     * @param seq 이미지를 조회할 게시글의 고유 번호
     * @return 조회된 이미지 {@link HotDealImageDTO}의 리스트
     */
    List<HotDealImageDTO> selectImages(String seq);

    /**
     * 기존 핫딜 게시글 정보를 데이터베이스에서 업데이트합니다.
     *
     * @param dto 업데이트할 핫딜 게시글 정보를 담은 {@link HotDealDTO} 객체
     * @return 업데이트된 행의 수
     */
    int updateBoard(HotDealDTO dto);

    /**
     * 특정 이미지 ID에 해당하는 이미지를 데이터베이스에서 삭제합니다.
     *
     * @param imageId 삭제할 이미지의 고유 ID
     */
    void deleteImage(String imageId);

    /**
     * 특정 핫딜 게시글에 첨부된 이미지 중 가장 큰 순서(seq) 값을 조회합니다.
     *
     * @param seq 게시글의 고유 번호
     * @return 가장 큰 이미지 순서 값
     */
    int selectMaxImageSeq(String seq);



    /**
     * 특정 핫딜 게시글을 데이터베이스에서 삭제합니다.
     *
     * @param seq 삭제할 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int deleteBoard(String seq);
	
    /**
     * 특정 핫딜 게시글에 연결된 모든 이미지 정보를 데이터베이스에서 삭제합니다.
     *
     * @param seq 이미지를 삭제할 게시글의 고유 번호
     */
    void deleteAllImages(String seq);

    /**
     * 특정 핫딜 게시글에 달린 모든 댓글을 데이터베이스에서 삭제합니다.
     *
     * @param seq 댓글을 삭제할 게시글의 고유 번호
     */
    void deleteComment(String seq);

    /**
     * 특정 핫딜 게시글에 대한 모든 좋아요 기록을 데이터베이스에서 삭제합니다.
     *
     * @param seq 좋아요 기록을 삭제할 게시글의 고유 번호
     */
    void deleteLike(String seq);

    /**
     * 특정 핫딜 게시글에 대한 모든 스크랩 기록을 데이터베이스에서 삭제합니다.
     *
     * @param seq 스크랩 기록을 삭제할 게시글의 고유 번호
     */
    void deleteScrap(String seq);


}

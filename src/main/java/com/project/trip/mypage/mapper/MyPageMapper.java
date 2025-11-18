package com.project.trip.mypage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.project.trip.mypage.model.AccomReservationViewDTO;
import com.project.trip.mypage.model.BoardDTO;
import com.project.trip.mypage.model.CarReservationViewDTO;
import com.project.trip.mypage.model.UserRouteViewDTO;

/**
 * 마이페이지 관련 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * 사용자 활동 내역 (게시글, 댓글, 좋아요, 스크랩), 숙소 및 렌터카 예약 내역,
 * 사용자 루트 조회 등 마이페이지 기능을 위한 SQL 쿼리 호출을 정의합니다.
 */
public interface MyPageMapper {

	/**
	 * 특정 사용자의 전체 게시글 수를 조회합니다.
	 *
	 * @param map 검색 및 사용자 정보를 담은 {@code Map<String, String>}
	 * @return 전체 게시글 수
	 */
	int getAllBoardTotalCount(Map<String, String> map);

	/**
	 * 특정 사용자의 게시글 목록을 조회합니다.
	 *
	 * @param map 검색 및 페이징 정보를 담은 {@code Map<String, String>}
	 * @return 게시글 목록 {@code List<BoardDTO>}
	 */
	List<BoardDTO> totalBoardList(Map<String, String> map);

	/**
	 * 특정 사용자의 전체 댓글 수를 조회합니다.
	 *
	 * @param map 검색 및 사용자 정보를 담은 {@code Map<String, String>}
	 * @return 전체 댓글 수
	 */
	int getAllCommentTotalCount(Map<String, String> map);

	/**
	 * 특정 사용자의 댓글 목록을 조회합니다.
	 *
	 * @param map 검색 및 페이징 정보를 담은 {@code Map<String, String>}
	 * @return 댓글 목록 {@code List<BoardDTO>}
	 */
	List<BoardDTO> totalCommentList(Map<String, String> map);

	/**
	 * 특정 사용자의 전체 좋아요 수를 조회합니다.
	 *
	 * @param map 검색 및 사용자 정보를 담은 {@code Map<String, String>}
	 * @return 전체 좋아요 수
	 */
	int getAllLikeTotalCount(Map<String, String> map);

	/**
	 * 특정 사용자의 좋아요 목록을 조회합니다.
	 *
	 * @param map 검색 및 페이징 정보를 담은 {@code Map<String, String>}
	 * @return 좋아요 목록 {@code List<BoardDTO>}
	 */
	List<BoardDTO> totalLikeList(Map<String, String> map);

	/**
	 * 특정 사용자의 전체 스크랩 수를 조회합니다.
	 *
	 * @param map 검색 및 사용자 정보를 담은 {@code Map<String, String>}
	 * @return 전체 스크랩 수
	 */
	int getAllScrapTotalCount(Map<String, String> map);

	/**
	 * 특정 사용자의 스크랩 목록을 조회합니다.
	 *
	 * @param map 검색 및 페이징 정보를 담은 {@code Map<String, String>}
	 * @return 스크랩 목록 {@code List<BoardDTO>}
	 */
	List<BoardDTO> totalScrapList(Map<String, String> map);

	/**
	 * 특정 사용자의 전체 숙소 예약 수를 조회합니다.
	 *
	 * @param map 검색 및 사용자 정보를 담은 {@code Map<String, String>}
	 * @return 전체 숙소 예약 수
	 */
	int getAccomReservationTotalCount(Map<String, String> map);

	/**
	 * 특정 사용자의 숙소 예약 목록을 조회합니다.
	 *
	 * @param map 검색 및 페이징 정보를 담은 {@code Map<String, String>}
	 * @return 숙소 예약 목록 {@code List<AccomReservationViewDTO>}
	 */
	List<AccomReservationViewDTO> totalAccomList(Map<String, String> map);
	/**
	 * 특정 숙소 예약의 상세 정보를 조회합니다.
	 *
	 * @param seq 사용자 고유 번호
	 * @param accomseq 숙소 예약 고유 번호
	 * @return 숙소 예약 상세 정보 {@link AccomReservationViewDTO}
	 */
	 AccomReservationViewDTO getAccomReservation(
		        @Param("seq") String seq, 
		        @Param("accomseq") String accomseq
		    );
	/**
	 * 특정 숙소 예약을 취소 처리합니다.
	 *
	 * @param accomseq 취소할 숙소 예약의 고유 번호
	 */
	void addAccomCancel(String accomseq);

	/**
	 * 특정 사용자의 전체 렌터카 예약 수를 조회합니다.
	 *
	 * @param map 검색 및 사용자 정보를 담은 {@code Map<String, String>}
	 * @return 전체 렌터카 예약 수
	 */
	int getCarReservationTotalCount(Map<String, String> map);

	/**
	 * 특정 사용자의 렌터카 예약 목록을 조회합니다.
	 *
	 * @param map 검색 및 페이징 정보를 담은 {@code Map<String, String>}
	 * @return 렌터카 예약 목록 {@code List<CarReservationViewDTO>}
	 */
	List<CarReservationViewDTO> totalCarList(Map<String, String> map);

	/**
	 * 특정 렌터카 예약의 상세 정보를 조회합니다.
	 *
	 * @param seq 사용자 고유 번호
	 * @param carseq 렌터카 예약 고유 번호
	 * @return 렌터카 예약 상세 정보 {@link CarReservationViewDTO}
	 */
	  CarReservationViewDTO getCarReservation(
		        @Param("seq") String seq, 
		        @Param("carseq") String carseq
		    );
	/**
	 * 특정 렌터카 예약을 취소 처리합니다.
	 *
	 * @param carseq 취소할 렌터카 예약의 고유 번호
	 */
	void addCarCancel(String carseq);

	/**
	 * 특정 사용자의 루트 목록을 조회합니다.
	 *
	 * @param map 검색 및 페이징 정보를 담은 {@code Map<String, String>}
	 * @return 사용자 루트 목록 {@code List<UserRouteViewDTO>}
	 */
	List<UserRouteViewDTO> UserRouteList(Map<String, String> map);

	/**
	 * 특정 사용자의 전체 루트 수를 조회합니다.
	 *
	 * @param map 검색 및 사용자 정보를 담은 {@code Map<String, String>}
	 * @return 전체 루트 수
	 */
	int getUserRouteTotalCount(Map<String, String> map);





}

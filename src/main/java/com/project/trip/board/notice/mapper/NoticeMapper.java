package com.project.trip.board.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.trip.board.notice.model.NoticeDTO;

/**
 * 공지사항 게시판과 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 공지사항 목록 조회, 등록, 상세 조회, 수정, 삭제 및 조회수 증가 등
 * 공지사항 관리에 필요한 데이터베이스 작업을 정의합니다.
 * </p>
 */
@Mapper
public interface NoticeMapper {
	/**
	 * 모든 공지사항 목록을 조회합니다.
	 *
	 * @return 모든 공지사항 {@link NoticeDTO} 객체의 리스트
	 */
	List<NoticeDTO> findAll();

	/**
	 * 새로운 공지사항을 데이터베이스에 삽입합니다.
	 *
	 * @param dto 삽입할 공지사항 정보를 담은 {@link NoticeDTO} 객체
	 */
	void insert(NoticeDTO dto);

	/**
	 * 특정 ID를 가진 공지사항을 조회합니다.
	 *
	 * @param noticePostId 조회할 공지사항의 고유 ID
	 * @return 조회된 공지사항 {@link NoticeDTO} 객체, 해당 ID의 공지사항이 없으면 null
	 */
	NoticeDTO findById(Long noticePostId);

	/**
	 * 기존 공지사항 정보를 업데이트합니다.
	 *
	 * @param dto 업데이트할 공지사항 정보를 담은 {@link NoticeDTO} 객체
	 */
	void update(NoticeDTO dto);

	/**
	 * 특정 ID를 가진 공지사항을 삭제합니다.
	 *
	 * @param noticePostId 삭제할 공지사항의 고유 ID
	 */
	void delete(Long noticePostId);

	/**
	 * 특정 공지사항의 조회수를 1 증가시킵니다.
	 *
	 * @param noticePostId 조회수를 증가시킬 공지사항의 고유 ID
	 */
	void incrementViewCount(Long noticePostId);


}

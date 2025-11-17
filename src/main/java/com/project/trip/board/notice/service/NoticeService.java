package com.project.trip.board.notice.service;

import java.util.List;

import com.project.trip.board.notice.model.NoticeDTO;


public interface NoticeService {

    /**
     * 모든 공지사항 게시글 목록을 조회합니다.
     *
     * @return 조회된 공지사항 {@link NoticeDTO}의 리스트
     */
    List<NoticeDTO> getNoticeList();

    /**
     * 새로운 공지사항 게시글을 생성합니다.
     *
     * @param dto 생성할 공지사항 정보를 담은 {@link NoticeDTO} 객체
     */
    void createNotice(NoticeDTO dto);

    /**
     * 공지사항 수정을 위해 특정 공지사항 게시글의 상세 정보를 조회합니다.
     *
     * @param noticePostId 조회할 공지사항 게시글의 고유 번호
     * @return 조회된 공지사항 {@link NoticeDTO}
     */
    NoticeDTO getNoticeForEdit(Long noticePostId);

    /**
     * 기존 공지사항 게시글 정보를 업데이트합니다.
     *
     * @param dto 업데이트할 공지사항 정보를 담은 {@link NoticeDTO} 객체
     */
    void updateNotice(NoticeDTO dto);

    /**
     * 특정 공지사항 게시글을 삭제합니다.
     *
     * @param noticePostId 삭제할 공지사항 게시글의 고유 번호
     */
    void deleteNotice(Long noticePostId);

    /**
     * 특정 공지사항 게시글의 상세 정보를 조회하고, 조회수를 증가시킵니다.
     *
     * @param noticePostId 조회할 공지사항 게시글의 고유 번호
     * @return 조회된 공지사항 {@link NoticeDTO}
     */
    NoticeDTO getNoticeDetail(Long noticePostId);

	

}

package com.project.trip.board.notice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.trip.board.notice.mapper.NoticeMapper;
import com.project.trip.board.notice.model.NoticeDTO;

import lombok.RequiredArgsConstructor;

/**
 * {@link NoticeService} 인터페이스의 구현 클래스입니다.
 * <p>
 * 공지사항 게시판과 관련된 비즈니스 로직을 처리합니다.
 * 공지사항 목록 조회, 생성, 수정, 삭제 및 상세 조회 기능을 제공합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
	
	private final NoticeMapper noticeMapper;

    /**
     * 모든 공지사항 게시글 목록을 조회합니다.
     *
     * @return 조회된 공지사항 {@link NoticeDTO}의 리스트
     */
    @Override
    public List<NoticeDTO> getNoticeList() {
		
		return noticeMapper.findAll();
	}

    /**
     * 새로운 공지사항 게시글을 생성합니다.
     *
     * @param dto 생성할 공지사항 정보를 담은 {@link NoticeDTO} 객체
     */
    @Override
    public void createNotice(NoticeDTO dto) {
		
		noticeMapper.insert(dto);
	}

    /**
     * 공지사항 수정을 위해 특정 공지사항 게시글의 상세 정보를 조회합니다.
     *
     * @param noticePostId 조회할 공지사항 게시글의 고유 번호
     * @return 조회된 공지사항 {@link NoticeDTO}
     */
    @Override
    public NoticeDTO getNoticeForEdit(Long noticePostId) {

		return noticeMapper.findById(noticePostId);
	}

    /**
     * 기존 공지사항 게시글 정보를 업데이트합니다.
     *
     * @param dto 업데이트할 공지사항 정보를 담은 {@link NoticeDTO} 객체
     */
    @Override
    public void updateNotice(NoticeDTO dto) {
		
		noticeMapper.update(dto);
	}

    /**
     * 특정 공지사항 게시글을 삭제합니다.
     *
     * @param noticePostId 삭제할 공지사항 게시글의 고유 번호
     */
    @Override
    public void deleteNotice(Long noticePostId) {
		noticeMapper.delete(noticePostId);
	}

    /**
     * 특정 공지사항 게시글의 상세 정보를 조회하고, 조회수를 증가시킵니다.
     *
     * @param noticePostId 조회할 공지사항 게시글의 고유 번호
     * @return 조회된 공지사항 {@link NoticeDTO}
     */
    @Override
    public NoticeDTO getNoticeDetail(Long noticePostId) {
		
		noticeMapper.incrementViewCount(noticePostId);
		
		return noticeMapper.findById(noticePostId);
	}

}
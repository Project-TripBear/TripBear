package com.project.trip.board.notice.service;

import java.util.List;

import com.project.trip.board.notice.model.NoticeDTO;


public interface NoticeService {

	List<NoticeDTO> getNoticeList();

	void createNotice(NoticeDTO dto);

	NoticeDTO getNoticeForEdit(Long noticePostId);

	void updateNotice(NoticeDTO dto);

	void deleteNotice(Long noticePostId);

	NoticeDTO getNoticeDetail(Long noticePostId);

	

}

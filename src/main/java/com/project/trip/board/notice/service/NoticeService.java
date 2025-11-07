package com.project.trip.board.notice.service;

import java.util.List;

import com.project.trip.board.notice.model.NoticeDTO;

public interface NoticeService {

	List<NoticeDTO> getNoticeList();

	NoticeDTO getNoticeDetail(String notice_id);

	int creatNotice(NoticeDTO dto);

	

}

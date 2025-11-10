package com.project.trip.board.notice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.trip.board.notice.mapper.NoticeMapper;
import com.project.trip.board.notice.model.NoticeDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
	
	private final NoticeMapper noticeMapper;

	@Override
	public List<NoticeDTO> getNoticeList() {
		
		return noticeMapper.findAll();
	}

	@Override
	public void createNotice(NoticeDTO dto) {
		
		noticeMapper.insert(dto);
	}

	@Override
	public NoticeDTO getNoticeForEdit(Long noticePostId) {

		return noticeMapper.findById(noticePostId);
	}

	@Override
	public void updateNotice(NoticeDTO dto) {
		
		noticeMapper.update(dto);
	}

	@Override
	public void deleteNotice(Long noticePostId) {
		noticeMapper.delete(noticePostId);
	}

	@Override
	public NoticeDTO getNoticeDetail(Long noticePostId) {
		
		noticeMapper.incrementViewCount(noticePostId);
		
		return noticeMapper.findById(noticePostId);
	}

}
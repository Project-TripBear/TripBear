package com.project.trip.board.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.trip.board.notice.model.NoticeDTO;

@Mapper
public interface NoticeMapper {
	List<NoticeDTO> findAll();

	void insert(NoticeDTO dto);

	NoticeDTO findById(Long noticePostId);

	void update(NoticeDTO dto);

	void delete(Long noticePostId);

	void incrementViewCount(Long noticePostId);


}

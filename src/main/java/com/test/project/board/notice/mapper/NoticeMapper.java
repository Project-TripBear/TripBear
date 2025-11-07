package com.test.project.board.notice.mapper;

import java.util.List;

import com.test.project.board.notice.NoticeDTO;

public interface NoticeMapper {

	List<NoticeDTO> list();

	NoticeDTO get(String seq);

	void edit(NoticeDTO dto);

	void del(String seq);



}

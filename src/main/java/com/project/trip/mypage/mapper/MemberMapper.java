package com.project.trip.mypage.mapper;

import com.project.trip.mypage.model.UserDTO;

public interface MemberMapper {

	UserDTO get(String username);

	void add(UserDTO dto);

	int idCheck(String id);

	String idSelect(UserDTO dto);

	void PwUpdate(UserDTO updateDto);

	int userCheckByIdAndEmail(UserDTO checkDto);

	int userEdit(UserDTO dto);

	void userDel(String seq);

	Integer getMyBoardCount(String username);

	Integer getMyCommentCount(String username);

	Integer getMyLikeTotalCount(String username);

	Integer getMyScrapTotalCount(String username);

	UserDTO getUserByUsername(String username);



}

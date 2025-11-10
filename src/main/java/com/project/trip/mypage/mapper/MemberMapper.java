package com.project.trip.mypage.mapper;

import org.apache.ibatis.annotations.Param;

import com.project.trip.mypage.model.UserDTO;

public interface MemberMapper {

	UserDTO get(String username);

	void add(UserDTO dto);

	int idCheck(String id);

	String idSelect(UserDTO dto);

	void PwUpdate(UserDTO updateDto);

	int userCheckByIdAndEmail(UserDTO checkDto);



}

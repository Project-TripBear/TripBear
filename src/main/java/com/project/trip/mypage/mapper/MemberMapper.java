package com.project.trip.mypage.mapper;

import com.project.trip.mypage.model.UserDTO;

/**
 * 회원 정보와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * 회원가입, 로그인, 정보 조회 및 수정, 탈퇴, 활동 내역 요약 등 회원 관리를 위한
 * SQL 쿼리 호출을 정의합니다.
 */
public interface MemberMapper {

	/**
	 * 사용자 아이디(username)를 통해 회원 정보를 조회합니다.
	 *
	 * @param username 조회할 사용자의 아이디
	 * @return 조회된 {@link UserDTO} 객체
	 */
	UserDTO get(String username);

	/**
	 * 새로운 회원 정보를 데이터베이스에 추가합니다.
	 *
	 * @param dto 추가할 회원 정보를 담은 {@link UserDTO} 객체
	 */
	void add(UserDTO dto);

	/**
	 * 아이디의 중복 여부를 확인합니다.
	 *
	 * @param id 중복 여부를 확인할 아이디
	 * @return 중복이면 1, 아니면 0
	 */
	int idCheck(String id);

	/**
	 * 사용자 정보를 기반으로 아이디를 조회합니다.
	 *
	 * @param dto 사용자 정보를 담은 {@link UserDTO} 객체 (주로 이름과 이메일 사용)
	 * @return 조회된 아이디 문자열
	 */
	String idSelect(UserDTO dto);

	/**
	 * 사용자의 비밀번호를 업데이트합니다.
	 *
	 * @param updateDto 업데이트할 사용자 정보 (아이디, 이메일, 암호화된 새 비밀번호)를 담은 {@link UserDTO} 객체
	 */
	void PwUpdate(UserDTO updateDto);

	/**
	 * 아이디와 이메일이 일치하는 사용자가 존재하는지 확인합니다.
	 *
	 * @param checkDto 확인할 아이디와 이메일 정보를 담은 {@link UserDTO} 객체
	 * @return 일치하는 사용자가 있으면 1, 없으면 0
	 */
	int userCheckByIdAndEmail(UserDTO checkDto);

	/**
	 * 사용자 정보를 업데이트합니다.
	 *
	 * @param dto 업데이트할 사용자 정보를 담은 {@link UserDTO} 객체
	 * @return 업데이트된 행의 수
	 */
	int userEdit(UserDTO dto);

	/**
	 * 특정 사용자를 삭제(탈퇴) 처리합니다.
	 *
	 * @param seq 삭제할 사용자의 고유 번호 (시퀀스)
	 */
	void userDel(String seq);

	/**
	 * 특정 사용자가 작성한 게시글의 총 개수를 조회합니다.
	 *
	 * @param username 게시글 개수를 조회할 사용자의 아이디
	 * @return 해당 사용자의 게시글 총 개수
	 */
	Integer getMyBoardCount(String username);

	/**
	 * 특정 사용자가 작성한 댓글의 총 개수를 조회합니다.
	 *
	 * @param username 댓글 개수를 조회할 사용자의 아이디
	 * @return 해당 사용자의 댓글 총 개수
	 */
	Integer getMyCommentCount(String username);

	/**
	 * 특정 사용자가 누른 좋아요의 총 개수를 조회합니다.
	 *
	 * @param username 좋아요 개수를 조회할 사용자의 아이디
	 * @return 해당 사용자의 좋아요 총 개수
	 */
	Integer getMyLikeTotalCount(String username);

	/**
	 * 특정 사용자가 스크랩한 게시글의 총 개수를 조회합니다.
	 *
	 * @param username 스크랩 개수를 조회할 사용자의 아이디
	 * @return 해당 사용자의 스크랩 총 개수
	 */
	Integer getMyScrapTotalCount(String username);

	/**
	 * 사용자 아이디(username)를 통해 회원 정보를 조회합니다.
	 * 이 메서드는 주로 Spring Security에서 사용자 정보를 로드할 때 사용됩니다.
	 *
	 * @param username 조회할 사용자의 아이디
	 * @return 조회된 {@link UserDTO} 객체
	 */
	UserDTO getUserByUsername(String username);



}

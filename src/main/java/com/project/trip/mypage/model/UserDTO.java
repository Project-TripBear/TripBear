package com.project.trip.mypage.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * 회원가입, 로그인, 마이페이지 등 사용자 정보가 필요한 다양한 곳에서 사용됩니다.
 */
@Getter
@Setter
@ToString
public class UserDTO {
	/**
	 * 사용자 고유 번호 (시퀀스)
	 */
	private String seq;
	/**
	 * 사용자 아이디
	 */
	private String id;
	/**
	 * 사용자 고유 식별자 (예: OAuth 로그인 시 사용)
	 */
	private String uid;
	/**
	 * 사용자 비밀번호 (암호화되어 저장됨)
	 */
	private String pw;
	/**
	 * 주민등록번호 (또는 생년월일 등 개인 식별 정보)
	 */
	private String ssn;
	/**
	 * 전화번호
	 */
	private String phoneNumber;
	/**
	 * 닉네임
	 */
	private String nickName;
	/**
	 * 사용자 이름
	 */
	private String name;
	/**
	 * 이메일 주소
	 */
	private String email;
	/**
	 * 주소
	 */
	private String address;
	/**
	 * 성별
	 */
	private String gender;
	/**
	 * 키
	 */
	private String height;
	/**
	 * 몸무게
	 */
	private String weight;
	/**
	 * 건강 목표
	 */
	private String healthGoals;
	/**
	 * 사용자 권한 (예: "ROLE_USER", "ROLE_ADMIN")
	 */
	private String auth;
	/**
	 * 사용자 상태 ID (예: 활성, 비활성, 탈퇴)
	 */
	private String userStatusId;




}

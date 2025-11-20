package com.project.trip.admin.user.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 관리자 페이지에서 사용자 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 사용자 ID, 닉네임, 실명, 이메일, 가입일, 상태 등 사용자 관련 정보를 포함합니다.
 * </p>
 */
@Getter
@Setter
@ToString
public class AdminUserDTO {
    /**
     * 사용자의 고유 식별자
     */
    private int userId;
    /**
     * 사용자의 닉네임
     */
    private String nickname;
    /**
     * 사용자의 실명
     */
    private String realName;
    /**
     * 사용자의 이메일 주소
     */
    private String email;
    /**
     * 사용자 가입일
     */
    private Date regdate;
    /**
     * 사용자의 현재 상태 (예: 활성, 비활성, 탈퇴 등)
     */
    private String status;
}


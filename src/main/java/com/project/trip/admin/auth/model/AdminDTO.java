// 경로: com.project.trip.admin.model.AdminDTO.java
package com.project.trip.admin.auth.model;
import java.io.Serializable;
import lombok.Data;

/**
 * 관리자 계정 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 관리자 ID, 비밀번호, 권한 등 관리자 인증 및 인가에 필요한 정보를 포함합니다.
 * {@link Serializable}을 구현하여 세션 등에 저장될 수 있습니다.
 * </p>
 */
@Data
public class AdminDTO implements Serializable {
    /**
     * 관리자 계정의 고유 ID (사용자 이름)
     */
    private String adminId;
    /**
     * 관리자 계정의 비밀번호 (암호화된 형태)
     */
    private String adminPw;
    /**
     * 관리자의 권한 (예: "ROLE_ADMIN")
     */
    private String auth;
    // (기타 관리자 이름, 부서 등 추가 가능)
}
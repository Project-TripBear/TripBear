// 파일 경로: src/main/java/com/trip/admin/model/suspendedUserDTO.java
package com.project.trip.admin.user.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 관리자 페이지에서 정지된 사용자 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 사용자 ID, 회원 정지 ID, 닉네임, 정지 사유, 정지 시작일, 정지 종료일 등 정지된 사용자 관련 정보를 포함합니다.
 * </p>
 */
@Getter
@Setter
@ToString
public class suspendedUserDTO {
    /**
     * 정지된 사용자의 고유 식별자
     */
    private int userId;
    /**
     * 회원 정지 내역의 고유 식별자
     */
    private int memsuspendedId;
    /**
     * 정지된 사용자의 닉네임
     */
    private String nickname;
    /**
     * 사용자 정지 사유
     */
    private String suspendedReason;
    /**
     * 사용자 정지 시작일
     */
    private Date suspendedStartDate;
    /**
     * 사용자 정지 종료일
     */
    private Date suspendedEndDate;
}
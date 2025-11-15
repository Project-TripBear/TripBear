// 경로: com.project.trip.admin.model.AdminDTO.java
package com.project.trip.admin.auth.model;
import java.io.Serializable;
import lombok.Data;

@Data
public class AdminDTO implements Serializable {
    private String adminId; // 관리자 ID (예: admin_id)
    private String adminPw; // 관리자 PW (예: admin_pw)
    private String auth;    // 권한 (예: "ROLE_ADMIN")
    // (기타 관리자 이름, 부서 등 추가 가능)
}
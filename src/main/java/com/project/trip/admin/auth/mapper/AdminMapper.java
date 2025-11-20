// 경로: com.project.trip.admin.auth.mapper.AdminMapper.java
package com.project.trip.admin.auth.mapper; // ★★★ 패키지 경로 수정 ★★★

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.trip.admin.auth.model.AdminDTO;
	
/**
 * 관리자 인증 및 권한 부여와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * 관리자 정보 조회 기능을 제공합니다.
 */
@Mapper
public interface AdminMapper {

    /**
     * 관리자 아이디(username)로 관리자 정보를 조회합니다.
     * (CustomAdminUserDetailsService에서 사용)
     * @param username (로그인 시 입력한 아이디)
     * @return AdminDTO (관리자 정보)
     */
    public AdminDTO getAdmin(String username);
    
    /**
     * 관리자 아이디(username)를 통해 관리자 고유 ID를 조회합니다.
     *
     * @param adminUsername 조회할 관리자의 아이디
     * @return 관리자의 고유 ID
     */
    public Long getAdminIdByUsername(@Param("adminUsername") String adminUsername); 

    
}	
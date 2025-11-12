// 경로: com.project.trip.admin.auth.mapper.AdminMapper.java
package com.project.trip.admin.auth.mapper; // ★★★ 패키지 경로 수정 ★★★

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.trip.admin.auth.model.AdminDTO;
	
@Mapper
public interface AdminMapper {

    /**
     * 관리자 아이디(username)로 관리자 정보를 조회합니다.
     * (CustomAdminUserDetailsService에서 사용)
     * @param username (로그인 시 입력한 아이디)
     * @return AdminDTO (관리자 정보)
     */
    public AdminDTO getAdmin(String username);
    
    public Long getAdminIdByUsername(@Param("adminUsername") String adminUsername); 

    
}	
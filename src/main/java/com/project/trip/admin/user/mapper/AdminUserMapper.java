// 파일 경로: com.project.trip.admin.user.mapper.AdminUserMapper.java (Legacy 구조 참고)

package com.project.trip.admin.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.project.trip.admin.user.model.suspendedUserDTO;
import com.project.trip.admin.user.model.AdminUserDTO;

/**
 * 관리자 페이지의 사용자 관리와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 사용자 목록 조회, 사용자 상태 업데이트, 사용자 정지 로그 기록 및 조회 등
 * 다양한 사용자 관리 기능을 위한 SQL 쿼리 호출을 정의합니다.
 * </p>
 */
public interface AdminUserMapper {
    
	/**
     * 검색 조건에 맞는 전체 사용자 수를 조회합니다.
     * @param params 검색 조건을 담은 Map (searchType, keyword, status 등)
     * @return 검색 조건에 해당하는 전체 사용자 수
     */
    int getTotalUserCount(Map<String, Object> params);
    
    /**
     * 검색 및 페이징 조건에 따라 사용자 목록을 조회합니다.
     * @param params 검색 및 페이징 조건을 담은 Map (searchType, keyword, status, startRow, endRow 등)
     * @return {@link AdminUserDTO} 객체 리스트
     */
    List<AdminUserDTO> getAllUsers(Map<String, Object> params);

    /**
     * (오버로드) 검색 및 필터 조건에 따라 회원 목록을 조회합니다.
     * <p>
     * 이 메소드는 페이징 정보 없이 검색 조건만으로 사용자 목록을 조회할 때 사용됩니다.
     * </p>
     * @param searchType 검색 유형 (예: "nickname", "email")
     * @param keyword 검색어
     * @param status 사용자 상태 (예: "ACTIVE", "DELETED", "BANNED")
     * @return {@link AdminUserDTO} 객체 리스트
     */
    List<AdminUserDTO> getAllUsers(
        @Param("searchType") String searchType,
        @Param("keyword") String keyword,
        @Param("status") String status
    );
    
    /**
     * 특정 사용자의 상태를 업데이트합니다.
     * <p>
     * 사용자 정지 또는 복구 시 사용됩니다.
     * </p>
     * @param userId 상태를 변경할 사용자의 고유 ID
     * @param statusId 변경할 상태 ID (예: 활성, 정지, 탈퇴 등)
     */
    void updateUserStatus(@Param("userId") int userId, @Param("statusId") int statusId);
    
    /**
     * 사용자 정지 로그를 기록합니다.
     * <p>
     * 사용자가 정지될 때 정지 사유, 기간 등을 기록합니다.
     * </p>
     * @param userId 정지된 사용자의 고유 ID
     * @param adminId 정지 조치를 수행한 관리자의 고유 ID
     * @param reason 정지 사유
     * @param duration 정지 기간 (일 단위)
     */
    void insertSuspendLog(@Param("userId") int userId, @Param("adminId") int adminId, @Param("reason") String reason, @Param("duration") int duration);
    
    /**
     * 정지된 사용자 목록을 조회합니다.
     * @return {@link suspendedUserDTO} 객체 리스트
     */
    List<suspendedUserDTO> getSuspendedUserList();

    /**
     * 특정 사용자의 정지 로그를 삭제합니다.
     * <p>
     * 사용자가 복구될 때 해당 정지 로그를 삭제하는 데 사용될 수 있습니다.
     * </p>
     * @param userId 정지 로그를 삭제할 사용자의 고유 ID
     */
    void deleteSuspendLog(@Param("userId") int userId);
}
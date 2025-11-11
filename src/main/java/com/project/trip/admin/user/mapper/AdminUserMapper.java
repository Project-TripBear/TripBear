// 파일 경로: com.project.trip.admin.user.mapper.AdminUserMapper.java (Legacy 구조 참고)

package com.project.trip.admin.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.project.trip.admin.user.model.suspendedUserDTO;
import com.project.trip.admin.user.model.AdminUserDTO;

public interface AdminUserMapper {
    
	// 1. ★★★ [추가] 검색 조건에 맞는 전체 회원 수를 세는 메서드
    int getTotalUserCount(Map<String, Object> params);
    
    // 2. ★★★ [수정] 기존 getAllUsers 메서드에 페이징 파라미터(startRow, endRow) 추가
    //    (Service에서 Map으로 묶어 전달할 예정)
    List<AdminUserDTO> getAllUsers(Map<String, Object> params);

    // 1. 검색 및 필터 조건에 따라 회원 목록을 조회합니다.
    List<AdminUserDTO> getAllUsers(
        @Param("searchType") String searchType,
        @Param("keyword") String keyword,
        @Param("status") String status
    );
    
 // 2. ★★★ [추가] 회원 상태 업데이트 (정지/복구 공용) ★★★
    void updateUserStatus(@Param("userId") int userId, @Param("statusId") int statusId);
    
    // 3. ★★★ [추가] 회원 정지 로그 기록 ★★★
    void insertSuspendLog(@Param("userId") int userId, @Param("adminId") int adminId, @Param("reason") String reason, @Param("duration") int duration);
    
    // 4. ★★★ [추가] 정지된 회원 목록 조회 ★★★
    List<suspendedUserDTO> getSuspendedUserList();

    void deleteSuspendLog(@Param("userId") int userId);
}
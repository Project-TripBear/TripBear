// 파일 경로: com.project.trip.admin.user.service.AdminUserService.java

package com.project.trip.admin.user.service;

import java.util.List;

import com.project.trip.admin.board.model.PagingDTO;
import com.project.trip.admin.user.model.suspendedUserDTO;
import com.project.trip.admin.user.model.AdminUserDTO;

public interface AdminUserService {
    
    // 1. 회원 목록을 조회합니다.
	int getTotalUserCount(String searchType, String keyword, String status);
    List<AdminUserDTO> getUserList(String searchType, String keyword, String status, PagingDTO paging);
    
    // 2. ★★★ [수정] adminId 파라미터 추가 ★★★
    void processSuspend(int userId, int adminId, String reason, int duration);
    
    // 3. ★★★ [추가] 정지된 회원 목록 조회 ★★★
    List<suspendedUserDTO> getSuspendedUserList();
    
    void restoreUser(int userId);
}
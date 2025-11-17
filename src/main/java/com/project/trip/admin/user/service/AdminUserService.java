// 파일 경로: com.project.trip.admin.user.service.AdminUserService.java

package com.project.trip.admin.user.service;

import java.util.List;

import com.project.trip.admin.board.model.PagingDTO;
import com.project.trip.admin.user.model.suspendedUserDTO;
import com.project.trip.admin.user.model.AdminUserDTO;

/**
 * 관리자 페이지의 사용자 관리와 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface AdminUserService {
    
    /**
     * 검색 조건에 맞는 전체 사용자 수를 조회합니다.
     * @param searchType 검색 유형 (예: "nickname", "email")
     * @param keyword 검색어
     * @param status 사용자 상태 (예: "ACTIVE", "DELETED", "BANNED")
     * @return 검색 조건에 해당하는 전체 사용자 수
     */
	int getTotalUserCount(String searchType, String keyword, String status);
    
    /**
     * 검색 및 페이징 조건에 따라 사용자 목록을 조회합니다.
     * @param searchType 검색 유형
     * @param keyword 검색어
     * @param status 사용자 상태
     * @param paging 페이징 정보를 담은 {@link PagingDTO} 객체
     * @return {@link AdminUserDTO} 객체 리스트
     */
    List<AdminUserDTO> getUserList(String searchType, String keyword, String status, PagingDTO paging);
    
    /**
     * 특정 사용자를 정지 처리합니다.
     * <p>
     * 사용자의 상태를 변경하고, 정지 로그를 기록합니다.
     * </p>
     * @param userId 정지할 사용자의 고유 ID
     * @param adminId 정지 조치를 수행한 관리자의 고유 ID
     * @param reason 정지 사유
     * @param duration 정지 기간 (일 단위)
     */
    void processSuspend(int userId, int adminId, String reason, int duration);
    
    /**
     * 정지된 사용자 목록을 조회합니다.
     * @return {@link suspendedUserDTO} 객체 리스트
     */
    List<suspendedUserDTO> getSuspendedUserList();
    
    /**
     * 특정 사용자를 복구 처리합니다.
     * <p>
     * 사용자의 상태를 활성으로 변경하고, 관련 정지 로그를 삭제합니다.
     * </p>
     * @param userId 복구할 사용자의 고유 ID
     */
    void restoreUser(int userId);
}
// 파일 경로: com.project.trip.admin.user.service.AdminUserServiceImpl.java

package com.project.trip.admin.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.admin.board.model.PagingDTO;
import com.project.trip.admin.user.mapper.AdminUserMapper;
import com.project.trip.admin.user.model.suspendedUserDTO;
// import com.project.trip.admin.user.model.suspendedUserDTO; // 1. 중복 임포트 제거
import com.project.trip.admin.user.model.AdminUserDTO;

/**
 * {@link AdminUserService} 인터페이스의 구현 클래스입니다.
 * <p>
 * {@link AdminUserMapper}를 통해 데이터베이스와 연동하여 관리자 페이지의 사용자 관리
 * (목록 조회, 정지, 복구 등) 관련 비즈니스 로직을 처리합니다.
 * </p>
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {
    
    @Autowired
    private AdminUserMapper mapper;

    // 3. 페이징 없는 getUserList 메서드 삭제 (컨트롤러와 동일하게)
    /*
    @Override
    public List<userDTO> getUserList(String searchType, String keyword, String status) {
        // ... (삭제) ...
    }
    */

    /**
     * {@inheritDoc}
     * <p>
     * 사용자의 상태를 '정지'로 변경하고, 정지 사유와 기간을 포함한 정지 로그를 기록합니다.
     * 이 작업은 트랜잭션으로 묶여 있어 두 작업 중 하나라도 실패하면 롤백됩니다.
     * </p>
     * @param userId 정지할 사용자의 고유 ID
     * @param adminId 정지 조치를 수행한 관리자의 고유 ID
     * @param reason 정지 사유
     * @param duration 정지 기간 (일 단위)
     */
    @Override
    @Transactional
    public void processSuspend(int userId, int adminId, String reason, int duration) {
        
        // 1. tblUser 테이블의 상태를 '정지'(user_status_id = 2)로 변경
        // (2는 '정지' 상태 ID라고 가정)
        mapper.updateUserStatus(userId, 2); 
        
        // 2. 정지 로그를 기록 (legacy 파일 참고)
        mapper.insertSuspendLog(userId, adminId, reason, duration);
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * 현재 정지 상태인 사용자들의 목록을 조회하여 반환합니다.
     * </p>
     * @return 정지된 사용자 정보({@link suspendedUserDTO})를 담은 List 객체
     */
    @Override
    public List<suspendedUserDTO> getSuspendedUserList() {
        return mapper.getSuspendedUserList();
    }
    
    // 2. 클래스 조기 종료 오류 수정 (여기 있던 '}' 제거)
    
    /**
     * {@inheritDoc}
     * <p>
     * 검색 조건(검색 유형, 키워드, 상태)에 따라 전체 사용자 수를 조회합니다.
     * </p>
     * @param searchType 검색 유형
     * @param keyword 검색어
     * @param status 사용자 상태
     * @return 검색 조건에 해당하는 전체 사용자 수
     */
    @Override
    public int getTotalUserCount(String searchType, String keyword, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("keyword", keyword);
        params.put("status", status);
        return mapper.getTotalUserCount(params);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 검색 조건(검색 유형, 키워드, 상태)과 페이징 정보에 따라 사용자 목록을 조회합니다.
     * </p>
     * @param searchType 검색 유형
     * @param keyword 검색어
     * @param status 사용자 상태
     * @param paging 페이징 정보를 담은 {@link PagingDTO} 객체
     * @return {@link AdminUserDTO} 객체 리스트
     */
    @Override
    public List<AdminUserDTO> getUserList(String searchType, String keyword, String status, PagingDTO paging) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("keyword", keyword);
        params.put("status", status);
        params.put("startRow", paging.getStartRow());
        params.put("endRow", paging.getEndRow());
        
        return mapper.getAllUsers(params);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 정지된 사용자를 '활동중' 상태로 복구하고, 해당 사용자의 정지 로그를 삭제합니다.
     * 이 작업은 트랜잭션으로 묶여 있어 두 작업 중 하나라도 실패하면 롤백됩니다.
     * </p>
     * @param userId 복구할 사용자의 고유 ID
     */
    @Override
    @Transactional
    public void restoreUser(int userId) {
        // 1. tblUser 테이블의 상태를 '활동중'(user_status_id = 1)로 변경
        mapper.updateUserStatus(userId, 1); 
        
        // 2. tblMemSuspended 테이블에서 정지 기록 삭제
        mapper.deleteSuspendLog(userId);
    }
    
} // <-- 클래스의 올바른 끝
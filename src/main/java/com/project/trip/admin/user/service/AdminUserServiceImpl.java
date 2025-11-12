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

    @Override
    @Transactional
    public void processSuspend(int userId, int adminId, String reason, int duration) {
        
        // 1. tblUser 테이블의 상태를 '정지'(user_status_id = 2)로 변경
        // (2는 '정지' 상태 ID라고 가정)
        mapper.updateUserStatus(userId, 2); 
        
        // 2. 정지 로그를 기록 (legacy 파일 참고)
        mapper.insertSuspendLog(userId, adminId, reason, duration);
    }
    
    @Override
    public List<suspendedUserDTO> getSuspendedUserList() {
        return mapper.getSuspendedUserList();
    }
    
    // 2. 클래스 조기 종료 오류 수정 (여기 있던 '}' 제거)
    
    @Override
    public int getTotalUserCount(String searchType, String keyword, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("keyword", keyword);
        params.put("status", status);
        return mapper.getTotalUserCount(params);
    }

    // 페이징 처리 구현 (유지)
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
    @Override
    @Transactional
    public void restoreUser(int userId) {
        // 1. tblUser 테이블의 상태를 '활동중'(user_status_id = 1)로 변경
        mapper.updateUserStatus(userId, 1); 
        
        // 2. tblMemSuspended 테이블에서 정지 기록 삭제
        mapper.deleteSuspendLog(userId);
    }
    
} // <-- 클래스의 올바른 끝
// 파일 경로: com.project.trip.admin.main.service.AdminMainServiceImpl.java

package com.project.trip.admin.main.service;

import java.util.HashMap;
import java.util.List; // 추가
import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.trip.admin.main.mapper.AdminMainMapper;
import com.project.trip.admin.report.model.reportDTO; // 추가

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMainServiceImpl implements AdminMainService {

    private final AdminMainMapper mapper;

    @Override
    public Map<String, Integer> getDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();
        
        // Mapper를 통해 DB에서 데이터 조회
        stats.put("totalMembers", mapper.getTotalMembers());
        stats.put("newMembersToday", mapper.getNewMembersToday());
        stats.put("pendingReportsCount", mapper.getPendingReportsCount());
        stats.put("newAccomCarCountToday", mapper.getNewAccomCarCountToday());
        
        // (금일 방문자 수는 DB 구현이 없으므로 임시 값 사용)
        // 실제 방문자 수는 Google Analytics 같은 외부 툴 연동 필요
        stats.put("visitorsToday", 1250); // 임시 값
        stats.put("visitorsYesterday", 1116); // 임시 값
        
        return stats;
    }

    // ★★★ [추가] 최신 신고 내역 구현 ★★★
    @Override
    public List<reportDTO> getLatestReports() {
        return mapper.getLatestReports();
    }
}
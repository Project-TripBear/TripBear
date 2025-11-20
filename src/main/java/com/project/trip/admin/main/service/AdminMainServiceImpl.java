// 파일 경로: com.project.trip.admin.main.service.AdminMainServiceImpl.java

package com.project.trip.admin.main.service;

import java.util.HashMap;
import java.util.List; // 추가
import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.trip.admin.main.mapper.AdminMainMapper;
import com.project.trip.admin.report.model.reportDTO; // 추가

import lombok.RequiredArgsConstructor;

/**
 * {@link AdminMainService}의 구현 클래스입니다.
 * <p>
 * 데이터베이스 접근을 위해 {@link AdminMainMapper}를 사용하여
 * 대시보드에 필요한 통계 데이터와 최신 신고 내역을 조회합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AdminMainServiceImpl implements AdminMainService {

    private final AdminMainMapper mapper;

    /**
     * {@inheritDoc}
     * <p>
     * 전체 회원 수, 오늘 가입한 신규 회원 수, 처리 대기 중인 신고 건수,
     * 오늘 등록된 신규 숙소/차량 수를 데이터베이스에서 조회하여 Map으로 반환합니다.
     * 방문자 수와 같은 일부 데이터는 현재 임시 값을 사용하고 있습니다.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<reportDTO> getLatestReports() {
        return mapper.getLatestReports();
    }
}
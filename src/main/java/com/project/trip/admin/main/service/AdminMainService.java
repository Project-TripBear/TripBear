// 파일 경로: com.project.trip.admin.main.service.AdminMainService.java

package com.project.trip.admin.main.service;

import java.util.List;
import java.util.Map;

import com.project.trip.admin.report.model.reportDTO; // 추가

public interface AdminMainService {
    
    // 대시보드에 필요한 모든 핵심 통계 지표를 가져오는 메서드
    Map<String, Integer> getDashboardStats();

    // ★★★ [추가] 최신 신고 내역을 가져오는 메서드 ★★★
    List<reportDTO> getLatestReports();
}
// 파일 경로: com.project.trip.admin.report.service.AdminReportService.java

package com.project.trip.admin.report.service;

import java.util.List;
import com.project.trip.admin.report.model.reportDTO;

public interface AdminReportService {
    
    // 1. 대기 중 신고 목록
    List<reportDTO> getPendingReports();
    
    // 2. 처리 내역 목록
    List<reportDTO> getProcessedReports();
    
    // 3. 신고 처리 (숨김 또는 반려) - 트랜잭션 필요
    void processReport(int reportId, String action, String targetType, int targetId);
}
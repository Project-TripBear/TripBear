// 파일 경로: com.project.trip.admin.report.service.AdminReportServiceImpl.java

package com.project.trip.admin.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.admin.report.mapper.AdminReportMapper;
import com.project.trip.admin.report.model.reportDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {

    private final AdminReportMapper mapper;

    public List<reportDTO> getPendingReports() {
        return mapper.getPendingReports();
    }

   
    public List<reportDTO> getProcessedReports() {
        return mapper.getProcessedReports();
    }

    /**
     * 신고 처리 (숨김 또는 반려)
     * - 숨김(approve) 시에는 해당 게시글을 숨기고(hidePost) 신고 상태를 승인(APPROVED)으로 변경합니다.
     * - 반려(reject) 시에는 신고 상태만 반려(REJECTED)로 변경합니다.
     */
    @Override
    @Transactional
    public void processReport(int reportId, String action, String targetType, int targetId) {
        
        if ("approve".equals(action)) {
            // 1. 게시글 숨김 처리 (DAO의 hidePost 역할)
            mapper.hidePost(targetType, targetId);
            
            // 2. 신고 상태를 승인(APPROVED)으로 변경 (DAO의 updateReportStatus 역할)
            mapper.updateReportStatus(reportId, "APPROVED");
            
        } else if ("reject".equals(action)) {
            // 1. 신고 상태를 반려(REJECTED)로 변경
            mapper.updateReportStatus(reportId, "REJECTED");
        }
    }
}
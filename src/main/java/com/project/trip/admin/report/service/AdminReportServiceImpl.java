// 파일 경로: com.project.trip.admin.report.service.AdminReportServiceImpl.java

package com.project.trip.admin.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.admin.report.mapper.AdminReportMapper;
import com.project.trip.admin.report.model.reportDTO;

import lombok.RequiredArgsConstructor;

/**
 * {@link AdminReportService} 인터페이스의 구현 클래스입니다.
 * <p>
 * {@link AdminReportMapper}를 통해 데이터베이스와 연동하여 관리자 페이지의 신고 관리
 * (목록 조회, 신고 처리 등) 관련 비즈니스 로직을 처리합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {

    private final AdminReportMapper mapper;

    /**
     * {@inheritDoc}
     */
    public List<reportDTO> getPendingReports() {
        return mapper.getPendingReports();
    }

    /**
     * {@inheritDoc}
     */
    public List<reportDTO> getProcessedReports() {
        return mapper.getProcessedReports();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * 'approve'(승인) 액션의 경우, 신고된 게시글을 숨김 처리하고 신고 상태를 'APPROVED'로 변경합니다.
     * 'reject'(반려) 액션의 경우, 신고 상태만 'REJECTED'로 변경합니다.
     * </p>
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
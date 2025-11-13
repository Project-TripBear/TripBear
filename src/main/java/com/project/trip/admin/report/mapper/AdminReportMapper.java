// 파일 경로: com.project.trip.admin.report.mapper.AdminReportMapper.java

package com.project.trip.admin.report.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.project.trip.admin.report.model.reportDTO;

// 이 인터페이스는 root-context.xml의 <mybatis-spring:scan>에 의해 DAO 역할을 수행합니다.
public interface AdminReportMapper {

    // 1. 대기 중인 신고 목록 조회 (reportList.java의 getPendingReports 대체)
    List<reportDTO> getPendingReports();

    // 2. 처리 완료된 신고 목록 조회 (reportHistory.java의 getProcessedReports 대체)
    List<reportDTO> getProcessedReports();
    
    // 3. 게시글 숨김 처리 (processReport.java의 hidePost 대체)
    // hidePost(targetType, targetId)
    void hidePost(@Param("targetType") String targetType, @Param("targetId") int targetId);
    
    // 4. 신고 상태 업데이트 (processReport.java의 updateReportStatus 대체)
    // updateReportStatus(reportId, status)
    void updateReportStatus(@Param("reportId") int reportId, @Param("reportStatus") String reportStatus);
}
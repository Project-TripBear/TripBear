// 파일 경로: com.project.trip.admin.report.mapper.AdminReportMapper.java

package com.project.trip.admin.report.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.project.trip.admin.report.model.reportDTO;

/**
 * 관리자 페이지의 신고 관리와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 신고 목록 조회, 게시글 숨김 처리, 신고 상태 업데이트 등 신고 관리를 위한
 * SQL 쿼리 호출을 정의합니다.
 * </p>
 */
// 이 인터페이스는 root-context.xml의 <mybatis-spring:scan>에 의해 DAO 역할을 수행합니다.
public interface AdminReportMapper {

    /**
     * 처리 대기 중인 신고 목록을 조회합니다.
     * @return 처리 대기 중인 신고 정보({@link reportDTO})를 담은 List 객체
     */
    List<reportDTO> getPendingReports();

    /**
     * 처리가 완료된 신고 목록을 조회합니다.
     * @return 처리 완료된 신고 정보({@link reportDTO})를 담은 List 객체
     */
    List<reportDTO> getProcessedReports();
    
    /**
     * 신고된 게시글을 숨김(비공개) 처리합니다.
     * @param targetType 신고 대상 유형 (예: "findboard", "review")
     * @param targetId 신고 대상의 고유 ID
     */
    void hidePost(@Param("targetType") String targetType, @Param("targetId") int targetId);
    
    /**
     * 신고 처리 상태를 업데이트합니다.
     * @param reportId 상태를 변경할 신고의 고유 ID
     * @param reportStatus 변경할 상태 (예: "PROCESSED", "REJECTED")
     */
    void updateReportStatus(@Param("reportId") int reportId, @Param("reportStatus") String reportStatus);
}
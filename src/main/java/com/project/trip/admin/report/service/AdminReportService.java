
	// 파일 경로: com.project.trip.admin.report.service.AdminReportService.java
	
	package com.project.trip.admin.report.service;
	
	import java.util.List;
	import com.project.trip.admin.report.model.reportDTO;
	
	/**
	 * 관리자 페이지의 신고 관리와 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
	 */
	public interface AdminReportService {
	    
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
	     * 신고를 처리합니다. (게시글 숨김 또는 신고 반려)
	     * <p>
	     * 이 작업은 신고 상태 업데이트와 게시글 상태 변경 등 여러 DB 작업을 포함할 수 있으므로
	     * 단일 트랜잭션으로 처리되어야 합니다.
	     * </p>
	     * @param reportId 처리할 신고의 고유 ID
	     * @param action 처리 내용 (예: "hide" - 숨김, "reject" - 반려)
	     * @param targetType 신고 대상 유형 (예: "findboard", "review")
	     * @param targetId 신고 대상의 고유 ID
	     */
	    void processReport(int reportId, String action, String targetType, int targetId);
	}

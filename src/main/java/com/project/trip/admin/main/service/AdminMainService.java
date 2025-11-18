// 파일 경로: com.project.trip.admin.main.service.AdminMainService.java

package com.project.trip.admin.main.service;

import java.util.List;
import java.util.Map;

import com.project.trip.admin.report.model.reportDTO; // 추가

/**
 * 관리자 메인 대시보드 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface AdminMainService {
    
    /**
     * 대시보드에 필요한 모든 핵심 통계 지표를 조회합니다.
     * <p>
     * 예: 신규 회원 수, 총 예약 수, 오늘 발생한 매출 등 다양한 통계 데이터를
     * Map 형태로 반환합니다.
     * </p>
     * @return 각 통계 항목의 이름(key)과 값(value)을 담은 Map 객체
     */
    Map<String, Integer> getDashboardStats();

    /**
     * 최신 신고 내역 목록을 조회합니다.
     * <p>
     * 대시보드에 표시할 최근 신고 내역을 시간순으로 정렬하여 반환합니다.
     * </p>
     * @return 최신 신고 정보({@link reportDTO})를 담은 List 객체
     */
    List<reportDTO> getLatestReports();
}
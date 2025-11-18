package com.project.trip.admin.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.trip.admin.report.model.reportDTO;

/**
 * 관리자 페이지 메인 대시보드와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 대시보드에 표시될 각종 통계 데이터(회원 수, 예약 수, 신고 건수 등) 및
 * 최신 신고 내역 조회 기능을 위한 SQL 쿼리 호출을 정의합니다.
 * </p>
 */
@Mapper // Spring 3.x 이상에서는 @Mapper 사용 가능하나, 레거시 환경을 위해 @Repository와 XML 설정을 유지합니다.
public interface AdminMainMapper {

    // 1. 총 회원 수 조회 (대시보드 메인)
    int getTotalMembers();
    
    // 2. 금일 신규 회원 수 조회
    int getNewMembersToday();
    
    // 3. 현재 처리 대기 중인 신고 건수
    int getPendingReportsCount();
    
    // 4. 금일 신규 등록 숙소/렌터카 수 (편의상 통합)
    int getNewAccomCarCountToday();

	/**
	 * 최신 신고 내역 목록을 조회합니다.
	 *
	 * @return 최신 신고 내역 {@link reportDTO} 객체 리스트
	 */
	List<reportDTO> getLatestReports();
}
package com.project.trip.admin.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.trip.admin.report.model.reportDTO;

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

	List<reportDTO> getLatestReports();
}
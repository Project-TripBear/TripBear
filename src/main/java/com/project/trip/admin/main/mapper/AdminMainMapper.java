package com.project.trip.admin.main.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper // (또는 mybatis-spring:scan에 추가)
public interface AdminMainMapper {

    // 1. 전체 회원 수
    int getTotalMembers();

    // 2. 오늘 등록된 예약 건수 (tblReservation)
    int getTodayReservations();
    
    // 3. 처리 대기 중인 신고 건수 (tblReports)
    int getPendingReports();
    
}
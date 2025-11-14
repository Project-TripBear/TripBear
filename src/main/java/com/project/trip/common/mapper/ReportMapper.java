package com.project.trip.common.mapper; // (공용 패키지로)

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper {
    // 신고 내역을 tblReports에 삽입하는 공통 쿼리
    int addReport(Map<String, Object> params);
}
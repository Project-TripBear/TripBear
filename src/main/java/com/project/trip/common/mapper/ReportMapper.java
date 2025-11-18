package com.project.trip.common.mapper; // (공용 패키지로)

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 공통 신고 기능과 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 다양한 게시글 및 댓글 신고 기능을 위한 공통 SQL 쿼리 호출을 정의합니다.
 * </p>
 */
@Mapper
public interface ReportMapper {
    // 신고 내역을 tblReports에 삽입하는 공통 쿼리
    /**
     * 신고 내역을 `tblReports` 테이블에 삽입합니다.
     *
     * @param params 신고 정보를 담은 {@code Map<String, Object>} (예: boardSeq, reporterId, reportedUserId, reason)
     * @return 삽입된 행의 수
     */
    int addReport(Map<String, Object> params);
}
package com.project.trip.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * DB 연결 테스트 전용 Mapper
 */
public interface TestMapper {

    // 단순 DB 시간 확인 (SELECT SYSDATE)
    @Select("SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') FROM dual")
    String getTime();

    // 단순 INSERT 테스트 (테이블 이름은 임시용)
    // tblTest(id, msg) 테이블에 데이터 추가한다고 가정
    @Select("INSERT INTO tblTest (id, msg) VALUES (seqTest.nextval, 'JUnit Insert Test')")
    void insertDummy();
}

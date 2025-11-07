package com.project.trip.mapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Oracle Cloud + MyBatis 연결 테스트
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml" // 현재 클라우드 DB 설정 파일
})
public class TestMapperTest {

    @Autowired
    private TestMapper mapper;

    @Test
    public void testConnection() {
        try {
            String now = mapper.getTime();
            System.out.println("✅ Oracle Cloud 연결 성공! 현재 시간: " + now);
            assertNotNull(now);
        } catch (Exception e) {
            e.printStackTrace();
            fail("❌ Oracle Cloud 연결 실패: " + e.getMessage());
        }
    }
}

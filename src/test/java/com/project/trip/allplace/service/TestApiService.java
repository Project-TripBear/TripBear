package com.project.trip.allplace.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.allplace.model.PlaceDTO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration // (컨트롤러 테스트는 아니지만, WebApplicationContext를 로드하기 위해)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" 
    // (MyBatis <scan>이나 @Service <scan>이 분리되어 있다면 둘 다 로드)
})
@Log4j
@Transactional
public class TestApiService {

	@Autowired
	private AllPlaceService allPlaceService;
	
	@Autowired
    private DataSource dataSource; // DB 커넥션 풀 주입
	
	@Test
    public void testDatabaseConnection() {
        // dataSource 자체가 주입되었는지 확인
        assertNotNull("DataSource가 null입니다. root-context.xml 설정을 확인하세요.", dataSource);
        System.out.println(">>> DataSource Bean Injection Success! <<<");
        System.out.println(dataSource);

        // 실제 DB 커넥션을 가져와 봅니다. (가장 중요)
        try (Connection conn = dataSource.getConnection()) {
            assertNotNull("DB 커넥션 가져오기 실패", conn);
            System.out.println(">>> DB Connection Success! <<<");
            System.out.println(conn);
        } catch (Exception e) {
            e.printStackTrace();
            fail("DB 연결 실패 (Wallet 경로/권한 문제 또는 DB 설정 오류): " + e.getMessage());
        }
    }
	
	private final String TEST_CONTENT_ID = "126508";
	
	@Test
    public void testAddPlaceOnDemand() {
        log.info("--- On-Demand 테스트 시작 ---");
        
        
		log.info("[테스트 1] 최초 등록 시도...");
	    
	    // (AllPlaceServiceImpl의 System.out.println 로그를 주목하세요)
	    PlaceDTO firstCallResult = allPlaceService.addPlaceOnDemand(TEST_CONTENT_ID);
	
	    // [검증 1]
	    assertNotNull("최초 호출 시 DTO는 null이면 안 됩니다.", firstCallResult);
	    assertEquals("API에서 가져온 이름이 '롯데월드'여야 합니다.", "롯데월드", firstCallResult.getName());
	    assertNotNull("최초 등록 시 placeId가 생성되어야 합니다.", firstCallResult.getPlaceId());
	
	    long firstPlaceId = firstCallResult.getPlaceId();
	    log.info("[테스트 1] 성공! placeId: " + firstPlaceId + ", 이름: " + firstCallResult.getName());
	
	
	    // ----------------------------------------------------
	    // [시나리오 2] '롯데월드(126508)'를 다시 등록 (DB에 이미 있음)
	    // ----------------------------------------------------
	    log.info("[테스트 2] 동일한 장소 재호출 시도...");
	    
	    // (AllPlaceServiceImpl의 System.out.println 로그를 주목하세요)
	    // (이번에는 "DB에 이미 존재함" 로그가 찍혀야 합니다)
	    PlaceDTO secondCallResult = allPlaceService.addPlaceOnDemand(TEST_CONTENT_ID);
	
	    // [검증 2]
	    assertNotNull("재호출 시에도 DTO는 null이면 안 됩니다.", secondCallResult);
	    assertEquals("DB에서 가져온 이름도 '롯데월드'여야 합니다.", "롯데월드", secondCallResult.getName());
	    
	    // [핵심 검증] 
	    // 1번의 placeId와 2번의 placeId가 동일해야 합니다. (새로 INSERT한 게 아니므로)
	    assertEquals("재호출 시 placeId는 최초 등록 시의 ID와 동일해야 합니다.", 
	                 firstPlaceId, secondCallResult.getPlaceId());
	
	    log.info("[테스트 2] 성공! placeId: " + secondCallResult.getPlaceId() + " (최초 ID와 동일함)");
	    log.info("--- On-Demand 테스트 통과! ---");
	}
	
}

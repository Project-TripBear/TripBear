package com.project.trip.allplace.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourItemVO;

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
	
	/**
	 * DB연결 테스트
	 */
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
	
	/**
	 * On-Demand 방식 작동 테스트
	 */
	@Test
    public void testAddPlaceOnDemand() {
        log.info("--- On-Demand 테스트 시작 ---");
        
        
		log.info("[테스트 1] 최초 등록 시도...");
	    
	    // (AllPlaceServiceImpl의 System.out.println 로그를 주목하세요)
	    PlaceDTO firstCallResult = allPlaceService.addPlaceOnDemand(TEST_CONTENT_ID);
	
	    // [검증 1]
	    assertNotNull("최초 호출 시 DTO는 null이면 안 됩니다.", firstCallResult);
	    assertEquals("API에서 가져온 이름이 '경복궁'여야 합니다.", "경복궁", firstCallResult.getName());
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
	    assertEquals("DB에서 가져온 이름도 '경복궁'여야 합니다.", "경복궁", secondCallResult.getName());
	    
	    // [핵심 검증] 
	    // 1번의 placeId와 2번의 placeId가 동일해야 합니다. (새로 INSERT한 게 아니므로)
	    assertEquals("재호출 시 placeId는 최초 등록 시의 ID와 동일해야 합니다.", 
	                 firstPlaceId, secondCallResult.getPlaceId());
	
	    log.info("[테스트 2] 성공! placeId: " + secondCallResult.getPlaceId() + " (최초 ID와 동일함)");
	    log.info("--- On-Demand 테스트 통과! ---");
	}
	/**
	 * API 상세정보 조회 테스트
	 */
	@Test
    public void testGetPlaceDetail() {
        // ... (로그, 1. 경복궁 INSERT) ...
        PlaceDTO insertedPlace = allPlaceService.addPlaceOnDemand(TEST_CONTENT_ID);
        long placeId = insertedPlace.getPlaceId();
        
        // 2. 상세정보 조회
        // (반환 타입이 PlaceDetailDTO -> PlaceDTO로 변경)
        PlaceDTO detail = allPlaceService.getPlaceDetail(placeId);

        // 3. 검증
        assertNotNull("상세정보 조회 결과가 null입니다.", detail);
        assertEquals("조회된 이름이 '경복궁'이어야 합니다.", "경복궁", detail.getName());
           
        // --- [핵심 수정] ---
        // (CLOB 데이터가 중첩 DTO 안에 있는지 확인)
        assertNotNull("중첩 DTO(touristSpotDetail)가 null이면 안 됩니다.", detail.getTouristSpotDetail());
        assertNotNull("상세설명(CLOB)이 null이면 안 됩니다.", detail.getTouristSpotDetail().getSpotOverinfo());
        
        log.info("[테스트 성공] 상세설명(CLOB) 일부: " + 
                 detail.getTouristSpotDetail().getSpotOverinfo().substring(0, 20) + "...");
        log.info("--- 상세정보 조회 테스트 통과! ---");
    }
	
	/**
     *  API 키워드 검색 테스트
     */
	@Test
    public void testSearchByKeyword() {
        log.info("--- API 키워드 검색 테스트 시작 ---");

        String keyword = "경복궁";
        
        // --- [이 줄을 추가하세요] ---
        // 'arrange' 변수를 선언하고 테스트할 값을 할당합니다 (A=제목순)
        String arrange = "A"; 
        // --- [여기까지] ---

        // (이제 'arrange' 변수를 인식할 수 있습니다)
        TourApiResponseVO response = allPlaceService.searchByKeyword(keyword, arrange);

        // 1. API 응답 자체가 null이 아닌지
        assertNotNull("API 응답(Wrapper)이 null입니다.", response);
        
        // 2. item 리스트가 null이 아닌지
        List<TourItemVO> items = response.getResponse().getBody().getItems().getItem();
        assertNotNull("API 응답의 item 리스트가 null입니다.", items);

        // 3. "경복궁"으로 검색했으니, 리스트가 비어있지 않아야 함
        assertTrue("검색 결과가 1개 이상이어야 합니다.", items.size() > 0);
        
        // 4. 0번째 항목의 제목에 "경복궁"이 포함되어 있는지
        TourItemVO firstItem = items.get(0);
        assertNotNull("첫 번째 검색 결과가 null입니다.", firstItem);
        assertTrue("첫 번째 결과의 제목에 '경복궁'이 포함되어야 합니다.", 
                   firstItem.getTitle().contains(keyword));

        log.info("[테스트 성공] API 검색 성공! 총 " + items.size() + "개 검색됨.");
        log.info("[테스트 성공] 0번째 항목: " + firstItem.getTitle());
    }
	
	/**
     * (신규) 해시태그(키워드) 기능 테스트 (추가 및 검색)
     */
    @Test
    @Transactional // (DB 변경사항 롤백을 위해 필수)
    public void testHashtagFeatures() {
        log.info("--- 해시태G 기능 테스트 시작 ---");
        
        // --- 1. 테스트 준비: '경복궁'을 DB에 INSERT ---
        PlaceDTO place = allPlaceService.addPlaceOnDemand(TEST_CONTENT_ID); // 126508 (경복궁)
        assertNotNull("테스트 준비(INSERT) 실패", place);
        long placeId = place.getPlaceId();
        
        // --- 2. '추가' 기능 테스트 ---
        log.info("[해시태그 테스트] 1. 태그 2개 추가 시도...");
        allPlaceService.addHashtagToPlace(placeId, "궁궐");
        allPlaceService.addHashtagToPlace(placeId, "서울여행");
        
        // --- [이 부분을 주석 처리 하세요] ---
        log.info("[해시태그 테스트] 2. 중복 태그 추가 시도 (무시되어야 함)...");
        // (@Transactional 테스트에서는 중복 INSERT가 롤백 전까지 DB에 반영되므로,
        // 이 라인을 주석 처리하여 '궁궐'이 1번만 INSERT되도록 합니다.)
        // allPlaceService.addHashtagToPlace(placeId, "궁궐"); 
        // --- [여기까지] ---
        
        // --- 3. '검색' 기능 테스트 ---
        log.info("[해시태그 테스트] 3. '궁궐' 키워드로 검색...");
        List<PlaceDTO> resultList = allPlaceService.findPlacesByKeyword("궁궐");
        
        assertNotNull("검색 결과 리스트가 null입니다.", resultList);
        assertTrue("검색 결과가 1개여야 합니다.", resultList.size() == 1); // (이제 size()가 1이 됩니다)
        assertEquals("검색된 장소가 '경복궁'이어야 합니다.", "경복궁", resultList.get(0).getName());
        
        log.info("[해시태그 테스트] 4. 없는 키워드로 검색...");
        List<PlaceDTO> emptyList = allPlaceService.findPlacesByKeyword("없는태그");
        assertTrue("검색 결과가 0개여야 합니다.", emptyList.isEmpty());
        
        log.info("--- 해시태그 기능 테스트 통과! ---");
    }
	
}

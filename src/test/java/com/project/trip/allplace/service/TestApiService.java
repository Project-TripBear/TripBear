package com.project.trip.allplace.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
@WebAppConfiguration 
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
    "file:src/main/webapp/WEB-INF/spring/security-context.xml"
})
@Log4j
@Transactional
public class TestApiService {

	@Autowired
	private AllPlaceService allPlaceService;
	
	@Autowired
    private DataSource dataSource; 
	
	// ... (testDatabaseConnection, testAddPlaceOnDemand_Spot, testGetPlaceDetail, testHashtagFeatures 메서드는 모두 동일) ...
	
	private final String TEST_CONTENT_ID = "126508";
	
	@Test
    public void testAddPlaceOnDemand_Spot() {
        log.info("--- On-Demand '관광지(12)' 테스트 시작 ---");
        TourApiResponseVO searchResponse = allPlaceService.searchByKeyword("경복궁", "A", "12");
        assertNotNull(searchResponse);
        TourItemVO itemToSave = searchResponse.getResponse().getBody().getItems().getItem().get(0);
        assertNotNull(itemToSave);
        
        PlaceDTO firstCallResult = allPlaceService.addPlaceOnDemand(itemToSave); 
	    assertNotNull("최초 호출 시 DTO는 null이면 안 됩니다.", firstCallResult);
	    log.info("[테스트 1] 성공!");
	
	    PlaceDTO secondCallResult = allPlaceService.addPlaceOnDemand(itemToSave); 
	    assertNotNull("재호출 시에도 DTO는 null이면 안 됩니다.", secondCallResult);
	    assertEquals("재호출 시 placeId는 최초 등록 시의 ID와 동일해야 합니다.", 
	                 firstCallResult.getPlaceId(), secondCallResult.getPlaceId());
	    log.info("--- On-Demand '관광지(12)' 테스트 통과! ---");
    }
    
	@Test
    public void testGetPlaceDetail() {
        log.info("--- 상세정보 조회 테스트 시작 ---");
        TourApiResponseVO searchResponse = allPlaceService.searchByKeyword("경복궁", "A", "12");
        TourItemVO itemToSave = searchResponse.getResponse().getBody().getItems().getItem().get(0);
        PlaceDTO insertedPlace = allPlaceService.addPlaceOnDemand(itemToSave);
        
        assertNotNull("INSERT 실패", insertedPlace);
        long placeId = insertedPlace.getPlaceId();
        log.info("[테스트 준비] 경복궁 INSERT 성공 (placeId: " + placeId + ")");
        
        PlaceDTO detail = allPlaceService.getPlaceDetail(placeId);
        assertNotNull("상세정보 조회 결과가 null입니다.", detail);
        log.info("--- 상세정보 조회 테스트 통과! ---");
    }
	
	@Test
    @Transactional 
    public void testHashtagFeatures() {
         log.info("--- 해시태그 기능 테스트 시작 ---");
        TourApiResponseVO searchResponse = allPlaceService.searchByKeyword("경복궁", "A", "12");
        TourItemVO itemToSave = searchResponse.getResponse().getBody().getItems().getItem().get(0);
        PlaceDTO place = allPlaceService.addPlaceOnDemand(itemToSave);
        assertNotNull("테스트 준비(INSERT) 실패", place);
        
        allPlaceService.addHashtagToPlace(place.getPlaceId(), "궁궐");
        List<PlaceDTO> resultList = allPlaceService.findPlacesByKeyword("궁궐");
        assertTrue("검색 결과가 1개여야 합니다.", resultList.size() == 1); 
        log.info("--- 해시태그 기능 테스트 통과! ---");
    }
    
    /**
     * (신규) 'searchFestival' API 테스트 (축제 ID 찾기용)
     */
    @Test
    public void testFindFestivalId() {
        log.info("--- (ID찾기) API 축제 검색 테스트 시작 ---");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7); 
        String targetDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        
        log.info("[ID찾기] 검색 기준 날짜: " + targetDate); 
        
        // --- [수정] ---
        // (arrange="A" 인자 추가)
        TourApiResponseVO response = allPlaceService.searchFestival(targetDate, "A");
        // --- [여기까지] ---
        assertNotNull("API 응답(Wrapper)이 null입니다.", response);
        
        if(response.getResponse().getBody().getItems() == null) {
            log.warn("[테스트] " + targetDate + " 날짜로 검색된 축제가 없습니다.");
            return;
        }
        
        List<TourItemVO> items = response.getResponse().getBody().getItems().getItem();
        if(items == null || items.isEmpty()) {
            log.warn("[테스트] " + targetDate + " 날짜로 검색된 축제가 없습니다.");
            return;
        }
        
        TourItemVO firstItem = items.get(0);
        log.info("--- [테스트 ID 찾기 성공] ---");
        log.info("▶ 테스트용 축제 ID: " + firstItem.getContentId());
        log.info("▶ 축제 이름: " + firstItem.getTitle());
        log.info("---------------------------------");
    }
    
    /**
     * (축제 15) 테스트
     */
    @Test
    @Transactional
    public void testAddPlaceOnDemand_Event() {
        log.info("--- On-Demand 축제/행사(15) 테스트 시작 ---");
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7); 
        String targetDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        
        // --- [수정] ---
        // (arrange="A" 인자 추가)
        TourApiResponseVO searchResponse = allPlaceService.searchFestival(targetDate, "A");
        // --- [여기까지] ---
        assertNotNull("축제 검색 API 실패 (API 응답이 null)", searchResponse);
        
        if (searchResponse.getResponse().getBody().getItems() == null) {
            fail(targetDate + " 기준 검색된 축제가 없어 테스트 실패 (items가 null)");
            return;
        }
        
        List<TourItemVO> items = searchResponse.getResponse().getBody().getItems().getItem();
        if (items == null || items.isEmpty()) {
            fail(targetDate + " 기준 검색된 축제가 없어 테스트 실패 (item 리스트가 비어있음)");
            return;
        }
        
        TourItemVO itemToSave = items.get(0);
        assertNotNull(itemToSave);
        log.info("[테스트 준비] 저장 대상 축제: " + itemToSave.getTitle() + " (ID: " + itemToSave.getContentId() + ")");
        
        PlaceDTO insertedPlace = allPlaceService.addPlaceOnDemand(itemToSave);
        assertNotNull("최초 호출 시 DTO는 null이면 안 됩니다.", insertedPlace);
        log.info("[테스트] tblPlace INSERT 성공! (placeId: " + insertedPlace.getPlaceId() + ")");
        
        log.info("--- On-Demand 축제/행사(15) 테스트 통과! ---");
    }
	

    /**
     * (음식점 39) 테스트
     */
    @Test
    @Transactional
    public void testAddPlaceOnDemand_Restaurant() {
        log.info("--- On-Demand '음식점(39)' 테스트 시작 ---");
        
        String keyword = "경기"; 
        
        TourApiResponseVO searchResponse = allPlaceService.searchByKeyword(keyword, "A", "39");
        assertNotNull("음식점 검색 API 실패 (API 응답이 null)", searchResponse);

        if (searchResponse.getResponse().getBody().getItems() == null) {
            fail("키워드 '" + keyword + "'으로 검색된 음식점이 없어 테스트 실패 (items가 null)");
            return;
        }
        
        List<TourItemVO> items = searchResponse.getResponse().getBody().getItems().getItem();
        if (items == null || items.isEmpty()) {
            fail("키워드 '" + keyword + "'으로 검색된 음식점이 없어 테스트 실패 (item 리스트가 비어있음)");
            return;
        }
        
        TourItemVO itemToSave = items.get(0); 
        assertNotNull(itemToSave);
        log.info("[테스트 준비] 저장 대상 음식점: " + itemToSave.getTitle() + " (ID: " + itemToSave.getContentId() + ")");
        
        
        PlaceDTO insertedPlace = allPlaceService.addPlaceOnDemand(itemToSave);
        
        assertNotNull("최초 호출 시 DTO는 null이면 안 됩니다.", insertedPlace);
        long newPlaceId = insertedPlace.getPlaceId();
        log.info("[테스트] tblPlace INSERT 성공! (placeId: " + newPlaceId + ")");

        log.info("[테스트 3] 동일한 음식점 재호출 시도...");
	    PlaceDTO secondCallResult = allPlaceService.addPlaceOnDemand(itemToSave);
        assertNotNull(secondCallResult);
        assertEquals("재호출 시 placeId는 최초 등록 시의 ID와 동일해야 합니다.", 
	                 newPlaceId, secondCallResult.getPlaceId());
        
        log.info("--- On-Demand '음식점(39)' 테스트 통과! ---");
    }
	
}
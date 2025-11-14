package com.project.trip.hotdeal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.project.trip.board.hotdeal.controller.HotDealController;
import com.project.trip.board.hotdeal.mapper.HotDealMapper;
import com.project.trip.board.hotdeal.model.HotDealDTO;
import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.model.UserDTO;

import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class HotDealTest {

    @Autowired
    private HotDealMapper mapper;
    private HotDealController controller;
    private SqlSessionFactory sqlSessionFactory;



//    /**
//     * 총 게시물 수를 가져오는 메서드 테스트.
//     * 검색 조건 없을 때 0 이상의 값이 반환되어야 한다.
//     */
//    @Test
//    public void testGetBoardTotalCountNoSearch() {
//        Map<String, String> map = new HashMap<>();
//        map.put("search", "n"); // 검색 없음
//
//        int count = mapper.getBoardTotalCount(map);
//        assertTrue("총 게시물 수는 0 이상이어야 합니다.", count >= 0);
//    }
//
//    /**
//     * 검색 조건이 있을 때 총 게시물 수 조회 테스트.
//     * 예를 들어 '블루투스'라는 키워드에 대해 count를 확인한다.
//     */
//    @Test
//    public void testGetBoardTotalCountWithSearch() {
//        Map<String, String> map = new HashMap<>();
//        map.put("search", "y");
//        map.put("column", "hotdeal_title");
//        map.put("word", "블루투스");
//
//        int count = mapper.getBoardTotalCount(map);
//        assertTrue("검색 결과 총 게시물 수는 0 이상이어야 합니다.", count >= 0);
//    }
//
//    /**
//     * 게시물 목록 조회 테스트.
//     * 검색 없는 상태에서 10개 이내의 게시물이 반환되는지 확인한다.
//     */
//    @Test
//    public void testListNoSearch() {
//        Map<String, String> map = new HashMap<>();
//        map.put("begin", "1");
//        map.put("end", "10");
//        map.put("search", "n");
//
//        List<HotDealDTO> list = mapper.list(map);
//        assertNotNull("리스트는 null이면 안됩니다.", list);
//        assertTrue("리스트 크기는 10 이하이어야 합니다.", list.size() <= 10);
//    }
//
//    /**
//     * 검색 조건이 있을 때 게시물 목록 조회 테스트.
//     */
//    @Test
//    public void testListWithSearch() {
//        Map<String, String> map = new HashMap<>();
//        map.put("begin", "1");
//        map.put("end", "10");
//        map.put("search", "y");
//        map.put("column", "hotdeal_title");
//        map.put("word", "블루투스");
//
//        List<HotDealDTO> list = mapper.list(map);
//        assertNotNull("검색된 게시물 리스트는 null이 아니어야 합니다.", list);
//    }
//    
    


    @Before
    public void setUp() throws Exception {
    	 String resource = "config/mybatis-config.xml"; // 리소스 경로. src/main/resources에 위치해야 함
    	    InputStream inputStream = Resources.getResourceAsStream(resource);
    	    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void testGetUserByUserId() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MemberMapper mapper = session.getMapper(MemberMapper.class);

            String testUserId = "test";
            UserDTO user = mapper.get(testUserId);

            assertNotNull("유저 조회 결과가 null이면 안됨", user);
            assertEquals("유저 ID가 일치해야 함", testUserId, user.getId());
            assertNotNull("유저 시퀀스가 null이면 안됨", user.getSeq());
        }
    }
    
    
}
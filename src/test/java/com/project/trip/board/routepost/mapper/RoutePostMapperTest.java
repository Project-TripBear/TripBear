// package com.project.trip.board.routepost.mapper;

// import static org.junit.Assert.*;

// import java.util.List;

// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// import com.project.trip.board.routepost.model.RoutePostDTO;
// import com.project.trip.board.routepost.model.RoutePostImageDTO;

// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = {
//     "file:src/main/webapp/WEB-INF/spring/root-context.xml"
// })
// public class RoutePostMapperTest {

//     @Autowired
//     private RoutePostMapper mapper;

//     // DB 연결 확인용
//     @Test
//     public void testConnection() {
//         assertNotNull(mapper);
//         System.out.println("✅ MyBatis Mapper 연결 성공!");
//     }

//     // 목록 조회 테스트
//     @Test
//     public void testList() {
//         List<RoutePostDTO> list = mapper.list();
//         assertNotNull(list);
//         System.out.println("✅ 게시글 목록 조회 성공 (" + list.size() + "건)");
//         list.forEach(System.out::println);
//     }

//     // 단일 조회 테스트
//     @Test
//     public void testGet() {
//         String testId = "999"; // 실제 DB에 있는 ID로 변경
//         RoutePostDTO dto = mapper.get(testId);
//         if (dto != null) {
//             System.out.println("✅ 게시글 상세 조회 성공: " + dto.getRoutepostTitle());
//             System.out.println(dto);
//         } else {
//             System.out.println("❌ 게시글 ID " + testId + " 없음");
//         }
//     }

//     // 게시글 추가 테스트 //확인완료
//     @Test
//     public void testAdd() {
//         RoutePostDTO dto = new RoutePostDTO();
//         dto.setRoutepostId("999");     // 시퀀스 안 쓰는 경우 직접 지정
//         dto.setUserId("41");            // tblUser에 실제 존재하는 user_id
//         dto.setRoutepostTitle("JUnit 게시글");
//         dto.setRoutepostContent("JUnit insert 테스트");
//         dto.setRoutepostSatisfaction("4.5");
//         dto.setRoutepostViewCount("0");
//         dto.setRoutepostReportCount("0");
//         dto.setRoutepostStatus("A");

//         int result = mapper.add(dto);
//         assertEquals(1, result);
//     }


//     // 게시글 수정 테스트
//     @Test
//     public void testEdit() {
//         RoutePostDTO dto = new RoutePostDTO();
//         dto.setRoutepostId("999"); // 위에서 등록한 ID 사용
//         dto.setRoutepostTitle("수정된 제목");
//         dto.setRoutepostContent("수정된 내용입니다.");
//         dto.setRoutepostSatisfaction("4");
//         dto.setRoutepostStatus("Y");

//         int result = mapper.edit(dto);
//         assertEquals(1, result);
//         System.out.println("✅ 게시글 수정 성공!");
//     }

//     // 이미지 등록 테스트
//     @Test
//     public void testAddImage() {
//         RoutePostImageDTO img = new RoutePostImageDTO();
//         img.setRoutepostImageId("999");
//         img.setRoutepostId("999");
//         img.setRoutepostImageSeq("1");
//         img.setRoutepostImageUrl("https://example.com/test.jpg");

//         int result = mapper.addImage(img);
//         assertEquals(1, result);
//         System.out.println("✅ 이미지 등록 성공!");
//     }

//     // 이미지 목록 조회 테스트
//     @Test
//     public void testGetImages() {
//         List<RoutePostImageDTO> imgs = mapper.getImages("RP999");
//         assertNotNull(imgs);
//         System.out.println("✅ 이미지 목록 조회 성공 (" + imgs.size() + "건)");
//         imgs.forEach(System.out::println);
//     }

//     // 이미지 삭제 테스트
//     @Test
//     public void testDelImages() {
//         int result = mapper.delImages("999");
//         assertTrue(result >= 0);
//         System.out.println("✅ 이미지 전체 삭제 성공!");
//     }

//     // 게시글 삭제 테스트
//     @Test
//     public void testDel() {
//         int result = mapper.del("999");
//         assertEquals(1, result);
//         System.out.println("✅ 게시글 삭제 성공!");
//     }
// }

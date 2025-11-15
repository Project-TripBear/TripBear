// package com.project.trip.board.routepost.mapper;

// import static org.junit.Assert.*;

// import java.util.List;

// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// import com.project.trip.board.routepost.model.RoutePostCommentDTO;

// /**
//  * RoutePostCommentMapper 단위 테스트
//  * DB 연결 + SQL 매핑 확인용
//  */
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = {
//     "file:src/main/webapp/WEB-INF/spring/root-context.xml"
// })
// public class RoutePostCommentMapperTest {

//     @Autowired
//     private RoutePostCommentMapper mapper;

//     // ✅ 1. Mapper 주입 확인
//     @Test
//     public void mapperExists() {
//         assertNotNull("RoutePostCommentMapper 주입 실패", mapper);
//         System.out.println("✅ Mapper 주입 성공: " + mapper.getClass().getName());
//     }

//     // ✅ 2. 댓글 목록 조회 테스트
//     @Test
//     public void testList() {
//         String routepostId = "1"; // 테스트용 게시글 ID
//         List<RoutePostCommentDTO> list = mapper.list(routepostId);
//         System.out.println("✅ 댓글 개수: " + list.size());
//         list.forEach(c -> System.out.println(
//                 "[" + c.getRoutepostCommentId() + "] " + c.getNickname() + ": " + c.getRoutepostContent()
//         ));
//     }

//     // ✅ 3. 댓글 등록 테스트
//     @Test
//     public void testAdd() {
//         RoutePostCommentDTO dto = new RoutePostCommentDTO();
//         dto.setUserId("1"); // 존재하는 사용자 ID
//         dto.setRoutepostId("1"); // 존재하는 게시글 ID
//         dto.setRoutepostContent("JUnit 테스트 댓글입니다 😎");

//         int result = mapper.add(dto);
//         assertEquals("댓글 등록 실패", 1, result);
//         System.out.println("✅ 댓글 등록 성공");
//     }

//     // ✅ 4. 댓글 삭제 테스트
//     @Test
//     public void testDelete() {
//         String commentId = "1"; // 실제 DB에 존재하는 댓글 ID로 변경
//         int result = mapper.del(commentId);
//         assertEquals("댓글 삭제 실패", 1, result);
//         System.out.println("✅ 댓글 삭제 성공");
//     }

//     // ✅ 5. 댓글 개수 테스트
//     @Test
//     public void testCount() {
//         String routepostId = "1";
//         int count = mapper.count(routepostId);
//         System.out.println("✅ 게시글 " + routepostId + "의 댓글 개수: " + count);
//     }
// }

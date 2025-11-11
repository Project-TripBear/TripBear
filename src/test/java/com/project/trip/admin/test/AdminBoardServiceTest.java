package com.project.trip.admin.test; // 형님의 테스트 클래스 패키지

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// 1. 형님이 보내주신 DTO를 import 합니다.
import com.project.trip.admin.board.model.IntegratedBoardDTO;

// 2. 형님이 테스트하려는 Service를 import 합니다. (패키지 경로는 예시입니다)
import com.project.trip.admin.board.service.AdminBoardService; 

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class AdminBoardServiceTest {

    // log.info()를 사용하기 위한 Logger 객체
    private static final Logger log = LoggerFactory.getLogger(AdminBoardServiceTest.class);

    // 3. 테스트할 Service를 주입받습니다.
    @Autowired
    private AdminBoardService service; // (Service 객체 이름이 다를 수 있습니다)

    @Test
    public void testGetBoardList() {
        log.info("--- 통합 게시판 조회 테스트 시작 ---");

        // 4. Service의 메서드를 호출하여 데이터를 List로 받아옵니다.
        // (메서드 이름은 getIntegratedBoardList()가 아닐 수도 있습니다)
        List<IntegratedBoardDTO> list = service.getIntegratedBoardList();

        // 5. (필수) 데이터가 null이 아닌지 확인합니다.
        assertNotNull(list);

        // 6. (★★핵심★★) 받아온 데이터를 콘솔에 출력합니다.
        log.info("--- DB에서 조회된 데이터 ---");
        
        if (list.isEmpty()) {
            log.warn("조회된 데이터가 없습니다.");
        } else {
            log.info("조회된 총 개수: " + list.size());
            
            // DTO에 @ToString이 있으므로 list.toString()으로 전체 데이터를 볼 수 있습니다.
            log.info("전체 목록: " + list.toString()); 

            // (선택) 또는, for문으로 하나씩 더 깔끔하게 볼 수 있습니다.
            log.info("--- 개별 항목 출력 (for문) ---");
            for (IntegratedBoardDTO dto : list) {
                log.info(String.format("글번호: %d, 제목: %s, 닉네임: %s",
                             dto.getSeq(), dto.getTitle(), dto.getNickname()));
            }
        }

        log.info("--- 테스트 정상 종료 ---");
    }
}
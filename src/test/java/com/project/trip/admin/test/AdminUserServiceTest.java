package com.project.trip.admin.test;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.project.trip.admin.user.model.AdminUserDTO;
import com.project.trip.admin.user.service.AdminUserService;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Log4j
public class AdminUserServiceTest {

    @Autowired
    private AdminUserService service;

    @Test
    public void testGetUserList() {
        
		
		 log.info("--- 회원 목록 조회 테스트 시작 ---");
		  
		 List<AdminUserDTO> list = service.getUserList();
		  
		 assertNotNull("Service가 null을 반환했습니다.", list);
		 
		 log.info("총 " + list.size() + "명의 회원이 조회되었습니다.");
		  
		 list.stream().limit(5).forEach(dto -> { log.info(dto); });
		  
		 log.info("--- 테스트 성공 ---");
		 
    }

}
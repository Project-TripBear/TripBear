package com.project.trip.mapper;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.model.UserDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"file:src/main/webapp/WEB-INF/spring/security-context.xml"
})
public class MemberTest {
	
	//의존 주입의 타입 > 인터페이스 + 상속 구현한 클래스 2개 이상
	//1. @Qualifier 사용
	//2. 딱 1개 클래스만 구현, 나머지는 삭제
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private MemberMapper mapper;
	
	@Test
	public void testEncoder() {
		
		//No qualifying bean of type 'org.springframework.security.crypto.password.PasswordEncoder' available: expected single matching bean but found 2: customNoOpPasswordEncoder,bCryptPasswordEncoder
		assertNotNull(encoder);
		
		String pw = "1111";
		System.out.println( "확인: "+ encoder.encode(pw));
		
	}
	

	
	@Test
	public void getTest() {
		
		assertNotNull(mapper);
		
		UserDTO dto = mapper.get("kimminjun");
		
		System.out.println("dto : " + dto);
		
	}
	
	@Test
	public void addTest() {
		
		assertNotNull(mapper);
		
		UserDTO dto = new UserDTO();
		dto.setId("cat");
		dto.setPw(encoder.encode("1111"));
		dto.setSsn("111111-1111111");
		dto.setPhoneNumber("010-1111-1111");
		dto.setNickName("고양이11");
		dto.setName("고양이");
		dto.setEmail("cat@gmail.com");
		dto.setGender("f");
		dto.setAddress("서울시 강남구");
		dto.setHeight("175");
		dto.setWeight("75");
		dto.setHealthGoals("11");
		
		mapper.add(dto);
		
		System.out.println("dto : " + dto);
		
	}


}














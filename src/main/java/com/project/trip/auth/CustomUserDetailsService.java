package com.project.trip.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.model.CustomUser;
import com.project.trip.mypage.model.UserDTO;

//User > CustomUser 사용
public class CustomUserDetailsService implements UserDetailsService {

	// DB > select > User
	
	//1. /customlogin.do > 아이디(hong), 암호(1111) 입력
	//2. POST +/login > 인증처리
	//2.5 2번과 더불어 loadUserByUsername() 호출
	
	@Autowired  //필드주입과 클래스에 다는 어노테이션 차이점 확인하기!!!
	private MemberMapper mapper;
	
	
	//로그인 발생 시 같이 호출
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDTO dto = mapper.get(username);
		if (dto == null) {
	        throw new UsernameNotFoundException("아이디를 찾을 수 없습니다."); // 사용자가 없는 경우
	    }
	    
	    // 차단 상태일 때 DisabledException 발생시키기
	    if (dto.getAuth().equals("DELETED") || dto.getAuth().equals("BANNED")) {
	        
	        String message = dto.getAuth().equals("DELETED") 
	                       ? "탈퇴 처리된 계정입니다." 
	                       : "관리자에 의해 차단된 계정입니다.";
	                       
	        // 💡 중요: DisabledException을 던져서 Handler가 이 상태를 인지하게 합니다.
	        throw new DisabledException(message); 
	    }
	    
	    return new CustomUser(dto);
	    
//		System.out.println("테스트 : " +dto);
//		if(dto.getAuth().equals("DELETED") || dto.getAuth().equals("BANNED")) {
//			
//			System.out.println("차단된 아이디");
//			
//			return null;
//		}
//		
//		return dto != null ? new CustomUser(dto) : null;
	}
	
	
}

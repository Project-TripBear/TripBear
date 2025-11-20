package com.project.trip.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.model.CustomUser;
import com.project.trip.mypage.model.UserDTO;

/**
 * Spring Security의 UserDetailsService를 구현한 사용자 정의 서비스 클래스입니다.
 * <p>
 * 사용자가 로그인을 시도할 때, 입력된 사용자 이름(ID)을 기반으로 데이터베이스에서 사용자 정보를 조회하고,
 * Spring Security가 인증을 처리할 수 있도록 {@link UserDetails} 객체를 생성하여 반환합니다.
 * </p>
 */
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private MemberMapper mapper;
	
	/**
	 * 사용자 이름(ID)을 사용하여 사용자 정보를 로드합니다.
	 * <p>
	 * Spring Security가 인증 과정에서 이 메소드를 호출합니다.
	 * 데이터베이스에서 사용자 정보를 조회한 후, {@link CustomUser} 객체로 변환하여 반환합니다.
	 * </p>
	 * @param username 사용자가 로그인 시 입력한 아이디
	 * @return 인증에 사용할 사용자 상세 정보가 담긴 {@link UserDetails} 객체
	 * @throws UsernameNotFoundException 해당 아이디의 사용자를 찾을 수 없을 경우 발생
	 * @throws DisabledException 사용자가 탈퇴했거나 관리자에 의해 차단된 경우 발생
	 */
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
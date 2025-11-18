package com.project.trip.mypage.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

/**
 * Spring Security에서 사용자 인증 정보를 담는 {@link User} 클래스를 확장한 커스텀 클래스입니다.
 * {@link UserDTO} 객체를 포함하여 추가적인 사용자 정보를 제공합니다.
 */
@Getter
public class CustomUser extends User {

	/**
	 * 사용자 상세 정보를 담고 있는 {@link UserDTO} 객체입니다.
	 */
	private UserDTO udto;
	
	/**
	 * 기본 생성자입니다.
	 * 익명 사용자를 나타내며, {@link UserDTO} 객체도 기본값으로 초기화됩니다.
	 */
	public CustomUser() {
	        super("anonymous", "anonymous", List.of());
	        this.udto = new UserDTO();
	    }
	
	/**
	 * 사용자 이름, 비밀번호, 권한 목록을 받아 {@link User} 객체를 생성하는 생성자입니다.
	 *
	 * @param username 사용자의 아이디
	 * @param password 사용자의 비밀번호
	 * @param authorities 사용자의 권한 목록
	 */
	public CustomUser(String username, String password,
						Collection<? extends GrantedAuthority> authorities) {
			super(username, password, authorities);
	}
	
	
	/**
	 * {@link UserDTO} 객체를 받아 {@link User} 객체를 생성하는 생성자입니다.
	 * {@link UserDTO}의 아이디, 비밀번호, 권한을 사용하여 Spring Security의 User 객체를 초기화합니다.
	 *
	 * @param dto 사용자 정보를 담은 {@link UserDTO} 객체
	 */
	public CustomUser(UserDTO dto) {

        super(dto.getId(),
              dto.getPw(),
              List.of(new SimpleGrantedAuthority(dto.getAuth())));


        this.udto = dto;

    }
	

}

package com.project.trip.mypage.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

//회원 정보 객체
//- 기존의 User 대신
@Getter
public class CustomUser extends User {

	private UserDTO udto;
	
	public CustomUser(String username, String password,
						Collection<? extends GrantedAuthority> authorities) {
			super(username, password, authorities);
	}
	
	
	public CustomUser(UserDTO dto) {

        super(dto.getId(),
              dto.getPw(),
              List.of(new SimpleGrantedAuthority(dto.getAuth())));


        this.udto = dto;

    }
	

}

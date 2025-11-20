package com.project.trip.admin.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// ... import 문 ...
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.trip.admin.auth.mapper.AdminMapper;
import com.project.trip.admin.auth.model.AdminDTO; // 🚨 DTO 경로는 실제 경로에 맞게 확인!
import com.project.trip.admin.auth.model.CustomAdminUser;

/**
 * 관리자 로그인을 위해 Spring Security의 {@link UserDetailsService}를 구현한 사용자 정의 서비스입니다.
 * <p>
 * 관리자가 로그인을 시도할 때, 입력된 사용자 이름(ID)을 기반으로 데이터베이스에서 관리자 정보를 조회하고,
 * Spring Security가 인증을 처리할 수 있도록 {@link CustomAdminUser} 객체를 생성하여 반환합니다.
 * </p>
 */
@Service
public class CustomAdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper; // 1단계에서 수정한 매퍼

    /**
     * 관리자 사용자 이름(ID)을 사용하여 관리자 정보를 로드합니다.
     * <p>
     * Spring Security가 관리자 인증 과정에서 이 메소드를 호출합니다.
     * 데이터베이스에서 관리자 정보를 조회한 후, 권한 정보와 함께 {@link CustomAdminUser} 객체로 변환하여 반환합니다.
     * </p>
     * @param username 관리자가 로그인 시 입력한 아이디
     * @return 인증에 사용할 관리자 상세 정보가 담긴 {@link UserDetails} 객체
     * @throws UsernameNotFoundException 해당 아이디의 관리자를 찾을 수 없을 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        AdminDTO admin = adminMapper.getAdmin(username);

        if (admin == null) {
            throw new UsernameNotFoundException("관리자 계정 없음");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // ★ 관리자도 CustomUser처럼 사용 가능하게 반환
        return new CustomAdminUser(admin, authorities);
    }
}
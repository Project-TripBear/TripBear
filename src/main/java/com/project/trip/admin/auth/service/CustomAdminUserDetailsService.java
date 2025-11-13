package com.project.trip.admin.auth.service;

// ... import 문 ...
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User; // 🚨 Spring Security의 User
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.admin.auth.mapper.AdminMapper;
import com.project.trip.admin.auth.model.AdminDTO; // 🚨 DTO 경로는 실제 경로에 맞게 확인!

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomAdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper; // 1단계에서 수정한 매퍼

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. 1단계에서 수정한 쿼리 호출 (adminId, adminPw만 가져옴)
        AdminDTO admin = adminMapper.getAdmin(username); 

        // 2. 사용자가 없으면 예외 발생
        if (admin == null) {
            throw new UsernameNotFoundException("관리자 계정을 찾을 수 없습니다: " + username);
        }

        // 3. ▼▼▼ 핵심 ▼▼▼
        // DB에 권한 컬럼이 없으므로, 여기서 강제로 권한을 생성합니다.
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // 👈 무조건 'ROLE_ADMIN' 권한 부여
        authorities.add(new SimpleGrantedAuthority("ACTIVE"));     // 👈 2. 이 줄을 추가!
        // 4. Spring Security의 User 객체로 만들어서 반환
        // (DB에서 가져온 암호화된 비밀번호와, 방금 만든 권한 목록을 넣어줍니다)
        return new User(admin.getAdminId(), admin.getAdminPw(), authorities);
    }
}
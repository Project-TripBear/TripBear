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

@Service
public class CustomAdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper; // 1단계에서 수정한 매퍼

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
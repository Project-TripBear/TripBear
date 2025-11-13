// 경로: com.project.trip.admin.auth.model.CustomAdminUser.java
package com.project.trip.admin.auth.model;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.Getter;

@Getter
public class CustomAdminUser extends User {
    
    private AdminDTO adto;
    
    public CustomAdminUser(AdminDTO dto) {
        super(dto.getAdminId(), // Admin 아이디
              dto.getAdminPw(),   // Admin 비밀번호
              List.of(new SimpleGrantedAuthority(dto.getAuth()))); // Admin 권한 (예: "ROLE_ADMIN")
        
        this.adto = dto;
    }
}
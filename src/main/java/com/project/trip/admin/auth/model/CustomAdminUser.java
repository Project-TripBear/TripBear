package com.project.trip.admin.auth.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;



import lombok.Getter;

/**
 * 관리자 계정용 CustomUser
 * - Spring Security 기본 User 상속
 * - 관리자도 CustomUser처럼 컨트롤러에서 동일하게 처리하도록 만든 클래스
 */
@Getter
public class CustomAdminUser extends User {

    private final AdminDTO adto;   // 관리자 정보 DTO

    /**
     * {@link AdminDTO} 객체와 권한 목록을 받아 {@link User} 객체를 생성하는 생성자입니다.
     * {@link AdminDTO}의 관리자 ID, 비밀번호를 사용하여 Spring Security의 User 객체를 초기화합니다.
     *
     * @param adto 관리자 정보를 담은 {@link AdminDTO} 객체
     * @param authorities 관리자의 권한 목록
     */
    public CustomAdminUser(AdminDTO adto, Collection<? extends GrantedAuthority> authorities) {
        super(adto.getAdminId(), adto.getAdminPw(), authorities);
        this.adto = adto;
    }

    /**
     * 관리자 ID를 user_id처럼 반환할 수 있게 하는 편의 메서드
     * 예: 컨트롤러에서 dto.getUser_id() 비교 시 사용
     */
    public String getAdminSeq() {
        return adto.getAdminId();  // 관리자ID를 seq처럼 사용
    }
}

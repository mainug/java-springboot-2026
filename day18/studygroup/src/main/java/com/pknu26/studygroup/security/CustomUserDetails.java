package com.pknu26.studygroup.security;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pknu26.studygroup.dto.User;

import lombok.Getter;

// SpringSecurity와 기존 사용자테이블 연결
@Getter
public class CustomUserDetails implements UserDetails {
    
    private final Long userId;
    private final String loginId;
    private final String password;
    private final String name;
    private final String role;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.userId = user.getUserId();
        this.loginId = user.getLoginId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.role = user.getRole();
        this.authorities = authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    public Long getUserId() {
        return this.userId;
    }
    
}

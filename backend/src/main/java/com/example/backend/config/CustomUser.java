package com.example.backend.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class CustomUser implements UserDetails {

    private final String email;
    private final String password;
    private final String role;
    private final boolean isEnable;
    private final boolean accountNonLocked;

    public CustomUser(String email, String password, String role, Boolean isEnable, Boolean accountNonLocked) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.isEnable = Boolean.TRUE.equals(isEnable);
        this.accountNonLocked = Boolean.TRUE.equals(accountNonLocked);
    }

    public static CustomUser fromEntity(com.example.backend.model.entity.User user) {
        return new CustomUser(
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getIsEnable(),
                user.getAccountNonLocked()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }

}
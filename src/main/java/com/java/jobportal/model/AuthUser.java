package com.java.jobportal.model;

import com.java.jobportal.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class AuthUser implements UserDetails {
    private String username;
    private String password;
    private Role role;
    private boolean isActive;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role.toString())); // ADMIN,USER,COMPANY
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public AuthUser(){
    }
    public AuthUser(User user){
        username = user.getName();
        password = user.getPassword();
        role = user.getRole();
        this.isActive = !user.isDisable();
    }
}

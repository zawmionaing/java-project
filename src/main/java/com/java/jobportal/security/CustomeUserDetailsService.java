package com.java.jobportal.security;

import com.java.jobportal.model.AuthUser;
import com.java.jobportal.model.User;
import com.java.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomeUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUserNameOrEmail(username,username);
        return new AuthUser(user);
    }
}

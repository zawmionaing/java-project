package com.java.jobportal.service;

import com.java.jobportal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Page<User> getUser(int page, int limit);
    
    User getById (Long id) throws Exception;
    
    void saveOrUpdate(User user);

    void save(User user);

    void save(Long id, boolean disable);

    User getByUserNameOrEmail(String username, String email);

    User getUserByName(String name);

    boolean isUserExists(String name);

    boolean isNameOrEmailUnique(String name, String email);

    boolean isNameUnique(String name);

    boolean isEmailUnique(String email);

    User getByEmail(String email);
    
    void updatePassword(User userDetail, String newPassword);
}
